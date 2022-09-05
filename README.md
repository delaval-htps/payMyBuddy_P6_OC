# Paymybuddy

Paymybuddy is a web application that makes easier money transaction between users.
The user connects to the application via Oauht2 or a login and password or registers for the first time. Once connected, he can enter his banking information and immediately make transfers to the accounts of other selected users.

This application was developed with springBoot, springSecurity for back-end and Thymeleaf, Jquery for front-end.

## Running the application locally

There are several ways to run application on your local machine. One way is to execute the `main` method in the `com.paymybudddy.PayMyBuddyapplication.java` from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
## Access
Since application started, you can access to it with the URL:

```html
https://localhost:8080/
```
Below you can log to it either:
* by using provider like Github, Google or Facebook
* by registering your personal information 
* by filling your login and password, if you are allready registred in application

<div style="display:flex;flex-direction: row;">
<img src="src/main/resources/images/loginPage.png"
     alt="login-page"
     style="margin:auto;width: 80%; " />
</div>

## Run test

We used TDD to implement code in this project:

So, from creation of integration and unit tests, we created the source's code and check it to be sure of its correct working.

* To run unit tests only, you can use command:

```shell
mvn:test
```
* To run integration tests only, you can use command:

```shell
mvn failsafe:integration-test
```
* If you want to launch a build phase without integration tests, you can use next option for example:

```shell
mvn verify -Dskip.it=true
```
## Jacoco Coverage

A report of coverage is automatically done when you launch tests.

you can access to it at location :`target/site/jacoco/index.html`

<div style="display:flex;flex-direction: row;">
<img src="src/main/resources/images/Jacoco.png"
     alt="swagger-ui/administration"
     style="margin:auto;width: 80%; " />
</div>

## Site and Reports

We created a site with Maven to aggregate different reports:


* 	**SureFire Report** for all unit Tests.
* 	**Jacoco Report** for coverage in Junit Tests.
* 	**Javadoc Report** on src/main/java. 


To deploy the site use the command:

```shell
mvn site
```
and in `target/site` you will find a page index.html that you can open in your web browser witch contains these different reports:

## Application was built with

* `SpringBoot 2.4.4`
* `Java 8`
* `Maven 3.6.3`
* `H2 and Mysql databases`
* `JUnit`
* `Jacoco`
* `Log4j`

## Versions

* **V1.1.0**	:	Lastest version of application with upgrade tests and logs:

		1. Check code with logger to correctly display and save errors(Level ERROR), calculations(Level DEBUG), requests and responses(Level INFO)
		2. Update of method POST medicalRecord for all cases tests: update integration and unit tests to pass to green
		3. creation of class diagrams
		4. update Javadoc and README 

* **V1.0.0**	:	Forth version of application with upgrade tests and documentation:

		1. Update relationship between Person and fireStation to M:M
		2. Update Model Response to PersonDto for more readable response for each URL's endpoint
		3. Update and check all UT and IT for the application
		4. Creation of Api Documentation with swagger2 and mvn site with reports
		5. Management of Actuator: health,info,trace and metrics.
		
* **V0.2.0**	:	Third version with creation of RestController for Emergency's services using TDD:


		1. creation of Rest Controller for emergencies' services that manage URLS (fireStation,childAlert,phoneAlert,fire,flood,personInfo and communityEmail)
		2. creation of Model Response to make more easily readable the body of each URLS's ResponseEntity
		3. activation and configuration of actuator for health, info, metrics and httpTrace
		4. implementation of javadoc and check for chekstyle rules.

* **V0.1.0**	: 	Second version, implementation of code using TDD:


		1. creation of Service to load Data from a source using a strategy pattern in CommandLineRunner.
		2. creation of Rest Controller for administration that manages Entities Person,FireStation,MedicalRecord, Medication and Allergy.
		3. creation of global handler Exceptions to manage exceptions from Controllers 
		4. implementation of javadoc and update code to check checkStyle's rules.

* **V0.0.1**	: 	First version of the project. implementation of Structure only.