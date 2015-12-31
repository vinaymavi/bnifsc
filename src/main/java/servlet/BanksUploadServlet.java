package servlet;

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
            PrintWriter writer = resp.getWriter();
            writer.write("<h1>Banks uploading to checking status click bellow link.</h1>");
            writer.write("<a href='#'>check status</>");
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        }
    }
}
