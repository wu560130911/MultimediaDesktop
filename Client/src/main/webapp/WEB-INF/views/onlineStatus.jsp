<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cmn-Hans">
    <head>
        <meta charset="utf-8" />
        <title>系统提示</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
         <base href="<%=basePath%>"> 
        <!-- styles -->
        <link href="resources/resources/bootstrap/css/bootstrap.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/stilearn.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/stilearn-responsive.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/stilearn-helper.css" rel="stylesheet" />
        
        <link href="resources/resources/bootstrap/css/font-awesome.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/animate.css" rel="stylesheet" />
        <link href="resources/resources/bootstrap/css/uniform.default.css" rel="stylesheet" />  
        <link href="resources/css/onlineStatus.css" rel="stylesheet" />   
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
           		<div class="signin-form row-fluid">
            		<div class="errorMsgDiv user_login_panel">
        
        <c:choose>
        	<c:when test="${user_status==3}">
				<h3 class="loginmodalLabel text-center">用户状态<font color="red">登录失败</font></h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">您的账号已经在别处登录</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、如果您的密码被泄露,请马上修改密码.</p>
					<p> 2、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==2}">
        		<h3 class="loginmodalLabel text-center">用户状态<font color="red">登录失败</font></h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">您的账号被管理员强制下线</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、请不要攻击或者盗取别人隐私,严重者将被封号或者追究法律责任.</p>
					<p> 2、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==4}">
        		<h3 class="loginmodalLabel text-center">用户注册<font color="red">第二步</font></h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">恭喜${user.name}(${user.id})注册成功,系统已经发送验证邮件到${user.email}.</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、用户注册成功后,系统将会自动发送验证邮件到注册时填写的邮箱上.</p>
					<p> 2、只有验证成功后,账号才有效,系统才允许登录.</p>
					<p> 3、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==5}">
        		<h3 class="loginmodalLabel text-center">用户注册<font color="red">第三步</font></h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">恭喜您~邮件验证完毕.</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、用户注册成功后,系统将会自动发送验证邮件到注册时填写的邮箱上.</p>
					<p> 2、只有验证成功后,账号才有效,系统才允许登录.</p>
					<p> 3、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==6}">
        		<h3 class="loginmodalLabel text-center">用户注册<font color="red">第三步</font></h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">对不起,邮件验证失败,失败原因:${shiroLoginFailure}</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、用户注册成功后,系统将会自动发送验证邮件到注册时填写的邮箱上.</p>
					<p> 2、只有验证成功后,账号才有效,系统才允许登录.</p>
					<p> 3、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==7}">
        		<h3 class="loginmodalLabel text-center">重置密码</h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">重置密码成功，请查收您的邮箱。</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、用户注册成功后,系统将会自动发送验证邮件到注册时填写的邮箱上.</p>
					<p> 2、只有验证成功后,账号才有效,系统才允许登录.</p>
					<p> 3、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==8}">
        		<h3 class="loginmodalLabel text-center">重置密码</h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">对不起,重置密码失败,失败原因:${shiroLoginFailure}</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、用户注册成功后,系统将会自动发送验证邮件到注册时填写的邮箱上.</p>
					<p> 2、只有验证成功后,账号才有效,系统才允许登录.</p>
					<p> 3、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==9}">
        		<h3 class="loginmodalLabel text-center">更换绑定邮箱</h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">恭喜您，更换绑定邮箱成功.</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、用户注册成功后,系统将会自动发送验证邮件到注册时填写的邮箱上.</p>
					<p> 2、只有验证成功后,账号才有效,系统才允许登录.</p>
					<p> 3、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:when test="${user_status==10}">
        		<h3 class="loginmodalLabel text-center">警告</h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">请勿修改他人密码，否则管理员将封号处理.</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、用户注册成功后,系统将会自动发送验证邮件到注册时填写的邮箱上.</p>
					<p> 2、只有验证成功后,账号才有效,系统才允许登录.</p>
					<p> 3、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:when>
        	<c:otherwise>
        		<h3 class="loginmodalLabel text-center"><font color="red">出错啦...</font></h3>
				<div class="user_register_validation">
					<div class="form_name_div_left form_msg_div">
						<div class="form_msg">
							<h3 class="text-center">对不起,未定义错误</h3>
						</div>
					</div>
				</div>
				<div class="form_text_vr_div">
					<h4>提示:</h4>
					<p> 1、如有任何问题,请马上联系网站管理员.</p>
				</div>
				<div class="buttongroup">
					<a class="btn btn-block btn-large btn-primary" href="user/login">返回登录</a>
				</div>
        	</c:otherwise>
        </c:choose>
        			</div>
	      		</div><!-- /row -->
          	</div><!-- /container -->
       	</section>
        
		<!-- section footer -->
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

