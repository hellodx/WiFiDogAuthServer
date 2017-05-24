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
    
	<script type="text/javascript">
		var loadIframe = null;
		var noResponse = null;
		var callUpTimestamp = 0;
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
		
		function putNoResponse(ev){
			 clearTimeout(noResponse);
		}	
		
		 function errorJump()
		 {
			 var now = new Date().getTime();
			 if((now - callUpTimestamp) > 4*1000){
				 return;
			 }
			 alert('该浏览器不支持自动跳转微信请手动打开微信\n如果已跳转请忽略此提示');
		 }
		 
		 myHandler = function(error) {
			 errorJump();
		 };
		 
		 function createIframe(){
			 var iframe = document.createElement("iframe");
		     iframe.style.cssText = "display:none;width:0px;height:0px;";
		     document.body.appendChild(iframe);
		     loadIframe = iframe;
		 }
		//注册回调函数
		function jsonpCallback(result){  
			if(result && result.success){
			    alert('WeChat will call up : ' + result.success + '  data:' + result.data);			    
			    var ua=navigator.userAgent;              
				if (ua.indexOf("iPhone") != -1 ||ua.indexOf("iPod")!=-1||ua.indexOf("iPad") != -1) {   //iPhone             
					document.location = result.data;
				}else{			
				    createIframe();
				    callUpTimestamp = new Date().getTime();
				    loadIframe.src=result.data;
					noResponse = setTimeout(function(){
						errorJump();
			      	},3000);
				}			    
			}else if(result && !result.success){
				alert(result.data);
			}
		}
		function Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId, authUrl, mac, ssid, bssid){
			//将回调函数名称带到服务器端
			var url = "https://wifi.weixin.qq.com/operator/callWechatBrowser.xhtml?appId=" + appId 
								+ "&extend=" + extend 
								+ "&timestamp=" + timestamp 
								+ "&sign=" + sign;	
			
			//如果sign后面的参数有值，则是新3.1发起的流程
			if(authUrl && shopId){
				url = "https://wifi.weixin.qq.com/operator/callWechat.xhtml?appId=" + appId 
								+ "&extend=" + extend 
								+ "&timestamp=" + timestamp 
								+ "&sign=" + sign
								+ "&shopId=" + shopId
								+ "&authUrl=" + encodeURIComponent(authUrl)
								+ "&mac=" + mac
								+ "&ssid=" + ssid
								+ "&bssid=" + bssid;
				
			}			
			
			//通过dom操作创建script节点实现异步请求  
			var script = document.createElement('script');  
			script.setAttribute('src', url);  
			document.getElementsByTagName('head')[0].appendChild(script);
		}
	</script>
	
	
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
        <p class="mod-simple-follow-page__attention-txt">欢迎使用微信连Wi-Fi</p>
        <a class="mod-simple-follow-page__attention-btn" onclick="callWechatBrowser()">一键打开微信连Wi-Fi</a>
        <p id="res" class="mod-simple-follow-page__attention-txt"></p>
    </div>
</div>
</body>

<script src="${pageContext.request.contextPath}/static/js/md5.js"></script>  
<script src="${pageContext.request.contextPath}/static/js/md5.min.js"></script>  

<script type="text/javascript">

	function callWechatBrowser(){
		/* 微信连Wi-Fi协议3.1供运营商portal呼起微信浏览器使用
		----开发认证流程所需参数----
			ssid(非必须)
			shopId
			appId
			secretKey
		----复用demo代码说明----
			若认证Portal页直接使用此Demo源代码，请注意填写代码中的以下参数（由您的网络环境动态获取）：
			extend
			timestamp
			authUrl
			mac
			bssid（非必须）
			sign
			其中sign签名请在后台完成，例如：
			var toSign = appId + extend + timestamp + shopId + authUrl + mac + ssid + bssid + secretKey;
			var sign= md5(toSign);
		----参考文档----
			https://mp.weixin.qq.com/advanced/wiki?t=home/index&id=mp1444894086&token=&lang=zh_CN
		*/
		var appId	=	"wx46d30491a5f88ec5";
		var extend = getQueryString("mac");	//自定义参数，最终传给认证服务器认证URL
		var timestamp = new Date().getTime();
		var shopId	=	"4015174";
		var authUrl	=	getAuthUrl("1");//TODO
		var mac = getQueryString("mac"); //TODO 移动设备mac地址
		var ssid =	"OpenWrt";
		var bssid =	"ff:ff:ff:ff:ff:ff";//TODO AP设备Mac地址
		var secretKey	=	"404d11961cc53f02f9f044997119da40";
		var sign = md5(appId + extend + timestamp + shopId + authUrl + mac + ssid + bssid + secretKey);

		Wechat_GotoRedirect(appId, extend, timestamp, sign, shopId, authUrl, mac, ssid, bssid);
		//Wechat_GotoRedirect('wxd86056fe58061437', 'demoNew', timestamp, sign, '7743860', 'http://wifi.weixin.qq.com/assistant/wifigw/auth.xhtml?httpCode=200', 'aa:aa:aa:aa:aa:aa', '2099', 'ff:ff:ff:ff:ff:ff');

	}
</script>


<script type="text/javascript">
	document.addEventListener('visibilitychange', putNoResponse, false);
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