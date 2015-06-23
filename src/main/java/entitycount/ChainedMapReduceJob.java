package entitycount;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.mapreduce.*;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.inputs.DatastoreKeyInput;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLineInput;
import com.google.appengine.tools.mapreduce.outputs.DatastoreOutput;
import com.google.appengine.tools.mapreduce.outputs.InMemoryOutput;
import com.google.appengine.tools.pipeline.FutureValue;
import com.google.appengine.tools.pipeline.Job0;
import com.google.appengine.tools.pipeline.Job1;
import com.google.appengine.tools.pipeline.Value;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

// [START chain_job_example]

/**
 * Runs three MapReduces in a row. The first creates random MapReduceTest entities of the type
 * {@link #datastoreType}. The second counts the number of each character in these entities. The
 * third deletes all entities of the {@link #datastoreType}.
 */
public class ChainedMapReduceJob extends Job0<Void> {

    private static final long serialVersionUID = 6725038763886885189L;
    private static final Logger log = Logger.getLogger(ChainedMapReduceJob.class.getName());

    private final String bucket;
    private final String datastoreType;
    private final int shardCount;
    private final int entities;
    private final int bytesPerEntity;

    private static class LogResults extends
            Job1<Void, MapReduceResult<List<List<KeyValue<String, Long>>>>> {

        private static final long serialVersionUID = 131906664096202890L;

        @Override
        public Value<Void> run(MapReduceResult<List<List<KeyValue<String, Long>>>> mrResult)
                throws Exception {
            return null;
        }
    }

    public ChainedMapReduceJob(String bucket, String datastoreType, int shardCount, int entities,
                               int bytesPerEntity) {
        this.bucket = bucket;
        this.datastoreType = datastoreType;
        this.shardCount = shardCount;
        this.entities = entities;
        this.bytesPerEntity = bytesPerEntity;
    }

    @Override
    public FutureValue<Void> run() throws Exception {
        MapSettings settings = getSettings();
        FutureValue<MapReduceResult<Void>> createFuture = futureCall(
                new MapJob<>(getCreationJobSpec(bytesPerEntity, entities, shardCount), settings));

        FutureValue<MapReduceResult<List<List<KeyValue<String, Long>>>>> countFuture = futureCall(
                new MapReduceJob<>(getCountJobSpec(shardCount, shardCount),
                        new MapReduceSettings.Builder(settings).setBucketName(bucket).build()),
                waitFor(createFuture));

        return futureCall(new LogResults(), countFuture, waitFor(countFuture));
    }

    public FutureValue<MapReduceResult<List<List<KeyValue<String, Long>>>>> handleException(
            MapReduceJobException exception) throws Throwable {
        // one of the child MapReduceJob has failed
        log.severe("MapReduce job failed because of: " + exception.getMessage());
        // ... Send an email, try again, ... or fail
        throw exception;
    }

    // ...
    // [END chain_job_example]

    private MapSettings getSettings() {
        // [START mapSettings]
        MapSettings settings = new MapSettings.Builder()
                .setWorkerQueueName("mapreduce-workers")
                .setModule("default")
                .build();
        // [END mapSettings]
        return settings;
    }

    private MapSpecification<byte[], Entity, Void> getCreationJobSpec(int bytesPerEntity, int entities,
                                                                    int shardCount) {
        GcsFilename filename = new GcsFilename("bnifsc_beta", "csv2015June10.csv");
        GcsService gcsService = GcsServiceFactory.createGcsService();
        try{
            log.warning(gcsService.getMetadata(filename).toString());
        }catch (IOException ie){
            log.warning(ie.getMessage());
        }
        byte separator = (byte)'\n';
        // [START mapSpec]
        MapSpecification<byte[], Entity, Void> spec = new MapSpecification.Builder<>(
                new GoogleCloudStorageLineInput(filename, separator , shardCount),
                new EntityCreator(datastoreType, bytesPerEntity),
                new DatastoreOutput())
                .setJobName("Create MapReduce entities")
                .build();
        // [END mapSpec]
        return spec;
    }

    private MapReduceSpecification<Entity, String, Long, KeyValue<String, Long>,
            List<List<KeyValue<String, Long>>>> getCountJobSpec(int mapShardCount, int reduceShardCount) {
        Query query =
                new Query(datastoreType).setFilter(new FilterPredicate("foo", FilterOperator.EQUAL, "bar"));

        return new MapReduceSpecification.Builder<>(new DatastoreInput(query, mapShardCount),
                new CountMapper(), new CountReducer(), new InMemoryOutput<KeyValue<String, Long>>())
                .setKeyMarshaller(Marshallers.getStringMarshaller())
                .setValueMarshaller(Marshallers.getLongMarshaller())
                .setJobName("MapReduceTest count")
                .setNumReducers(reduceShardCount)
                .build();
    }

    private MapSpecification<Key, Void, Void> getDeleteJobSpec(int mapShardCount) {
        DatastoreKeyInput input = new DatastoreKeyInput(datastoreType, mapShardCount);
        DeleteEntityMapper mapper = new DeleteEntityMapper();
        return new MapSpecification.Builder<Key, Void, Void>(input, mapper)
                .setJobName("Delete MapReduce entities")
                .build();
    }
}
