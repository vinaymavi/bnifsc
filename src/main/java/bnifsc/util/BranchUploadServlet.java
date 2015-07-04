package bnifsc.util;

import com.google.appengine.tools.mapreduce.MapJob;
import com.google.appengine.tools.mapreduce.MapSettings;
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
                resp.sendRedirect(BranchUpload.upload(bucket, csvFile));
            } catch (IOException ioe) {
                logger.warning(ioe.getMessage());
            }
        }
    }
}
