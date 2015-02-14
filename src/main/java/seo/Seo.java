package seo;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Seo extends HttpServlet  {
	public static final Logger logger = Logger.getLogger(Seo.class.getName());
	private static final long serialVersionUID = 1L;
	@Override
	public void doGet(HttpServletRequest req,HttpServletResponse resp){
		try {
			resp.getWriter().write("Hello World");	
		} catch (IOException e) {
			logger.warning(e.getMessage());
		}
		
	}

}
