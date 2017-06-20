package servlet;

import com.google.appengine.tools.mapreduce.OutputWriter;
import mail.AppengineMail;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Servlet for mail testing.
 */
public class MailServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MailServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            InternetAddress to = new InternetAddress("vinaymavi@gmail.com", "Mr.Vinay");
            if (AppengineMail.sendMail(to, "Hello Vinay, Test mail from bnifsc.com", "This is test mail in text format.")) {
                out.write("Mail Sent");
            } else {
                out.write("Error in mail Sending, Please check logs for more info.");
            }
        } catch (IOException ioe) {
            logger.warning(ioe.getMessage());
        }

    }
}
