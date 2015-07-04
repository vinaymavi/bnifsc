package bnifsc.util;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapSettings;
import com.google.appengine.tools.mapreduce.MapSpecification;
import com.google.appengine.tools.mapreduce.inputs.GoogleCloudStorageLineInput;
import com.google.appengine.tools.mapreduce.outputs.DatastoreOutput;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.PipelineServiceFactory;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by vinaymavi on 04/07/15.
 * This file created to upload branches to datastore and search index.
 */
public class BranchUpload {
    public static final Logger log = Logger.getLogger(BranchUpload.class.getName());
    public static final int SHART_COUNT = 10;
    public static final String KIND = "Branch";

    /**
     * Job status url.
     *
     * @param pipelineId
     * @return {{String}}
     */
    private String getPipelineStatusUrl(String pipelineId) {
        return "/_ah/pipeline/status.html?root=" + pipelineId;
    }

    /**
     * Job Settings.
     *
     * @return {{MapSettings}}
     */
    private MapSettings getSettings() {
        // [START mapSettings]
        MapSettings settings = new MapSettings.Builder()
                .setWorkerQueueName("mapreduce-workers")
                .setModule("default")
                .build();
        // [END mapSettings]
        return settings;
    }

    /**
     * Create Job specification.
     *
     * @param kind
     * @param bucket
     * @param csvFile
     * @return {{MapSpecification}}
     */
    private MapSpecification<byte[], Entity, Void> getCreationJobSpec(String kind, String bucket, String csvFile) {
        GcsFilename filename = new GcsFilename(bucket, csvFile);
        GcsService gcsService = GcsServiceFactory.createGcsService();
        try {
            log.warning(gcsService.getMetadata(filename).toString());
        } catch (IOException ie) {
            log.warning(ie.getMessage());
        }
        byte separator = (byte) '\n';
        // [START mapSpec]
        MapSpecification<byte[], Entity, Void> spec = new MapSpecification.Builder<>(
                new GoogleCloudStorageLineInput(filename, separator, SHART_COUNT),
                new EntityCreator(kind),
                new DatastoreOutput())
                .setJobName("Create MapReduce entities")
                .build();
        // [END mapSpec]
        return spec;
    }

    /**
     * Start uploading to datastore.
     *
     * @param bucket
     * @param csvFile
     * @return {{String}} pipelineId
     */
    public static String upload(String bucket, String csvFile) {
        BranchUpload bu = new BranchUpload();
        PipelineService service = PipelineServiceFactory.newPipelineService();
        String pipelineId = service.startNewPipeline(new MapJob<>(bu.getCreationJobSpec(KIND, bucket, csvFile), bu.getSettings()));
        String redirectUrl = bu.getPipelineStatusUrl(pipelineId);
        return redirectUrl;
    }
}
