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
* [JPA](https://javaee.github.io/javaee-spec/javadocs/javax/persistence/package-summary.html) - API for object-relational mapping to databases in Java applications
* [Hibernate](https://hibernate.org/) - Object-relational mapping framework (implementation of JPA)
* [Google cloud](https://cloud.google.com/?hl=en) - Handles the deployment

## 🧭 High-level components <a id="high-level-components"></a>

REST requests are encapsulated inside various controller classes. For stomp communication via websocket we use GameWebSocketController. User, Game, and City are the three entity types in the entity package. User and City entities are permanently stored in database, while Game entities are temporarily stored in GameRepository as long as a user is inside a game. Various service classes are invoked by the corresponding controller classes and they are responsible for handling all functionalities.

The following classes are described in detail for better understanding:

### 👤 UserService

[UserService](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/UserService.java) is used for managing all user related parameters. With this, a user can be created and an existing user can log in. Furthermore, the user can change its username and avatar if they want to. UserService has access to [UserRepository](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/UserRepository.java), which communicates with the database. UserService is mandatory, because the functionality to register and log in is needed so that the game can be played afterwards.

### 🎮 GameService

[GameService](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/GameService.java) is used for managing all game related parameters. With this, a user can create, join, and leave game properly. Furthermore, a game gets deleted if it detects that no users are inside it. GameService has access to [GameRepository](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/repository/GameRepository.java), which communicates with the database. Without GameService, the user will not have the opportunity to create or join a game. In addition, it is needed, if a user wants to leave a game. If no users are left inside a game, it should be deleted from the database.

### 📮 MessageHandler

[MessageHandler](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/messages/MessageHandler.java) is used for processing websocket requests. It is responsible for handling all interactions between client and server that have to be synchronized throughout the application. It covers basic functionalities like logging out a user escpecially during a game session and game specific features like updating points after every round, chat function, communicating if a user wants to play again, and more. The communiction with the currect game happens via [GameService](https://github.com/sopra-fs24-group-40/mapquestor-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/GameService.java).

## 🏎️ Launch & Deployment <a id="launch-and-deployment"></a>

The following steps are needed for a new developer joining our team.

### 🗺️ Prerequisites and Installation

- Make sure Java 17 is installed on your system. _For Windows, please make sure your `JAVA_HOME` environment variable is set to the correct version of Java_.

### 🔨 Build and Run

To build project we work with Gradle.

To build the application use the local Gradle Wrapper:
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

_Additional information about [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/)._

How to build?

```./gradlew build```

How to run?

```./gradlew bootRun```

_Verify by visiting [localhost](http://localhost:8080/)._

How to run tests?

```./gradlew test```

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