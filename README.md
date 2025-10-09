# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Architecture Overview

[Server Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMyzKfMkIgmpOxfDA0KPMB4lUEiMAIEJ4oYvZwlwCiHDmEgJqqKSb6GLuNL7gyTJTgZc4BXeS7CmKEpujKcplu8SqYCqwYam6RpoBAzAAGa+E2w4OkmvrOl2MA9n225+dZ-rpUqAByWUinlHBRjGcaFFpRXwMgqaNOmTjdHpYw5qoeaGYWxbQPUejriihJqGASx0flt6FdZtmxVuvmCgV9IwCA8QoCAapNPofw7NO5YqSZAJhbyEWVMu0Xrjd67xQZN7hWtxWdvUp1bDsyWqhq-3nQCMAAJJoCA0AouAlUgdU-pQzDxZHWAoPqa1KCxgp6HwsmPXYX1TgAIyDcsI1jQWYxFiW9Q+NMl7QEgABeKC7OZjYMdt-n3fyY4Tigz7xOel7XrtAqRSua4BuLW0le2OmloJwkZKoAGYMriMSWBhFXfMJGod8FFUfWRu0fjmFE2AOF4QRBk0WRYym4h5ukQ29GMd4fj+F4KDoDEcSJP7geq74WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMrtIVbH6Wf62foFr+fwj9NkunZQkR2L8Fu2gPmK3zC6jjAjJgCLkBITXlF13dzf3sKMAAOJMnLtc5yl6qSkzeos+zDGSzr5eleV-a89VpYNe3zNQGzHPY7j8adYTJS2yTA0wENVP5tBdNTbKIYwHNRKLVzXuL11vmujOCudjtq17RANwPAHc67dzNvXT6-NFyPUHtIbw2Ax49xzuvSoyt6jh1POQHw+4YAYhRGAHw7wBQ6lnlgygJZSTa0-kjMCmlaHW1PnbfCoxloMU8D7AIbkYjYHFBqfiaJh5Kg0FHDesch5J1TvYJUWdLwQNzmgku9RC6FGoWXWyyAchD2EeA3uCMm6BTbqAruKi+64OlsPUeIt5GTzSjPeIc8OZQP7jQ36ZVexr0bmIpBECt5NUZi1aMON2q5xPr1Bo6ZyaX0pvyamt9JqlkZhRRxnM2HOPMetCum15D6PKJLIKYBtFzAETkDEZiHpClqCPZgmjmAQGypWBArdhGmA-pk0qtSWjZSKWoSqFR0EEKIWgEpYAuk9I0GojsNQXjLGkTmAsDRxhzJQBDaQBZSbhGCIEEEmx4i6hQLVT4ixvjJFAGqQ5hkxjfGWXVJUxyLgwE6PQ3WgpRLMKzLM4RCyllKlWeszZ2zli7P2RcvYJyEBnNBcckENy7lXIeU87m3tmL+A4AAdjcE4FATgYgRmCHALiAA2eAQshFzBgEUG20daGx1aB0KRMj7EQKzLCuYzzEyKNhAXORdcWX1SVBZLlpcpm2UPHIFAPTdFIRWKyrkuSZAANHEYy8nd0BSvQOUwqsD6jVJ8XXIGqVp7JN3vPdJEV2ndg8fo7x1i65+OagfEJx9KhvJJlEq+sSb4TXpgqexKSlpIraeorJP8cm8ypIq+oB0jpqialASVsrNUwMqTAZ6zTyXxU5AaqeAAhR+z8Fpmu+iKiuPTXxeLLv6PNHACQv0dXjZ13UmHnwpsNT141aYJOmvmlA80cgBvfoqpetlV7yvySSo8EqlQYkTUW5Ny4Ayyx6dazlX5agog1ggZgM7fnSCoSXJeulPlzD+fUDZWyuZRwADxJimc8XCLChrLNPTAc9gQuZIo4SiywKA+wQE2EHJACQwA-r-QBgAUhAcUZLDD+FOcdSlp9qW61pcyGSPRlmyPHugLM2AIU-qgHACA9koBrGfdIdlGE85CuUTypCuH8OUCIyRmVu7BVfmQ8veoAArKDaBJUqJWHh4ABHmPQFYyevdY7I3Kr1KqtA6rIEf21ZY5gtqJ7AyNTvPeC8h2uK4+4iqqC72lnU+ge1AT61HwYV1V1ESyatuvh2u+iS-Umo5gOlaX1FwWo3O6X+y9-7eZbtG46caE38rmEmqWKm03LNlPKLNtiYDVqfr2l+c6pa+dHcZmOKWQy1oWlZjqNmwnE3sxfD1uYvWdp9TNNLfbX5pKDSW0qyzy1-wMSOMch1joQ2ymJqAENVAKCoCcZIKAMTCdE8R8T5HosD1FOKdcbwEvppWdIY0EoYYkezRqHp5B7JqarOaGAYYc4ta-glDrgX9P+h6WQI7gYzSWHO5GIJh8SsvNszbWO6ZgiOfbTTFzyiTs1nDMhN+XnoFZeDSvK14aFXBeUWuN0CFJXTaY7N0j83MuLZgAAdV3uiIw2gYCQHW3gx8MBMewAp8shunXqNrrS4Q94MANb7mLkKw9dDEPmBgDe0oJnagPqzGkr9vsvAicA8B6X8pEDBlgMAbAeHCB5AKBS0RlbSxxwTknFOvRjAKIeDRy+7GrJw6jcA-z8g9AGDKdJ5H+0beBgQGZtAqhHfKZTfAiAiC3d6qQhoRH46gFK7d26L3C2LF+4D32PzIfG4Rud+HkBfZxne70ypuPAYE-jJXabln0VsiHQYtQ7xIxKMExdb9mAYvWFIqAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
