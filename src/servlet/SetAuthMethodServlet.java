package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import util.MySQLUtil;

public class SetAuthMethodServlet extends HttpServlet{
private static final long serialVersionUID = 3L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
		request.setCharacterEncoding("utf-8");
		
		String authMethod = request.getParameter("authMethod");
		boolean r = MySQLUtil.SetAuthMethod(authMethod);
		String res;
		
		if(r) res = "OK";
		else res = "Fail";
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", res);
		jsonObj.put("value", authMethod);
		
		String result = jsonObj.toString();
		String callback = request.getParameter("callback");
		String output = callback + "(" + result + ")";
		
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
