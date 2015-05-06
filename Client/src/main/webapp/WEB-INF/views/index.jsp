<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+
					  "://"+request.getServerName()+":"+
					  request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
	<meta name="baidu-tc-cerfication" content="9214e97b57c88f45c46dc9611494b983" />
    <title>桌面化多媒体共享平台</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta name="keywords" content="桌面化多媒体共享平台,桌面化,视频网站系统,WEBOS">
	<meta name="description" content="桌面化多媒体共享平台,提供操作系统式的界面和操作，让你的体验性更强.">
	<script type="text/javascript" src="resources/desktop/Bootstrap.js"></script>
	<script type="text/javascript" src="resources/resources/extjs/ext-all-dev.js"></script>
	<script type="text/javascript">
	
		var basePath='<%=basePath%>';
		
		WMSDesktop.Bootstrap.loadCallback=function(){
			
		};
		WMSDesktop.Bootstrap.loadRequires([{
			id:'themecss',
			type:WMSDesktop.Bootstrap.resTypes.CSS,
			url:'resources/resources/extjs/resources/css/theme-neptune.css'
		},{
			id:'desktop-css',
			type:WMSDesktop.Bootstrap.resTypes.CSS,
			url:'resources/resources/css/desktop.css'
		},{
			id:'icon-css',
			type:WMSDesktop.Bootstrap.resTypes.CSS,
			url:'resources/resources/css/icon.css'
		},{
			id:'login-css',
			type:WMSDesktop.Bootstrap.resTypes.CSS,
			url:'resources/resources/css/login.css'
		},{
			id:'fonts-css',
			type:WMSDesktop.Bootstrap.resTypes.CSS,
			url:'resources/resources/font-awesome/css/font-awesome.min.css'
		}/* ,{
			type:WMSDesktop.Bootstrap.resTypes.JAVASCRIPT,
			url:'js/extjs/ext-all-dev.js'
		} */,{
			type:WMSDesktop.Bootstrap.resTypes.JAVASCRIPT,
			url:'resources/resources/extjs/ext-lang-zh_CN.js'
		},{
			type:WMSDesktop.Bootstrap.resTypes.JAVASCRIPT,
			url:'resources/desktop/desktop/util/common.js'
		},{
			type:WMSDesktop.Bootstrap.resTypes.JAVASCRIPT,
			url:'resources/desktop/loader.js'
		},{
			type:WMSDesktop.Bootstrap.resTypes.JAVASCRIPT,
			url:'resources/desktop/util/newstip.js'
		},{
			type:WMSDesktop.Bootstrap.resTypes.JAVASCRIPT,
			url:'http://pv.sohu.com/cityjson?ie=utf-8'
		}]);
	</script>
 <body>
 </body>
</html>
