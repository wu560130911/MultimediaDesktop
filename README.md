# MultimediaDesktop(分布式多媒体共享平台)
本系统为个人毕业设计的系统，采用分布式系统构架，保证服务的稳定性。目前系统已经开发完毕，现开源和大家一起分享学习。系统采用Apache License Version 2.0开源协议。<br>

作者：吴梦升
邮箱：560130911@163.com

具体的系统设计和功能文档将在后期完善。。。。。。。。。。。。

## <a name="index"/>目录
* [系统项目结构](#h1)
* [系统构架](#h2)
* [系统采用的技术](#h3)
* [系统处理流程](#h4)
* [如何部署本系统](#h5)
* [系统功能](#h6)
* [系统功能截图](#h8)

<a name="h1"/>
## 系统项目结构
系统整个是一个Maven项目，包含三个子项目，分别是:Server-API、Server、Client。其中Server-API主要包含一些基本工具类和服务接口等，Server主要是业务逻辑和数据持久化，Client主要是WEB服务，处理web请求。

<a name="h2"/>
## 系统构架
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/%E7%B3%BB%E7%BB%9F%E6%9E%84%E6%9E%B6%E5%9B%BE.png)
<br>
系统构架采用目前中小型公司常用的构架方式。

<a name="h3"/>
## 系统采用的技术
系统采用的技术主要有：Dubbo,memcache,mail,spring,fastjson,hibernate,druid,freemarker,jpa,spring data jpa,servlet3.1,shiro,fastjson,spring mvc,extjs4等<br>
## 系统依赖的工具
Zookeeper，MemCache，Mysql（默认，可选择其他的），Tomcat（默认，可以选择其他的），Maven，JDK 1.7+，dubbo admin，dubbo monitor
<br>
<a name="h4"/>
## 系统处理流程
系统最外层由nginx进行处理，然后nginx对请求进行转发（静态文件和媒体文件特殊处理），如图所示。<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/Nginx%E8%BD%AC%E5%8F%91.png)<br>
当nginx分发请求到web集群后，web端的处理流程如图所示。<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/web%E5%A4%84%E7%90%86%E6%B5%81%E7%A8%8B.png)<br>
<a name="h5"/>
## 如何部署本系统
在deploy/sql里包含本系统的sql文件，导入数据库即可。
项目对打包进行了处理，服务端将打包成文件夹，包含执行脚本。消费端将打包成文件夹（war文件）和静态资源文件（经过压缩）。
请参见本人博文，[分布式桌面多媒体共享平台](http://my.oschina.net/WMSstudio/blog/408026)。<br>
<a name="h6"/>
## 系统功能
系统采用shiro作为安全框架，目前只有三个角色的用户，使用Memcache作为分布式会话缓存（不依赖容器），将用户权限等信息存放在缓存中，如图所示。<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/%E7%B3%BB%E7%BB%9F%E5%8A%9F%E8%83%BD%E5%9B%BE.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/Handler.png)<br>
<a name="h8"/>
## 系统功能截图
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/1.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/2.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/3.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/5.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/6.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/7.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/8.png)<br>
![](https://github.com/wu560130911/MultimediaDesktop/blob/master/deploy/pictures/9.png)<br>




