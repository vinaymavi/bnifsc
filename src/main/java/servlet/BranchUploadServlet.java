package servlet;

import com.google.appengine.tools.cloudstorage.GcsFilename;
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
        try {
            PrintWriter out = resp.getWriter();
            out.write("<html>" +
                    "<head>" +
                    "<title>Branch Upload</title>" +
                    "</head>" +
                    "<body>" +
                    "<form method='post'>" +
                    "Email <input type='text' name='email'/><br/>" +
                    "Bucket <input type='text' name='bucket'/><br/>" +
                    "CSV File <input type='text' name='csvfile'/><br/>" +
                    "<input type='submit'/>" +
                    "</form>" +
                    "</body>" +
                    "</html>");
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String email = req.getParameter("email");
        String bucket = req.getParameter("bucket");
        String csvFile = req.getParameter("csvfile");
        logger.warning("Branch Uploader Email address=" + email);
        if (email.equals("vinaymavi@gmail.com")) {
            try {
                String KIND = "Branch";
                PipelineService service = PipelineServiceFactory.newPipelineService();
                GcsFilename gcsfile = GoogleCloudStorage.createFile(bucket, "csv/" + csvFile);
                String pipelineId = service.startNewPipeline(new MapJob<>(MapReduceSpec.getBranchSpec(KIND, bucket, gcsfile), MapReduceSettings.getSettings()));
                logger.warning("tracking URL @/_ah/pipeline/status.html?root=" + pipelineId);
                resp.sendRedirect("/_ah/pipeline/status.html?root=" + pipelineId);
            } catch (IOException ioe) {
                logger.warning(ioe.getMessage());
            }
        }
    }
}
