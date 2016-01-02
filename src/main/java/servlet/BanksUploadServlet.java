package servlet;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.PipelineServiceFactory;
import com.google.apphosting.api.ApiProxy;
import gcs.GoogleCloudStorage;
import mapreduce.MapReduceSettings;
import mapreduce.MapReduceSpec;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by vinaymavi on 31/12/15.
 */
public class BanksUploadServlet extends HttpServlet {
    public final Logger logger = Logger.getLogger(BanksUploadServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
            logger.warning("host=" + env.getAttributes().get("com.google.appengine.runtime.default_version_hostname"));
            String host = (String) env.getAttributes().get("com.google.appengine.runtime.default_version_hostname");

            String KIND = "Bank";
            logger.warning("app_id=" + host.split("\\.")[0]);
            String bucket = host.split("\\.")[0];
            String gcsObject = "csv/banks.csv";
            PipelineService service = PipelineServiceFactory.newPipelineService();
            GcsFilename gcsfile = GoogleCloudStorage.createFile(bucket, gcsObject);
            String pipelineId = service.startNewPipeline(new MapJob<>(MapReduceSpec.getBankSpec(KIND, bucket, gcsfile),
                    MapReduceSettings.getSettings()));
            logger.warning("tracking URL @/_ah/pipeline/status.html?root=" + pipelineId);
            resp.sendRedirect("/_ah/pipeline/status.html?root=" + pipelineId);
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        }
    }
}
