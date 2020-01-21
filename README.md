Run serwera springowego:
```
mvn spring-boot:run
```
Build całego projektu z testami: 
```
mvn clean install
```
Puszczenie wszystkich testów:
```
mvn clean test 
```
Baza deweloperska (cleanuje się co run):
```
db = tasky
username = root
password = admin
```
Baza dla testów integracyjnych:
```
db = tasky_integration
username = root
password = admin
```
Serwer SMTP - musi byc na porcie 25 - trzeba uruchomić jako administrator
```
http://nilhcem.com/FakeSMTP/index.html
```
```
username = test
password = admin
``` 