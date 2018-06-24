#!/bin/bash

# author       chengzhu
# date         2018-05-31
# description  transport jar to linux service

JAVA_HOME=/home/prs3000/jdk1.7.0_80
export JAVA_HOME=$JAVA_HOME
export PATH=$PATH:$JAVA_HOME/bin:$JAVA_HOME/jre/bin

start(){
 now=`date "+%Y%m%d%H%M%S"`
 sh -prs3000 -c "prs3000!"
 exec java -Xms128m -Xmx1024m -jar /home/prs3000/bungalow/bungalow.jar --spring.config.location=/home/prs3000/bungalow/application.properties
}

stop(){
 ps -ef|grep bungalow|grep -v grep|awk '{print $2}'|while read pid
 do
    kill -9 $pid
 done
}

case "$1" in
start)
start
;;
stop)
stop
;;
restart)
stop
start
;;
*)
printf 'Usage: %s {start|stop|restart}\n' "$prog"
exit 1
;;
esac



