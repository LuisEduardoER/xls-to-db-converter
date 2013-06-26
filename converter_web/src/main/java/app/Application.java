package app;

import java.io.*;
import java.rmi.ServerException;

import javax.servlet.*;

public class Application extends GenericServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(ServletRequest req, ServletResponse res)
			throws ServerException, IOException {
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		pw.println("<B>Hello!");
		pw.close();
	}
}
