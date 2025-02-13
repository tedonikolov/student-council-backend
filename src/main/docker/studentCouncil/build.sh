cd ../../../../../student-council-backend
./gradlew clean assemble
cd src/main/docker/studentCouncil
docker-compose up -d --build