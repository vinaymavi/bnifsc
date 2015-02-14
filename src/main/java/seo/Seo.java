package seo;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bnifsc.entites.Branch;

public class Seo extends HttpServlet  {
	public static final Logger logger = Logger.getLogger(Seo.class.getName());
	private static final long serialVersionUID = 1L;
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse resp){
		String queryString  = req.getParameter("_escaped_fragment_");
		Branch branch = new Branch();						
		String[] queryParams = URLDecoder.decode(queryString).split("/");		
		logger.warning(URLDecoder.decode(queryString));
		logger.warning(""+queryParams.length);		
		switch (queryParams.length) {
		case 1:
			req.setAttribute("banksList", branch.banks());			
			try {
				req.getRequestDispatcher("/seo/banks.jsp").forward(req, resp);				
			} catch (Exception e) {
				logger.warning(e.getMessage());
			}
						
			break;
		case 2:
			break;
		case 3:
			break;
			
		default:
			break;
		}
		
	}

}
