# Quantum

> Quantum webapp using Spring Boot on the backend and React on the frontend, with 
Maven and Webpack as build tools, hot reloading on both sides and without xml configuration.

## Production

App can be reached here -->
Com Hem:  http://52.166.144.86/
Boxer : http://52.166.1.243/

## Status

Travis CI:

[![Build Status](https://travis-ci.org/ComHem/quantum.svg?branch=master)](https://travis-ci.org/ComHem/quantum)

## Docker

#### Build
docker build . -t quantum

#### Start
docker run --rm --name quantum -p 8080:8080 quantum

## Quickstart
To run the app you just need to:

    cd quantum
    mvn spring-boot:run

To check everything is running you can:

    # Visit the homepage
    http://localhost:8080

## Start developing
The Java code is available at `src/main/java` as usual, and the frontend files are in 
`src/main/frontend`.

### Running the backend
Run `QuantumApplication` class from your IDE.

### Running the frontend
Go to `src/main/frontend` and run `npm start`. (Run `yarn` (or `npm install`) before that if it's the first time)

Now we should work with `localhost:9090` (this is where we'll see our live changes reflected)
 instead of `localhost:8080`.

### Hot reloading
In the **backend** we make use of Spring DevTools to enable hot reloading, 
so every time we make a change in our files an application restart will
be triggered automatically.

Keep in mind that Spring DevTools automatic restart only works if we run the 
application by running the main method in our app, and not if for example we run 
the app with maven with `mvn spring-boot:run`.

In the **frontend** we use Webpack Dev Server hot module replacement 
through the npm script `start`. Once the script is running the Dev Server will be 
watching for any changes on our frontend files.

This way we can be really productive since we don't have to worry about recompiling and deploying
our server or client side code every time we make changes.


### Profiles

The project comes prepared for being used in three different environments plus 
another one for testing. We use Spring Profiles in combination with Boot feature for 
loading properties files by naming convention (application-*\<profile name\>*.properties).

You can find the profile constants in 
[StarterProfiles](src/main/java/com/dlizarra/starter/StarterProfiles.java) 
and the properties files in `src/main/resources`.


### Security
All the boilerplate for the initial Spring Security configuration is already created. These are they key classes:

- [User](src/main/java/com/dlizarra/starter/user/User.java), [Role](src/main/java/com/dlizarra/starter/role/Role.java) and  [RoleName](src/main/java/com/dlizarra/starter/role/RoleName.java) which are populated by [data.sql](src/main/resources/data.sql) file for the default profile only.
- [CustomUserDetails](src/main/java/com/dlizarra/starter/support/security/CustomUserDetails.java)
- [CustomUserDetailsService](src/main/java/com/dlizarra/starter/support/security/CustomUserDetailsService.java)
- [SecurityConfig](src/main/java/com/dlizarra/starter/SecurityConfig.java) with just very basic security rules.

### Unit and integration testing
For **unit testing** we included Spring Test, JUnit, Mockito and AssertJ as well as an [AbstractUnitTest](src/test/java/com/dlizarra/starter/support/AbstractUnitTest.java) class that we can extend to include the boilerplate annotations and configuration for every test. [UserServiceTest](src/test/java/com/dlizarra/starter/user/UserServiceTest.java) can serve as an example.

To create integration tests we can extend [AbstractIntegrationTest](src/test/java/com/dlizarra/starter/support/AbstractIntegrationTest.java) and make use of Spring `@sql` annotation to run a databse script before every test, just like it's done in [UserRepositoryTest](src/test/java/com/dlizarra/starter/user/UserRepositoryTest.java).

### Code coverage
The project is also ready to use Cobertura as a code coverage utility and Coveralls to show a nice graphical representation of the results, get a badge with the results, etc. 

The only thing you need to do is to create an account in [Coveralls.io](http://coveralls.io) and add your repo token key [here](pom.xml#L134) in the pom.xml.

And if you want to use different tools you just need to remove the plugins from the pom.

### Linting
We added ESLint preconfigured with Airbnb rules, which you can override and extend in [.eslintrc.js](src/main/frontend/.eslintrc.js) file. To lint the code you can run `npm run eslint` or configure your IDE/text editor to do so.

### Continuous integration and deployment
A [travis.yml](.travis.yml) file is included with a minimal configuration just to use jdk 8, trigger the code analysis tool and deploy the app to Heroku using the `api_key` in the file. 

We also included a Heroku [Procfile](Procfile) which declares the `web` process type and the java command to run our app and specifies which Spring Profile we want to use.

### Other ways to run the app
#### Run everything from Maven

    mvn generate-resources spring-boot:run

The Maven goal `generate-resources` will execute the frontend-maven-plugin to install Node
and Npm the first time, run npm install to download all the libraries  that are not 
present already and tell webpack to generate our `bundle.js`. It's the equivalent of running `npm run build` or `npm start` on a terminal.

#### Run Maven and Webpack separately (no hot-reloading)

    mvn spring-boot:run
In a second terminal:
    
    cd src/main/frontend
    npm run build

## Tech stack and libraries
### Backend
- [Spring Boot](http://projects.spring.io/spring-boot/)
- [Spring MVC](http://docs.spring.io/autorepo/docs/spring/3.2.x/spring-framework-reference/html/mvc.html)
- [Spring Data](http://projects.spring.io/spring-data/)
- [Spring Security](http://projects.spring.io/spring-security/)
- [Spring Test](http://docs.spring.io/autorepo/docs/spring-framework/3.2.x/spring-framework-reference/html/testing.html)
- [JUnit](http://junit.org/)
- [Mockito](http://mockito.org/)
- [AssertJ](http://joel-costigliola.github.io/assertj/)
- [Lombok](https://projectlombok.org/)
- [Orika](http://orika-mapper.github.io/orika-docs/)
- [Maven](https://maven.apache.org/)

### Frontend
- [Node](https://nodejs.org/en/)
- [React](https://facebook.github.io/react/)
- [Redux](http://redux.js.org/)
- [Webpack](https://webpack.github.io/)
- [Axios](https://github.com/mzabriskie/axios)
- [Babel](https://babeljs.io/)
- [ES6](http://www.ecma-international.org/ecma-262/6.0/)
- [ESLint](http://eslint.org/)

---
