#! /bin/sh
JAVA_HOME=/home/tools/jdk1.8.0_25
MAVEN_HOME=/home/tools/maven
PATH=.:${JAVA_HOME}/bin:$MAVEN_HOME/bin:$PATH
#LANG=zh_CN.gbk
export JAVA_HOME MAVEN_HOME PATH

PROJECT_DIR=/home/project/trunk
DEPLOY_DIR=/home/tools/tomcat/
DEPLOY_PROJECT=${DEPLOY_DIR}/webapps/ROOT/
NGINX_STATICS=/home/tools/MultimediaDesktop-Client-STATICS/
HOST_IPADDRESS=`ifconfig eth2 | grep 'inet 地址:' | cut -d: -f2 | awk '{ print $1}'`
LOG_DIR=/home/data/log/Client/${HOST_IPADDRESS}
SVNUP_log=${LOG_DIR}/svnup.log
RSYNC_log=${LOG_DIR}/rsync.log
DEPLOY_LOG=${LOG_DIR}/deploy.log
MAVEN_LOG=${LOG_DIR}/maven.log
FILE_EXCLUDE="--exclude .svn"
CLIENT_DIR=${PROJECT_DIR}/Client/target/MultimediaDesktop-Client/
STATICS_DIR=${PROJECT_DIR}/Client/target/statics/
FILE_MEDIA_DIR=/home/data/files/

srestart() {
  /bin/sh ${DEPLOY_DIR}/bin/catalina.sh stop
  /bin/sh ${DEPLOY_DIR}/bin/catalina.sh start
}

svnup(){
  cd ${PROJECT_DIR}
  {
    echo '--------------------------------'
    date "+%Y-%m-%d %H:%M:%S"
    time=$(date "+%Y-%m-%d %H:%M:%S")
    svn update
  }>>$SVNUP_log
  tail -10000 $SVNUP_log | sed -n "/$time/,\$p"
}

mavenCompile(){
  echo -n "正在编译Maven项目,请稍等"
  {
    echo '-------------------------------------'
    date "+%Y-%m-%d %H:%M:%S"
    time=$(date "+%Y-%m-%d %H:%M:%S")
    cd ${PROJECT_DIR}
    mvn clean package -Dmaven.test.skip=true
  }>>$MAVEN_LOG
  tail -10000 $MAVEN_LOG | sed -n "/$time/,\$p"
}

deploy(){
  echo -n "发布Client"
  {
    echo '-------------------------------------'
	date "+%Y-%m-%d %H:%M:%S"
        time=$(date "+%Y-%m-%d %H:%M:%S")
	/usr/bin/rsync -rtlpgoc --delete --progress \
	$FILE_EXCLUDE \
	${CLIENT_DIR} ${DEPLOY_PROJECT}
  }>>${DEPLOY_LOG}
  sed -i "s#^log4j.appender.dblog.machineId=.*#log4j.appender.dblog.machineId=${HOST_IPADDRESS}@Client#g" ${DEPLOY_PROJECT}WEB-INF/classes/log4j.properties  
  sed -i "s#^log4j.appender.RollingFile.File=.*#log4j.appender.RollingFile.File=${LOG_DIR}/${HOST_IPADDRESS}@Client.log#g" ${DEPLOY_PROJECT}WEB-INF/classes/log4j.properties
  sed -i "s#^dubbo.protocol.host=.*#dubbo.protocol.host=${HOST_IPADDRESS}#g" ${DEPLOY_PROJECT}WEB-INF/classes/server.properties
  sed -i "s#^server.basePath=.*#server.basePath=${NGINX_STATICS}#g" ${DEPLOY_PROJECT}WEB-INF/classes/server.properties
  sed -i "s#^server.mediaBasePath=.*#server.mediaBasePath=${FILE_MEDIA_DIR}#g" ${DEPLOY_PROJECT}WEB-INF/classes/server.properties
  tail -10000 $DEPLOY_LOG | sed -n "/$time/,\$p"
}

deployStatics(){
  echo -n "发布Client静态资源"
  {
    echo '-------------------------------------'
	date "+%Y-%m-%d %H:%M:%S"
        time=$(date "+%Y-%m-%d %H:%M:%S")
	/usr/bin/rsync -rtlpgoc --delete --progress \
	$FILE_EXCLUDE \
	${STATICS_DIR} ${NGINX_STATICS}resources/
  }>>${DEPLOY_LOG}
  tail -10000 $DEPLOY_LOG | sed -n "/$time/,\$p"
}

sstart(){
   /bin/sh ${DEPLOY_DIR}/bin/catalina.sh start
}

sstop(){
  /bin/sh ${DEPLOY_DIR}/bin/catalina.sh stop
}

if [ -d "${LOG_DIR}" ];then
    echo ""
  else
    mkdir -p ${LOG_DIR}
  fi

###########   Main Menu ###############
mydate=`date +%Y-%m-%d`
myhost=`hostname -s`
user=`whoami`
while :
do
tput clear

cat << myday

             $DEPLOY_DIR   部署       v1.0
################################################################################
今天是:$mydate            你好:[ $user ]       主机:[ $myhost ]         
################################################################################
              1: svn up 更新项目
              --------------------------------
	      2: maven编译和打包项目
	      --------------------------------
              3: 发布服务文件
	      --------------------------------
	      4: 重启服务器
              --------------------------------
	      5: 启动服务
              --------------------------------
	      6: 一键发布服务器
	      --------------------------------
	      7: 关闭服务
              --------------------------------
	      8: 发布Nginx静态资源
              --------------------------------
              Q: Exit menu
################################################################################
myday
echo "Your choice [1,2,H,Q]>"
read choice
   case $choice in
   1) svnup
	;;
   2) mavenCompile
	;;
   3) deploy
	;;
   4) srestart
	;;
   5) sstart
	;;
   6) svnup && mavenCompile && deploy && srestart
	;;
   7) sstop
	;;
	8) deployStatics
	;;
   Q|q) exit 0
       ;;
  esac
echo "hit any key to return!"
read DUMMY
done
###########   Main Menu end ###############