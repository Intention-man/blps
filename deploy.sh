mvn clean package -DskipTests
scp -P 2222 target/blps-1.0-SNAPSHOT.jar s367044@helios.cs.ifmo.ru:~
