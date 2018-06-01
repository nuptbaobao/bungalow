#!/bin/bash   

# author       chengzhu
# date         2018-05-31
# description  transport jar to linux service

start(){ 
 now=`date "+%Y%m%d%H%M%S"` 
 exec java -Xms128m -Xmx1024m -jar /home/prs3000/bungalow/bungalow.jar 5 >"$now"_start.log & 
 #java -Xms128m -Xmx2048m -jar cmpp.jar 5 > log.log & 
 #tail -f result.log 
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
 


