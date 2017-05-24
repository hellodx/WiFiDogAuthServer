package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.CommonUtil;
import util.MySQLUtil;

public class AuthServlet extends HttpServlet{
private static final long serialVersionUID = 4L;
	
	//�����ʽ:http://auth_server/auth/?stage= &ip= mac= &token= &incoming= &outgoing=
	//stage=counters|login|logout���ֱ��ʾ������֤������֤�û�����ʱ��Ҫɾ�����û���
	public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException { 
		request.setCharacterEncoding("utf-8");
		String respStr = "";
		
		String stage = request.getParameter("stage");
		String mac = request.getParameter("mac");
		String ip = request.getParameter("ip");
		String gw_id = request.getParameter("gw_id");
		String token = request.getParameter("token");
		
		String time = CommonUtil.getExaTime();
		
		long long_timestamp = new Date().getTime();
		String timestamp = long_timestamp + "";
		
		
		//String result = "";
		//��ʱ�˺���֤
		if(token.equals("temp")){
			switch(stage){
			case "login":
				respStr = "Auth: 1";
				if(MySQLUtil.QueryByMac("wifiauth",mac)){
					//���û� ��¼
					//result = 
					MySQLUtil.ChangePropByMac(mac, "status","temporary","ip",ip,"gwId",gw_id,
							"finLogin",time,"tempTimestamp",timestamp);
				}else{
					//���û� 
					MySQLUtil.InsertInfo("wifiauth",mac,"-","temporary",ip,gw_id,"-",time,"yes",timestamp);
				}
				break;
			case "counters":
				String logTime = MySQLUtil.getTempTimestampByMac(mac);
				if(long_timestamp - Long.parseLong(logTime)<5*60*1000){
					//δ��ʱ
					respStr = "Auth: 1";
					MySQLUtil.ChangePropByMac(mac, "status","temporary","ip",ip,"gwId",gw_id,"finLogin",time);
				}else{
					//��ʱ
					respStr = "Auth: 0";
					MySQLUtil.ChangePropByMac(mac, "status","tempOff","ip","-","gwId","-","finLogin",time);
				}
				break;
			case "logout":
				respStr = "Auth: 0";
				MySQLUtil.ChangePropByMac(mac, "status","tempOff","ip","-","gwId","-","finLogin",time);
				break;
			}
		}else{
			//��������֤
			String authMethod = MySQLUtil.QueryAuthMethod();
			
			switch(authMethod){
			//portal��֤
			case "1":
				switch(stage){
				case "login":
					respStr = "Auth: 1";
					if(MySQLUtil.QueryByMac("wifiauth",mac)){
						//���û� ��¼
						//result = 
						MySQLUtil.ChangePropByMac(mac, "status","active","ip",ip,"gwId",gw_id,"finLogin",time,"temp","no");
					}else{
						//���û� ע��
						MySQLUtil.InsertInfo("wifiauth",mac,"-","active",ip,gw_id,time,time,"no","-");
					}
					break;
				case "counters": 
					respStr = "Auth: 1";
					//result = 
					MySQLUtil.ChangePropByMac(mac, "status","active","ip",ip,"gwId",gw_id,"finLogin",time);
					break;
				case "logout":
					respStr = "Auth: 0";
					//result = 
					MySQLUtil.ChangePropByMac(mac, "status","offline","ip","-","gwId","-","finLogin",time);
					break;
				}
				break;
				
			//���ں���֤
			case "2":
				switch(stage){
				case "login":
					respStr = "Auth: 1";
					if(MySQLUtil.QueryByMac("wifiauth",mac)){
						//���û� ��¼
						//result = 
						MySQLUtil.ChangePropByMac(mac,"wxOpenId",token, "status","active","ip",ip,"gwId",gw_id,
								"finLogin",time,"temp","no");
					}else{
						//���û� ע��
						MySQLUtil.InsertInfo("wifiauth",mac,token,"active",ip,gw_id,time,time,"no");
					}
					break;
				case "counters": 
					if(MySQLUtil.QueryByOpenId("wifiauth", token)){
						respStr = "Auth: 1";
						//result = 
						MySQLUtil.ChangePropByMac(mac, "status","active","ip",ip,"gwId",gw_id,"finLogin",time);
					}else{
						respStr = "Auth: 0";
						MySQLUtil.ChangePropByMac(mac, "status","offline","ip",ip,"gwId",gw_id,"finLogin",time);
//						result = MySQLUtil.ChangePropByMac(mac, "status","offline","ip","-","gwId","-","finLogin",time);
					}
					break;
				case "logout":
					respStr = "Auth: 0";
					//result = 
					MySQLUtil.ChangePropByMac(mac, "status","offline","ip","-","gwId","-","finLogin",time);
					break;
				}
				break;
			default:
				MySQLUtil.InsertInfo("Error",authMethod,stage,mac,token,respStr,time);
				break;
			}
		}
		
		
		response.getOutputStream().write(respStr.getBytes());
    } 
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {
		this.doGet(request, response);
	}
}
