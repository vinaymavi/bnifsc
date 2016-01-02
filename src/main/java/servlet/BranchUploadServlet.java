package servlet;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.apphosting.api.ApiProxy;
import gcs.GoogleCloudStorage;
import mapreduce.MapReduceSettings;
import mapreduce.MapReduceSpec;
import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.pipeline.PipelineService;
import com.google.appengine.tools.pipeline.PipelineServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Created by vinaymavi on 04/07/15.
 * This is servlet to start Branch uploader.
 */
public class BranchUploadServlet extends HttpServlet {

    public static final Logger logger = Logger.getLogger(BranchUploadServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
        logger.warning("host=" + env.getAttributes().get("com.google.appengine.runtime.default_version_hostname"));
        String host = (String) env.getAttributes().get("com.google.appengine.runtime.default_version_hostname");

        String KIND = "Branch";
        logger.warning("app_id=" + host.split("\\.")[0]);
        String bucket = host.split("\\.")[0];
        String gcsObject = "csv/banks.csv";
        PipelineService service = PipelineServiceFactory.newPipelineService();
        try {
            GcsFilename gcsfile = GoogleCloudStorage.createFile(bucket, gcsObject);
            if (gcsfile != null) {
                String pipelineId = service.startNewPipeline(new MapJob<>(MapReduceSpec.getBranchSpec(KIND, bucket, gcsfile), MapReduceSettings.getSettings()));
                logger.warning("tracking URL @/_ah/pipeline/status.html?root=" + pipelineId);
                resp.sendRedirect("/_ah/pipeline/status.html?root=" + pipelineId);
            } else {
                logger.warning("csv/branch.csv not exist");
                resp.getWriter().write("csv/branch.csv not exist.");
            }
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        }
    }
}
