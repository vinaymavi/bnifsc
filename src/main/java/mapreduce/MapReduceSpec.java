package mapreduce;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLineInput;
import com.google.appengine.tools.mapreduce.outputs.DatastoreOutput;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by vinaymavi on 31/12/15.
 */
public class MapReduceSpec {
    public static final Logger log = Logger.getLogger(MapReduceSpec.class.getName());
    public static final int SHARD_COUNT = 20;
    public static final String KIND = "Branch";

    private MapReduceSpec() {

    }

    /**
     * Create Job specification.
     *
     * @param kind
     * @param bucket
     * @param gcsfile
     * @return {{MapSpecification}}
     */
    public static MapSpecification<byte[], Entity, Void> getBranchSpec(String kind, String bucket, GcsFilename gcsfile) {

        String jobName = "Branch_upload";
        byte separator = (byte) '\n';
        // [START mapSpec]
        MapSpecification<byte[], Entity, Void> spec = new MapSpecification.Builder<>(
                new GoogleCloudStorageLineInput(gcsfile, separator, SHARD_COUNT),
                new BranchMapper(kind),
                new DatastoreOutput())
                .setJobName(jobName)
                .build();
        // [END mapSpec]
        return spec;
    }

    /**
     * Create Job specification.
     *
     * @param kind
     * @param bucket
     * @param gcsfile
     * @return {{MapSpecification}}
     */
    public static MapSpecification<byte[], Entity, Void> getBankSpec(String kind, String bucket, GcsFilename gcsfile) {
        String jobName = "Bank_upload";
        byte separator = (byte) '\n';
        MapSpecification<byte[], Entity, Void> spec = new MapSpecification.Builder<>(
                new GoogleCloudStorageLineInput(gcsfile, separator, SHARD_COUNT),
                new BankMapper(kind),
                new DatastoreOutput())
                .setJobName(jobName)
                .build();
        return spec;
    }
}
