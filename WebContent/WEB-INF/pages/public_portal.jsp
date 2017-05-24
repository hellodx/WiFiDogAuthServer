<%@ page contentType="text/html;charset=UTF-8" language="java" errorPage=""%>

<!DOCTYPE HTML>
<html>
<head lang="zh-CN">
<meta charset="UTF-8">
    <title>微信连Wi-Fi</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style-simple-follow.css"/>
</head>

<body class="mod-simple-follow">
<div class="mod-simple-follow-page">
    <div class="mod-simple-follow-page__banner">
        <img class="mod-simple-follow-page__banner-bg" src="${pageContext.request.contextPath}/static/images/background.jpg" alt=""/>
        <div class="mod-simple-follow-page__img-shadow"></div>
        <div class="mod-simple-follow-page__logo">
            <img class="mod-simple-follow-page__logo-img" src="${pageContext.request.contextPath}/static/images/t.weixin.logo.png" alt=""/>
            <p class="mod-simple-follow-page__logo-name"></p>
            <p class="mod-simple-follow-page__logo-welcome">欢迎您</p>
        </div>
    </div>
    <div class="mod-simple-follow-page__attention">
        <p class="mod-simple-follow-page__attention-txt">欢迎使用Wi-Fi</p>
        <p class="mod-simple-follow-page__attention-txt">请扫描或保存该二维码以关注公众号完成认证</p>
        <p><img src="${pageContext.request.contextPath}/static/images/QRcode.png"></p>
        <p id="res" class="mod-simple-follow-page__attention-txt"></p>
    </div>
</div>
</body>

<script type="text/javascript">
		//通过正则表达式匹配获取url中的参数
		function getQueryString(name) { 
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
			var r = window.location.search.substr(1).match(reg); 
			if (r != null) return unescape(r[2]); return null; 
		}
		
		function getAuthUrl(token){
			var gw_address = getQueryString("gw_address");
			var gw_port = getQueryString("gw_port");
			//var token = new Date().getMilliseconds();

			var redirectUrl="http://"+gw_address+":"+gw_port+"/wifidog/auth?"+"token="+token;
			
			return redirectUrl;
		}
</script>

<script src="${pageContext.request.contextPath}/static/js/jquery.min.js"></script>
<script>
$(document).ready(function(){
	var authUrl = getAuthUrl("temp");
	$.ajax({
		type:"GET",
		//url:"http://localhost:8080/Servlet_Test/Common",
		url:authUrl,
		crossDomain:true,
		//data:{key:"value",key:"value"},
		dataType:"jsonp",
		jsonp:"callback",
		async:true,
		success:function(data){
			var result = data.result;
			if(result=="login success"){
				$("#res").text("临时上网账号租用成功，时长：五分钟");
			}else{
				alert(result);
			}
			},
		error:function(err){
			//alert("err:" + err.message);
			}
		});
});
</script>


</html>