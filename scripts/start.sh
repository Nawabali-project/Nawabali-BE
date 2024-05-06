#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ubuntu/app/step3
PROJECT_NAME=ZeroDowntimeDeployment
DEPLOY_LOG="$REPOSITORY/deploy.log"

TIME_NOW=$(date +%c)

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."

# 쉬고 있던 프로필로 jar파일을 백그라운드 실행
nohup java -Xms16m -Xmx2048m -jar \
    -Dspring.config.location="classpath:/application.properties, /home/ubuntu/app/step3/zip/application-$IDLE_PROFILE.properties" \
    -Dspring.profiles.active=$IDLE_PROFILE \
    -Duser.timezone=Asia/Seoul \
    $JAR_NAME > $REPOSITORY/application.log 2>&1 &

# Deploy 로그
echo "$TIME_NOW > $JAR_NAME 파일 실행" >> $DEPLOY_LOG
CURRENT_PID=$(pgrep -f $JAR_NAME)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG