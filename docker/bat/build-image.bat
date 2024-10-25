cd ..
call mvnw.cmd clean install

cd docker
start /wait docker build --rm --no-cache -t markant-bank -f ./Dockerfile ../boot/webapps