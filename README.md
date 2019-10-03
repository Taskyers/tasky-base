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
> CREATE DATABASE tasky;
> CREATE USER 'root'@'localhost' IDENTIFIED BY 'admin';
> GRANT ALL PRIVILEGES ON tasky.* TO 'root'@'localhost';
> FLUSH PRIVILEGES;
```