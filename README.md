# MapQuestor
<div style="text-align: justify">
*picture*

Have you ever wanted to travel the world, but lack the money and time? That's where MapQuestor comes in mind... What are you waiting for? Become a MapQuestor!
P.S. go check out our front-end implementation [here](https://github.com/sopra-fs24-group-40/mapquestor-client).

## 📜 Table of Contents

1. [👋 Introduction](#introduction)
2. [🛠️ Technologies](#️technologies)
3. [🧭 High-level components](#high-level-components)
4. [🏎️ Launch & Deployment](#️launch-and-deployment)
5. [🛣️ Roadmap](#️roadmap)
6. [👔 Authors and acknowledgment](#authors-and-acknowledgment)
7. [📝 License](#license)

## 👋 Introduction <a id="introduction"></a>

To give people a fun way of learning about potential sightseeing destinations, we introduce a game called MapQuestor. It is playable by multiple users at once, this will increase engagement and raise a competitive spirit. A picture of a place of interest will be shown to the players and they must guess which city or country it is from.

## 🛠️ Technologies <a id="technologies"></a>

For the devolopment of the server, we relied on the following technologies:

* [Java](https://www.java.com/de/download/manual.jsp) - Programming language
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework
* [Axios API](https://axios-http.com/docs/api_intro) - REST-based communication
* [Stomp](https://stomp-js.github.io/stomp-websocket/) - Websocket communication 
* [PostgreSQL](https://www.postgresql.org/) - Database management system used for storage and retrieval of player data ???
* [JPA](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/package-summary.html) - API for object-relational mapping to databases in Java applications
* [Hibernate](https://hibernate.org/) - Object-relational mapping framework (implementation of JPA)
* [Google cloud](https://cloud.google.com/?hl=en) - Handles the deployment

## 🧭 High-level components <a id="high-level-components"></a>

REST requests are encapsulated inside various controller classes. For stomp communication via websocket we use GameWebSocketController. User, Game, and City are the three entity types in the entity package. User and City entities are permanently stored in database, while Game entities are temporarily stored in GameRepository as long as a user is inside a game. Various service classes are invoked by the corresponding controller classes and they are responsible for handling all functionalities.

The following classes are described in detail for better understanding:

### UserService

[UserService](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/UserService.java) is used for managing all user related parameters. With this, a user can be created and an existing user can log in. Furthermore, the user can change its username and avatar if they want to. UserService has access to [UserRepository](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/UserRepository.java), which communicates with the database. UserService is mandatory, because the functionality to register and log in is needed so that the game can be played afterwards.

### GameService

[GameService](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/GameService.java) is used for managing all game related parameters. With this, a user can create, join, and leave game properly. Furthermore, a game gets deleted if it detects that no users are inside it. GameService has access to [GameRepository](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/GameRepository.java), which communicates with the database. Without GameService, the user will not have the opportunity to create or join a game. In addition, it is needed, if a user wants to leave a game. If no users are left inside a game, it should be deleted from the database.

### MessageHandler

[MessageHandler](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/messages/MessageHandler.java) is used for processing websocket requests. It is responsible for handling all interactions between client and server that have to be synchronized throughout the application. It covers basic functionalities like logging out a user escpecially during a game session and game specific features like updating points after every round, chat function, communicating if a user wants to play again, and more. The communiction with the currect game happens via [GameService](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/GameService.java).

---------

## 🏎️ Launch & Deployment <a id="launch-and-deployment"></a>

The following steps are needed for a new developer joining our team.

### 🗺️ Prerequisites and Installation

- For the development environment, Node.js is needed. We worked with the exact version [**v20.11.0**](https://nodejs.org/download/release/v20.11.0/) which comes with the npm package manager.
- Update the npm package manager to **10.4.0** by running ```npm install -g npm@10.4.0```. Check the correct version by running ```node -v``` and ```npm --version```, which should give you **v20.11.0** and **10.4.0** respectively.
- Run this command to install all other dependencies, including React by running ```npm install```
- Furthermore, you need to install Google Maps by running ```npm install @googlemaps/js-api-loader```

### 🔨 Build and Run

- Build the app by running ```npm run build```
- Start the app by running ```npm run dev```
- Open [localhost](http://localhost:3000) to view it in browser. _We recommend you to use Google Chrome._

### 📈 Testing

- Run tests by running ```npm run test```
    - _For macOS user with a 'fsevents' error --> https://github.com/jest-community/vscode-jest/issues/423_

### 💡 External dependencies

Both client and server have to be running for the application to behave as expected.

### ✉️ Releases

We stronlgy recommend to follow this [tutorial](https://docs.github.com/en/repositories/releasing-projects-on-github/managing-releases-in-a-repository) and to properly document and comment your release.

## 🛣️ Roadmap <a id="roadmap"></a>

- _Global leaderboard_ --> Leaderboard could be filterable by metrics (e.g. time or mode).
- _Joker assignment_ --> Assign jokers depending on points after each round (e.g. player with least points gets a joker).
- _Worldwide_ --> Include more cities and countries from other continents and game modes (e.g. City-Europe, Country-South-America, etc.).

## 👔 Authors and acknowledgment <a id="authors-and-acknowledgment"></a>

Authors of MapQuestor:

- [Branislav Milutinović](https://github.com/B-M)
- [Shanthos Magendran](https://github.com/LaughingF0x)
- [Benjamin Halfar](https://github.com/bhalf)
- [Nikola Stevanović](https://github.com/nik-stev)
- [Arbër Markaj](https://github.com/domeniku7)

We want to use this opportunity to thank our teaching assistant [Louis Caerts](https://github.com/LouisCaerts). His guidance and assistance were helpful and we really appreciate it.

## 📝 License <a id="license"></a>

This project is licensed under the Apache License Version 2.0.

</div>

# SoPra RESTful Service Template FS24

## Getting started with Spring Boot
-   Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/index.html
-   Guides: http://spring.io/guides
    -   Building a RESTful Web Service: http://spring.io/guides/gs/rest-service/
    -   Building REST services with Spring: https://spring.io/guides/tutorials/rest/

## Setup this Template with your IDE of choice
Download your IDE of choice (e.g., [IntelliJ](https://www.jetbrains.com/idea/download/), [Visual Studio Code](https://code.visualstudio.com/), or [Eclipse](http://www.eclipse.org/downloads/)). Make sure Java 17 is installed on your system (for Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java).

### IntelliJ
If you consider to use IntelliJ as your IDE of choice, you can make use of your free educational license [here](https://www.jetbrains.com/community/education/#students).
1. File -> Open... -> SoPra server template
2. Accept to import the project as a `gradle project`
3. To build right click the `build.gradle` file and choose `Run Build`

### VS Code
The following extensions can help you get started more easily:
-   `vmware.vscode-spring-boot`
-   `vscjava.vscode-spring-initializr`
-   `vscjava.vscode-spring-boot-dashboard`
-   `vscjava.vscode-java-pack`

**Note:** You'll need to build the project first with Gradle, just click on the `build` command in the _Gradle Tasks_ extension. Then check the _Spring Boot Dashboard_ extension if it already shows `soprafs24` and hit the play button to start the server. If it doesn't show up, restart VS Code and check again.

## Building with Gradle
You can use the local Gradle Wrapper to build the application.
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Development Mode
You can start the backend in development mode, this will automatically trigger a new build and reload the application
once the content of a file has been changed.

Start two terminal windows and run:

`./gradlew build --continuous`

and in the other one:

`./gradlew bootRun`

If you want to avoid running all tests with every change, use the following command instead:

`./gradlew build --continuous -xtest`

## API Endpoint Testing with Postman
We recommend using [Postman](https://www.getpostman.com) to test your API Endpoints.

## Debugging
If something is not working and/or you don't know what is going on. We recommend using a debugger and step-through the process step-by-step.

To configure a debugger for SpringBoot's Tomcat servlet (i.e. the process you start with `./gradlew bootRun` command), do the following:

1. Open Tab: **Run**/Edit Configurations
2. Add a new Remote Configuration and name it properly
3. Start the Server in Debug mode: `./gradlew bootRun --debug-jvm`
4. Press `Shift + F9` or the use **Run**/Debug "Name of your task"
5. Set breakpoints in the application where you need it
6. Step through the process one step at a time


## Testingg
Have a look here: https://www.baeldung.com/spring-boot-testing  