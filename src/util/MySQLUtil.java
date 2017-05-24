package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONStringer;


public class MySQLUtil {
	//数据库主机名
	public static final String host_name = "w.rdc.sae.sina.com.cn";
	//数据库名
	public static final String db_name = "app_authserver";
	//端口号
	public static final String port = "3307";
	//用户名 SAE Access Key
	public static final String username = "xzxw112yzl";
	//密码 SAE Secret Key
	public static final String password = "hwh0z3y3jzy0l3z2k5y150xxj302123y3j1ixi2x";
	
	/*
	 * 获取MySQL数据库连接
	 * 
	 * @return Connection
	 * */
	private Connection getConn(){
		Connection conn = null;
		
		//JDBC　URL
		String url = String.format("jdbc:mysql://%s:%s/%s",host_name,port,db_name);
		
		try{
			//加载MySQL JDBC驱动
			Class.forName("com.mysql.jdbc.Driver");
			//获取数据库连接
			conn = DriverManager.getConnection(url,username,password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	/*
	 * 插入数据到表中
	 * 
	 * @param table_name 表名
	 * @param values 插入数据(多项数据，顺序与数据库数据顺序一致)
	 * 
	 * @return result(boolean)
	 * */
	public static boolean InsertInfo(String table_name,String ...values){
		boolean result = false;
		String sql_value = "values(";
		for(int i =0;i<values.length-1;i++){
			sql_value += "?,";
		}
		sql_value += "?)";
		String sql = String.format("insert into %s " + sql_value, table_name);
		
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i =0;i<values.length;i++){
				ps.setString(i+1, values[i]);
			}
			int state = ps.executeUpdate();
			if(state == 0) result = false;
			else result = true;
			
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 判断表中是否已经有了该mac记录
	 * 
	 * @param table_name
	 * @param mac
	 * 
	 * @return result
	 * */
	public static boolean QueryByMac(String table_name,String mac){
		boolean result = false;
		String sql = String.format("select * from %s where mac=?",table_name);
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mac);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result = true;
			}
			
			rs.close();
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 获取临时账号的上线时间
	 * @param mac
	 * 
	 * @return finLogin
	 * */
	public static String getTempTimestampByMac(String mac){
		String result = "";
		String sql = "select * from wifiauth where mac=?";
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, mac);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result = rs.getString("tempTimestamp");
			}
			
			rs.close();
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	/*
	 * 判断表中是否已经有了该mac记录
	 * 
	 * @param table_name
	 * @param wxOpenId
	 * 
	 * @return result
	 * */
	public static boolean QueryByOpenId(String table_name,String wxOpenId){
		boolean result = false;
		String sql = String.format("select * from %s where wxOpenId=?",table_name);
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, wxOpenId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result = true;
			}
			
			rs.close();
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	/*
	 * 改变状态
	 * 
	 * @param mac
	 * @param values
	 * 
	 * @return result
	 * */
	public static String ChangePropByMac(String mac,String ...values){
		//boolean result = false;
		String result = "";
		int num = values.length;
		String sql = "update wifiauth set ";
		for(int m=0;m<num/2;m+=1){
			sql += values[m*2] + "=?,";
		}
		sql = sql.substring(0, sql.length()-1);
		sql += " where mac=?";
		//String sql = String.format("update wifiauth set ip=?,state=? where mac=?",table_name);
		result += sql + "\n";
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i=0;i<num/2;i++){
				ps.setString(i+1, values[i*2+1]);
				result += (i+1) + ":" + values[i*2+1] + "\n";
			}
			ps.setString(num/2+1, mac);
			result += (num/2+1) + ":" + mac + "\n";
			int r = ps.executeUpdate();
//			if(r==0){
//				result = false;
//			}else{
//				result = true;
//			}
			result += "resCode:" + r;
			
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 标记查询过的记录
	 * 
	 * @return result
	 * */
	private static boolean ChangeNew(){
		boolean result = false;
		String sql = "update wifiauth set new=? where new=?";
		
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, "no");
			ps.setString(2, "yes");
			int i = ps.executeUpdate();
			if(i==0){
				result = false;
			}else{
				result = true;
			}
			
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 设置认证方式
	 * 
	 * @param authMethod
	 * 
	 * @return result
	 * */
	public static boolean SetAuthMethod(String authMethod){
		boolean result = false;
		String sql = "update wifiauth set status=? where mac=?";
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,authMethod);
			ps.setString(2,"admin");
			int i = ps.executeUpdate();
			if(i==0){
				result = false;
			}else{
				result = true;
			}
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 查询当前认证方式
	 * 
	 * @return result
	 * 1:portal认证	2:公众号认证
	 * */
	public static String QueryAuthMethod(){
		String result = "";
		String sql = "select * from wifiauth where mac=?";
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, "admin");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				result = rs.getString("status");
			}
			rs.close();
			ps.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/*
	 * 查询数据库
	 * 
	 * @param condition(1.all 查询所有会员	2.active 查询在线会员	3.new 查询新增会员)
	 * 
	 * @return result
	 * */
	
	public static String getUser(String condition){
		String result = null;
		String sql = null;
		boolean r = false;
		
		switch(condition){
		case "all":
			sql = "select * from wifiauth";
			break;
		case "active":
			sql = "select * from wifiauth where status = 'active'";
			break;
		case "new":
			//calender对象
//			Calendar c1 = Calendar.getInstance();
//			String year = Integer.toString(c1.get(Calendar.YEAR));
//			String month = Integer.toString(c1.get(Calendar.MONTH) + 1);
//			String day = Integer.toString(c1.get(Calendar.DATE));
//			String date = String.format("%s/%s/%s", year,month,day);
			//sql = "select * from wifiauth where regDate='RegDate'".replace("RegDate", date);
			sql = "select * from wifiauth where new='yes'";
			break;
		default:
			return null;	
		}
		
		JSONStringer stringer = new JSONStringer();
		
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			stringer.array();			
			while(rs.next()){
				r = true;
				String mac = rs.getString("mac");
				String wxOpenID =rs.getString("wxOpenId");
				String status = rs.getString("status");
				String ip = rs.getString("ip");
				String gwId = rs.getString("gwId");
				String regDate = rs.getString("regDate");
				String finLogin = rs.getString("finLogin");
				stringer.object().
					key("mac").value(mac).
					key("wxOpenID").value(wxOpenID).
					key("status").value(status).
					key("ip").value(ip).
					key("gwId").value(gwId).
					key("regDate").value(regDate).
					key("finLogin").value(finLogin).endObject();
			}
			stringer.endArray();
			
			rs.close();
			ps.close();
			conn.close();
		}catch(JSONException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(stringer != null){
			result = stringer.toString();
		}
		
		if(r&&condition.equals("new")){
			ChangeNew();
		}
		
		return result;
	}
	
	/*
	 * 统计数据 index初始化
	 * 
	 * @return jsonobj
	 * */
	public static JSONObject getIndexInitData(){
		int count = 0;
		JSONObject result = new JSONObject();
		String sql_active = "select * from wifiauth where status='active'";
		String sql_new = "select * from wifiauth where new='yes'";
		String sql_total = "select * from wifiauth where temp='no'";
		String sql_temp = "select * from wifiauth where status='temporary'";
		try{
			//获取连接
			Connection conn = new MySQLUtil().getConn();
			PreparedStatement ps1 = conn.prepareStatement(sql_active);
			PreparedStatement ps2 = conn.prepareStatement(sql_new);
			PreparedStatement ps3 = conn.prepareStatement(sql_total);
			PreparedStatement ps4 = conn.prepareStatement(sql_temp);
			ResultSet rs1 = ps1.executeQuery();
			while(rs1.next()){
				count++;
			}
			result.put("ActiveCount", count);
			count = 0;
			rs1.close();
			ps1.close();
			
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next()){
				count++;
			}
			result.put("NewCount", count);
			count = 0;
			rs2.close();
			ps2.close();
			
			ResultSet rs3 = ps3.executeQuery();
			while(rs3.next()){
				count++;
			}
			result.put("TotalCount", count);
			count = 0;
			rs3.close();
			ps3.close();
			
			ResultSet rs4 = ps4.executeQuery();
			while(rs4.next()){
				count++;
			}
			result.put("TempCount", count);
			count = 0;
			rs4.close();
			ps4.close();
			
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
}
