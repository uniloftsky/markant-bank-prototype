cd ..\..
call mvnw.cmd clean install

cd docker
docker build --rm --no-cache -t markant-bank -f .\Dockerfile ..\boot\webapps

cd bat
pause