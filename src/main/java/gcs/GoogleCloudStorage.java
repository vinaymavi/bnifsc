package gcs;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by vinaymavi on 31/12/15.
 */
public class GoogleCloudStorage {
    public static Logger logger = Logger.getLogger(GoogleCloudStorage.class.getName());

    public static GcsFilename createFile(String bucket, String object) {
        GcsFilename filename = new GcsFilename(bucket, object);
        GcsService gcsService = GcsServiceFactory.createGcsService();
        try {
            logger.warning(gcsService.getMetadata(filename).toString());
            return filename;
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
            return null;
        } catch (NullPointerException npe) {
            logger.warning(npe.getMessage());
            return null;
        }

    }
}
