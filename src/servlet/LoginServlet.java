package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.MySQLUtil;


public class LoginServlet extends HttpServlet{
	private static final long serialVersionUID = 2L;
	
	public void service(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("expires", -1);
		
		request.setCharacterEncoding("utf-8");
		
		String authMethod = MySQLUtil.QueryAuthMethod();
		switch(authMethod){
		//portal认证
		case "1":
			request.getRequestDispatcher("/WEB-INF/pages/weixin_portal.jsp").forward(request, response);
			break;
		//公众号认证
		case "2":
			request.getRequestDispatcher("/WEB-INF/pages/public_portal.jsp").forward(request, response);
			break;
		default:
			String respStr1 = "Can not get method code" + "code=" + authMethod;
			response.getOutputStream().write(respStr1.getBytes());
			break;
		}
    }  
	
	public LoginServlet() {
		super();
	}
}
