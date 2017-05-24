<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
</head>
<body>

<button id="myBtn">一键上网</button>
<script type="text/javascript">

function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
} 

function loginRedirect()
{
	var gw_address = getQueryString("gw_address");
	var gw_port = getQueryString("gw_port");
	//var url = getQueryString("url");
	var token = new Date().getMilliseconds();

	var redirectUrl="http://"+gw_address+":"+gw_port+"/wifidog/auth?"+"token="+token;
	window.location.href=redirectUrl;
}


document.getElementById("myBtn").onclick=function(){loginRedirect()};


</script>


</body>
</html> 