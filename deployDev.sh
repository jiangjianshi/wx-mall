#!/usr/bin/env bash
echo "----------------------------------------------------------------------------------------------------------------------"
echo "Start pull new code from repository......"
cd /data/www/hzf_platform_manager
git clone -b dev git@github.com:huifenqi/hzf-platform-manager.git manager/
cd manager

echo "----------------------------------------------------------------------------------------------------------------------"


echo "----------------------------------------------------------------------------------------------------------------------"
curl -X POST localhost:8089/shutdown
echo "----------------------------------------------------------------------------------------------------------------------"


echo "------------------------------------------------------------------------------------------------------------------------"
echo "Start build project......"
mvn clean package -Dmaven.test.skip=true
cd target
cp hzf_platform_manager-1.0.jar ../..
cd ../..
rm -rf manager
nohup java -jar *.jar & tail -f nohup.out
echo "Start success......"