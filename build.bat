@echo off
echo clean all...
call .\gradlew.bat clean

echo Building member-service...
call .\gradlew.bat :member-service:clean :member-service:build -x test

echo Building community-service...
call .\gradlew.bat :community-service:clean :community-service:build -x test

echo Building inference-service...
call .\gradlew.bat :inference-service:clean :inference-service:build -x test

echo Building gateway-service...
call .\gradlew.bat :gateway-service:clean :gateway-service:build -x test

echo Build completed.
pause
