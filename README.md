# Journal Application

## Initialize Database
#### 1. You need to connect to your db user by following way:
linux: sudo -u username <br/>
windows: psql -U username
#### 2. Execute this scripts in terminal to initialize database
CREATE DATABASE journal;
#### 3. Put right credentials in application.yaml
#### 4. Other steps are executing by flyway

## Swagger page
http://localhost:8080/webjars/swagger-ui/index.html

## Working with application by curl's

### Get index page
 curl -X GET http://localhost:8080/
### Add student
 curl -X POST http://localhost:8080/add -d "fullName=FirstName%20SecondName&dateOfBirth=2010-12-04&gradeId=5" -H 'Content-Type: application/x-www-form-urlencoded'
### Delete student
 curl -X POST http://localhost:8080/delete -d "id=3" -H 'Content-Type: application/x-www-form-urlencoded'
### Update student
 curl -X POST http://localhost:8080/update -d "id=2&fullName=SecondName%20FirstName&dateOfBirth=2000-10-15&gradeId=2" -H 'Content-Type: application/x-www-form-urlencoded'
### Update performance
 curl -X POST http://localhost:8080/performance/update -d "id=3&grade=norm" -H 'Content-Type: application/x-www-form-urlencoded'