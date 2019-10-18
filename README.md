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
* Testy junitowe muszą się kończyć z *Test.java
* Jak chcesz pisać integracyjne to extends IntegrationBase <br/>
* Jeśli chcesz odpalać integracyjne przez intellija to musisz ręcznie clearować tą testową bazę. Jak mavenem to sama się będzie clearować co run <br/>
* Dobrą pratyką jest odpalanie wszystkich testów jak dodasz jakąś funkcjonalność albo jak coś naprawiasz.