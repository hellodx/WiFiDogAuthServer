package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class PortalServlet extends HttpServlet{
private static final long serialVersionUID = 4L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
		request.setCharacterEncoding("utf-8");
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", "login success");
		
		String result = jsonObj.toString();
		String callback = request.getParameter("callback");
		String output = callback + "(" + result + ")";
		
		
		response.setStatus(200);
		response.getOutputStream().write(output.getBytes());
		
		//response.sendError(407, "Need authentication!!!" );
    }  
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
		this.doGet(request, response);
	}
}
