#!/bin/sh
if [ `ps -ef |grep cn.com.flaginfo.action.SendTaskSyncAction |grep $1 |grep -v grep |wc -l` == 0 ]; then
(cd `dirname $0`;
for jar in ./lib/*.jar; do
  classpath="$classpath:$jar"
done
classpath="$classpath:./bin"
CLASSPATH="$classpath"
export CLASSPATH
/opt/java/bin/java cn.com.flaginfo.action.SendTaskSyncAction )
else
echo "syncAction operation is running"
fi