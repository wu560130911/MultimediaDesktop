#! /bin/sh
JAVA_HOME=/home/tools/jdk1.8.0_25
MAVEN_HOME=/home/tools/maven
PATH=.:${JAVA_HOME}/bin:$MAVEN_HOME/bin:$PATH
#LANG=zh_CN.gbk
export JAVA_HOME MAVEN_HOME PATH

PROJECT_DIR=/home/project/trunk
DEPLOY_DIR=/home/tools/deploy/
HOST_IPADDRESS=`ifconfig eth2 | grep 'inet 地址:' | cut -d: -f2 | awk '{ print $1}'`
LOG_DIR=/home/data/log/Server/${HOST_IPADDRESS}
SVNUP_log=${LOG_DIR}/svnup.log
RSYNC_log=${LOG_DIR}/rsync.log
DEPLOY_LOG=${LOG_DIR}/deploy.log
MAVEN_LOG=${LOG_DIR}/maven.log
FILE_EXCLUDE="--exclude .svn"
SERVER_DIR=${PROJECT_DIR}/Server/target/MultimediaDesktop-Server/
HOST_IPADDRESS=`ifconfig eth2 | grep 'inet 地址:' | cut -d: -f2 | awk '{ print $1}'`
srestart() {
  if [ -d "${DEPLOY_DIR}/bin" ];then
    /bin/sh ${DEPLOY_DIR}/bin/restart.sh
  else
    echo "请先部署下项目，然后再执行重启操作."
  fi
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
  echo -n "发布Server"
  {
    echo '-------------------------------------'
	date "+%Y-%m-%d %H:%M:%S"
        time=$(date "+%Y-%m-%d %H:%M:%S")
	/usr/bin/rsync -rtlpgoc --delete --progress \
	$FILE_EXCLUDE \
	${SERVER_DIR} ${DEPLOY_DIR}
  }>>${DEPLOY_LOG}
  sed -i "s#^log4j.appender.dblog.machineId=.*#log4j.appender.dblog.machineId=${HOST_IPADDRESS}@Server#g" ${DEPLOY_DIR}conf/log4j.properties  
  sed -i "s#^log4j.appender.RollingFile.File=.*#log4j.appender.RollingFile.File=${LOG_DIR}/${HOST_IPADDRESS}@Server.log#g" ${DEPLOY_DIR}conf/log4j.properties
  sed -i "s#^dubbo.protocol.host=.*#dubbo.protocol.host=${HOST_IPADDRESS}#g" ${DEPLOY_DIR}conf/dubbo.properties
  tail -10000 $DEPLOY_LOG | sed -n "/$time/,\$p"
}

sstart(){
   if [ -d "${DEPLOY_DIR}/bin" ];then
    /bin/sh ${DEPLOY_DIR}/bin/start.sh
  else
    echo "请先部署下项目，然后再执行启动操作."
  fi
}

sstop(){
  if [ -d "${DEPLOY_DIR}/bin" ];then
    /bin/sh ${DEPLOY_DIR}/bin/stop.sh
  else
    echo "请先部署下项目，然后再执行关闭操作."
  fi
}

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
   Q|q) exit 0
       ;;
  esac
echo "hit any key to return!"
read DUMMY
done
###########   Main Menu end ###############
 
