package servlets;

import java.io.*;
import java.rmi.ServerException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileGetter extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServerException, IOException {

		String color = req.getParameter("color");
		res.setContentType("text/html");
		PrintWriter pw = res.getWriter();
		pw.println("<b>The selected color is: </b>");
		pw.println(color);
		pw.close();
	}
}
