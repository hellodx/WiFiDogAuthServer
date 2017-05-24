package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.MySQLUtil;

public class ReLoginServlet extends HttpServlet{
private static final long serialVersionUID = 2L;
	
	public void service(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
		
		request.setCharacterEncoding("utf-8");
		String authMethod = MySQLUtil.QueryAuthMethod();
		
		if(authMethod.equals("2")){
			String wxopenid = (String) request.getParameter("wxopenid");
			String redirectUrl="http://192.168.1.1:2060/wifidog/auth?"+"token="+wxopenid;
			response.sendRedirect(redirectUrl);
		}else{
			request.getRequestDispatcher("/WEB-INF/pages/public_ban.jsp").forward(request, response);
		}
		
//		String gw_address = (String) request.getParameter("gw_address");//路由器地址
//		String gw_port = (String) request.getParameter("gw_port");//路由器端口
//		String url = (String) request.getParameter("url");//成功后的重定向url
//		String gw_id = (String) request.getParameter("gw_id");
//		String dev_id = (String) request.getParameter("dev_id");
//		String mac=(String) request.getParameter("mac");//客户端mac地址
		
		//拼接重定向url
//		String token=System.currentTimeMillis()+"";
//		String redirectUrl="http://"+gw_address+":"+gw_port+"/auth?"+"token="+token+"&url="+url;
//		response.sendRedirect(redirectUrl);
		//request.setAttribute("param", "ok");
		//request.getRequestDispatcher("/WEB-INF/jsp/portal.jsp?param1=ok1").forward(request, response);
//		response.setContentType("text/html; charset=UTF-8");
//		response.sendRedirect(request.getContextPath()+"/WebContent/WEB-INF/jsp/portal.jsp?param1=ok1");
    }  
	
	public ReLoginServlet() {
		super();
	}
}
