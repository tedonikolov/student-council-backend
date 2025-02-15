docker stop $(docker ps -a -q)
cd ../../../../../student-council-backend
./gradlew clean assemble
cd src/main/docker/studentCouncil
docker compose up -d --build