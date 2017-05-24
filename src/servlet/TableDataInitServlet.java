package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.MySQLUtil;


public class TableDataInitServlet extends HttpServlet{
private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
		request.setCharacterEncoding("utf-8");
		
		String condition = request.getParameter("condition");
		//String result =  MySQLUtil.getUser(condition);
		String result = MySQLUtil.getUser(condition);
		String callback = request.getParameter("callback");
		
		String output = callback + "(" + result + ")";
		
//		response.setContentType("text/javascript");
		
		PrintWriter out = response.getWriter();
		out.print(output);

		out.flush();
		out.close();
		out = null;
    }  
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
		this.doGet(request, response);
	}
}
