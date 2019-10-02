```
mvn spring-boot:run
```
```
mvn clean test 
```
```
mvn clean install
```
```
mysql -u root -p 
> CREATE DATABASE plantator;
> CREATE USER 'plantator_user'@'localhost' IDENTIFIED BY 'zaq1@WSX';
> GRANT ALL PRIVILEGES ON plantator.* TO 'plantator_user'@'localhost';
> FLUSH PRIVILEGES;
> source /src/main/resources/db/script.sql;
```