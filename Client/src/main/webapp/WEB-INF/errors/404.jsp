<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <meta charset="utf-8" />
        <title>页面不存在-404</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta name="description" content="" />
        <meta name="author" content="stilearning" />

        <!-- google font -->
        <link href="http://fonts.googleapis.com/css?family=Aclonica:regular" rel="stylesheet" type="text/css" />
        
        <!-- styles -->
        <link href="resources/resources/bootstrap/css/bootstrap.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/stilearn.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/stilearn-responsive.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/stilearn-helper.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/stilearn-icon.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/font-awesome.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/animate.css" rel="stylesheet" />

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /></head>

    <body>
         <!-- section header -->
        <header class="header" data-spy="affix" data-offset-top="0">
            <!--nav bar helper-->
            <div class="navbar-helper">
                <div class="row-fluid">
                    <!--panel site-name-->
                    <div class="span4">
                        <div class="panel-sitename">
                            <h2><a href="#"><span class="color-teal">桌面化</span><span class="color-purple">多媒体共享</span><span class="color-win8">平台</span></a></h2>
                        </div>
                    </div>
                    <!--/panel name-->
                </div>
            </div><!--/nav bar helper-->
        </header>
        
        <!-- section content -->
        <section class="section">
            <div class="container">
                <div class="error-page">
                    <h1 class="error-code color-red">Error 404</h1>
                    <p class="error-description muted">对不起，您访问的页面不存在。</p>
                    <p class="error-description muted">如果有任何问题，请联系网站管理员。</p>
                    <a href="./" class="btn btn-small btn-primary"><i class="icofont-arrow-left"></i> 返回主页</a>
                    <a href="user/login" class="btn btn-small btn-success">去登录 <i class="icofont-arrow-right"></i></a>
                </div>
            </div>
        </section>

        <!-- section header -->
        <header class="footer" data-spy="affix" data-offset-top="0">
            <!--nav bar helper-->
            <div class="navbar-helper">
                <div class="row-fluid">
                    <!--panel site-name-->
                    <div class="copyright center">
                            ©2015 WMS 版权所有
                    </div>
                    <!--/panel name-->
                </div>
            </div><!--/nav bar helper-->
        </header>
        
    </body>
</html>
