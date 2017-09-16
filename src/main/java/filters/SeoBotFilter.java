package filters;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SeoBotFilter implements Filter {
    final static Logger logger = Logger.getLogger(SeoBotFilter.class.getName());
    private static final String HTTPS = "https";
    private static final String HTTP = "http";
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        String _escaped_fragment_ = req.getParameter("_escaped_fragment_");
        logger.warning("_escaped_fragment_=" + _escaped_fragment_);
        String url = "";
        String remoteAddress = "";

        if (req instanceof HttpServletRequest) {
            url = ((HttpServletRequest) req).getRequestURL().toString();
            String uri = ((HttpServletRequest) req).getRequestURI().toString();
            remoteAddress = ((HttpServletRequest) req).getRemoteAddr().toString();
            String remoteHost = ((HttpServletRequest) req).getRemoteHost().toString();
            int remotePort = ((HttpServletRequest) req).getRemotePort();
            String queryString = ((HttpServletRequest) req).getQueryString();
            logger.warning("remoteaddress=" + remoteAddress);
            logger.warning("remoteHost=" + remoteHost);
            logger.warning("remoteport=" + remotePort);
            logger.warning(url);
            logger.warning(uri);
            logger.warning(queryString);
        }
        if (url.contains("appspot") || !url.contains("https")) {
            HttpServletResponse response = (HttpServletResponse)resp;
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", url.replaceAll(HTTP, HTTPS));
        } else if (_escaped_fragment_ != null) {
            req.getRequestDispatcher("/app/seo").forward(req, resp);
        } else {
            logger.warning("Else Part");
            req.getRequestDispatcher("index.html").forward(req, resp);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // TODO Auto-generated method stub
    }

}
