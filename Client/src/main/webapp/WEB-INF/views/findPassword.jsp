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
        <title>重置密码-桌面化多媒体共享平台</title>
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
                	<!--Sign In-->
                    		<div id="sign-in-container" class="span5 container formcontainer visible" style="width:100%;">
                    
                        <div class="box corner-all" style="width:40%;margin:0 auto;">
                            <div class="box-header grd-teal color-white corner-top">
                                <span>重置密码</span>
                            </div>
                            <div class="box-body bg-white">
                                <form id="sign-in" method="post" action="user/findPassword/${userId}" />
                                	<input name="userIdDigests" value="${userIdDigests}" hidden="hidden">
                                    <div class="control-group">
                                        <label class="control-label">账号</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level" value="${userId}" data-validate="{required: true,minlength:5,maxlength:20, messages:{required:'请输入账号',minlength:'账号不少于5位',maxlength:'账号不超过20位'}}" name="username" id="login_username" autocomplete="off" readonly="readonly" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">新密码</label>
                                        <div class="controls">
                                            <input type="password" class="input-block-level" data-validate="{required: true, minlength: 7,maxlength:20, messages:{required:'请输入密码', minlength:'请输入至少7位的密码',maxlength:'请输入最多20位的密码'}}" name="password" id="login_password" autocomplete="off" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label">重复新密码</label>
                                        <div class="controls">
                                            <input type="password" class="input-block-level" data-validate="{required: true, equalTo: '#login_password', messages:{required:'请再次输入密码', equalTo: '两次密码输入不一致'}}" name="rpassword" id="login_rpassword" autocomplete="off" />
                                        </div>
                                    </div>
									<div class="control-group">
                                        <label class="control-label">验证码</label>
                                        <div class="controls">
                                            <input type="text" class="input-block-level captcha" data-validate="{required: true, messages:{required:'请输入验证码'}}" name="captcha" id="login_captcha" autocomplete="off" />
											<img id="captchaImg1" class="captchaImg" src="">
										</div>
                                    </div>
									
                                    <div class="form-actions">
                                        <input type="submit" class="btn btn-block btn-large btn-primary" value="重置密码" />
                                    </div>
									<div class="toolbar clearfix">
											<div>
												<a href="user/login" class="link" data-toggle="modal">
													登录
												</a>
											</div>
									</div>
                                </form>
                            </div>
                        </div>
                    </div><!--/Sign In-->
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
                $("#captchaImg1").click();
            });
            
			function show_box(id) {
			 $('.span5.container.visible').removeClass('visible');
			 $('#'+id).addClass('visible');
			 $('#'+id).find('.captchaImg').click();
			}
        </script>
    </body>
</html>

