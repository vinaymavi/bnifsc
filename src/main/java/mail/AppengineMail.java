package mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Class that will use for all mail communications.
 */
public class AppengineMail {
    private static final Logger logger = Logger.getLogger(AppengineMail.class.getName());

    public static boolean sendMail(InternetAddress to, String subject, String message) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("vinaymavi@gmail.com", "bnifsc.com Admin"));
            msg.addRecipient(Message.RecipientType.TO, to);
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            return true;
        } catch (AddressException e) {
            logger.warning(e.getMessage());
        } catch (MessagingException e) {
            logger.warning(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.warning(e.getMessage());
        }
        return false;
    }
}
