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
        <title>登录系统-桌面化多媒体共享平台</title>
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
                	<c:if test="${shiroLoginFailure!= null}">
                		<div class="alert alert-error input-medium controls errorMsgDiv">
							<button class="close" data-dismiss="alert">×</button>错误提示:${shiroLoginFailure}
						</div>
                	</c:if>
                	<c:choose>
                		<c:when test="${tab==null||tab==1}">
                			<!--Sign In-->
                    		<div id="sign-in-container" class="span5 container formcontainer visible" style="width:100%;">
                		</c:when>
                		<c:otherwise>
                			<!--Sign In-->
                    		<div id="sign-in-container" class="span5 container formcontainer" style="width:100%;">
                		</c:otherwise>
                	</c:choose>
                    
                        <div class="box corner-all">
                            <div class="box-header grd-teal color-white corner-top">
                                <span>登录</span>
                            </div>
                            <div class="box-body bg-white">
                                <form id="sign-in" method="post" action="user/login" />
                                    <div class="control-group">
                                        <label class="control-label">账号</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level" value="${username}" data-validate="{required: true,minlength:5,maxlength:20, messages:{required:'请输入账号',minlength:'账号不少于5位',maxlength:'账号不超过20位'}}" name="username" id="login_username" autocomplete="off" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">密码</label>
                                        <div class="controls">
                                            <input type="password" class="input-block-level" data-validate="{required: true, minlength: 7,maxlength:20, messages:{required:'请输入密码', minlength:'请输入至少7位的密码',maxlength:'请输入最多20位的密码'}}" name="password" id="login_password" autocomplete="off" />
                                        </div>
                                    </div>
									<div class="control-group">
                                        <label class="control-label">验证码</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level captcha" data-validate="{required: true, messages:{required:'请输入验证码'}}" name="captcha" id="login_captcha" autocomplete="off" />
											<img id="captchaImg1" class="captchaImg" src="">
										</div>
                                    </div>
                                    <div class="control-group">
                                        <label class="checkbox">
                                            <input type="checkbox" data-form="uniform" name="rememberMe" id="remember_me_yes" value="yes" /> 记住我
                                        </label>
                                    </div>
									
                                    <div class="form-actions">
                                        <input type="submit" class="btn btn-block btn-large btn-primary" value="登录" />
                                       <!-- <p class="recover-account">找回 <a href="#modal-recover" class="link" data-toggle="modal">用户名或密码</a></p>-->
                                    </div>
									<div class="toolbar clearfix">
											<div>
												<a href="#modal-recover" class="link" data-toggle="modal">
													<i class="icon-arrow-left"></i>
													找回密码
												</a>
											</div>
											<div>
												<a href="#" onclick="show_box('sign-up-container'); return false;" class="user-signup-link">
													注册账号
													<i class="icon-arrow-right"></i>
												</a>
											</div>
										</div>
                                </form>
                            </div>
                        </div>
                    </div><!--/Sign In-->
                    <c:choose>
                		<c:when test="${tab==null||tab==1}">
                			 <!--Sign Up-->
                    		<div id="sign-up-container" class="span5 container formcontainer" style="width:100%;margin-left: 0;">
                		</c:when>
                		<c:otherwise>
                			 <!--Sign Up-->
                    		<div id="sign-up-container" class="span5 container formcontainer visible" style="width:100%;margin-left: 0;">
                		</c:otherwise>
                	</c:choose>
                        <div class="box corner-all">
                            <div class="box-header grd-green color-white corner-top">
                                <span>注册</span>
                            </div>
                            <div class="box-body bg-white">
                                <form id="sign-up" method="post" action="user/register"/>
                                    <div class="control-group">
                                        <label class="control-label">账号</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level" data-validate="{required: true,minlength:5,maxlength:20, messages:{required:'请输入账号',minlength:'账号不少于5位',maxlength:'账号不超过20位'}}" value="${user.id}" name="id" id="username" autocomplete="off" />
                                        </div>
                                    </div>
									<div class="control-group">
                                        <label class="control-label">姓名</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level" data-validate="{required: true,minlength:2,maxlength:10, messages:{required:'请输入姓名',minlength:'姓名不少于2位',maxlength:'姓名不超过10位'}}" value="${user.name}" name="name" id="username" autocomplete="off" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">邮箱</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level" data-validate="{required: true, email:true, messages:{required:'请输入邮箱', email:'请输入有效的邮箱地址'}}" name="email" id="email" autocomplete="off" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">密码</label>
                                        <div class="controls">
                                            <input type="password" class="input-block-level" data-validate="{required: true, minlength: 7,maxlength:20, messages:{required:'请输入密码', minlength:'请输入至少7位的密码',maxlength:'请输入最多20位的密码'}}" name="password" id="password" autocomplete="off" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">重复密码</label>
                                        <div class="controls">
                                            <input type="password" class="input-block-level" data-validate="{required: true, equalTo: '#password', messages:{required:'请再次输入密码', equalTo: '两次密码输入不一致'}}" name="passwordAgain" id="password_again" autocomplete="off" />
                                        </div>
                                    </div>
                                     <div class="control-group">
                                        <label class="control-label">验证码</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level captcha" data-validate="{required: true, messages:{required:'请输入验证码'}}" name="captcha" autocomplete="off" />
											<img id="captchaImg2" class="captchaImg" src="">
										</div>
                                    </div>
									<!--
                                    <div class="control-group">
                                        <p class="term-of-use">Lorem ipsum dolor sit amet, natoque per at morbi at vestibulum leo, velit non, curabitur ac est. <a href="#">terms of use</a> and <a href="#">privacy policy</a>.</p>
                                    </div>-->
                                    <div class="form-actions">
                                        <input type="submit" class="btn btn-block btn-large btn-success" value="注册" />
                                    </div>
									<div class="toolbar center">
											<a href="#" onclick="show_box('sign-in-container'); return false;" class="user-signup-link">
													<i class="icon-arrow-left"></i>
													已经有账号了?
											</a>
										</div>
                                </form>
                            </div>
                        </div>
                    </div><!--/Sign Up-->
                </div><!-- /row -->
            </div><!-- /container -->
            
            <!-- modal recover -->
            <div id="modal-recover" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modal-recoverLabel" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h3 id="modal-recoverLabel">重置密码<small>注册邮箱</small></h3>
                </div>
                <div class="modal-body">
                    <form id="form-recover" method="post" action="user/findPassword"/>
                        <div class="control-group">
                            <div class="controls">
                                <input type="text" data-validate="{required: true, email:true, messages:{required:'请输入您的邮箱', email:'请输入有效的邮箱'}}" name="email" />
                                <p class="help-block helper-font">请输入您的邮箱,系统将重置密码的邮件发送到您的邮箱上,请注意查收.</p>
                            </div>
                        </div>
                        <div class="control-group">
                                        <label class="control-label">验证码</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level captcha" data-validate="{required: true, messages:{required:'请输入验证码'}}" name="captcha" autocomplete="off" />
											<img id="captchaImg3" class="captchaImg" src="">
										</div>
                                    </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                    <input type="submit" form="form-recover" class="btn btn-primary" value="找回密码" />
                </div>
            </div><!-- /modal recover-->
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
		
        <!-- javascript
        ================================================== -->
        <script src="resources/resources/bootstrap/js/jquery.js"></script>
        <script src="resources/resources/bootstrap/js/bootstrap.js"></script>
        <script src="resources/resources/bootstrap/js/jquery.uniform.js"></script>      
        <script src="resources/resources/bootstrap/js/jquery.metadata.js"></script>
        <script src="resources/resources/bootstrap/js/jquery.validate.js"></script>
        
        <script type="text/javascript">
            $(document).ready(function() {
                // try your js
                
                // uniform
                $('[data-form=uniform]').uniform();
                
                // validate
                $('#sign-in').validate();
                $('#sign-up').validate();
                $('#form-recover').validate();
                $('#modal-recover').on('show.bs.modal', function () {
                	  $("#captchaImg3").click();
                });
                $('#modal-recover').on('hidden.bs.modal', function () {
                	$("#captchaImg1").click();
                });
                $('.captchaImg').click(function () {//生成验证码  
                	$(this).hide().attr('src', 'captchaImage.djpg?t=' + Math.floor(Math.random()*100) ).fadeIn();
                });
                <c:choose>
	    			<c:when test="${tab==null||tab==1}">
	    				$("#captchaImg1").click();
	    			</c:when>
	    			<c:otherwise>
	    				$("#captchaImg2").click();
	    			</c:otherwise>
	    		</c:choose>
            });
            
			function show_box(id) {
			 $('.span5.container.visible').removeClass('visible');
			 $('#'+id).addClass('visible');
			 $('#'+id).find('.captchaImg').click();
			}
        </script>
    </body>
</html>

