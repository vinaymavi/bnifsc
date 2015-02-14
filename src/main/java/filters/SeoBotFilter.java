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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
public class SeoBotFilter implements  Filter {
	final static Logger logger = Logger.getLogger(SeoBotFilter.class.getName());
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		    String _escaped_fragment_ = req.getParameter("_escaped_fragment_");
		    logger.warning("_escaped_fragment_="+_escaped_fragment_);
		    
		    if (req instanceof HttpServletRequest) {
		    	 String url = ((HttpServletRequest)req).getRequestURL().toString();
		    	 String uri = ((HttpServletRequest)req).getRequestURI().toString();
		    	 String queryString = ((HttpServletRequest)req).getQueryString();
		    	 logger.warning(url);
		    	 logger.warning(uri);
		    	 logger.warning(queryString);
		    }
		    
		    if(_escaped_fragment_ != null ){		    				   
			    resp.getWriter().write("SEO Site");
		    }else{
		    	logger.warning("Else Part");
		    	req.getRequestDispatcher("index.html").forward(req, resp);
//		    	chain.doFilter(req, resp);
		    }
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub		
	}

}
