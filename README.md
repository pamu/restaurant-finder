# Restaurant Finder micro-service

Built on top of playframwork scala and slick

### Libraries used

    1. Circe json
    2. Monix task (better alternative to Futures)
    3. Slick (Database library)
    4. Shapeless
    5. H2 database (postgres dialect)
    6. Cats-core (functional programming library)
    7. Flyway database migration
    8. Scala test for tests

### Docker

To run on docker ensure docker system is running on your system

go to the project root

```bash
./docker.sh
```


### REST API








### Instructions

1. Compiling the project

   `sbt compile`

2. Compiling the project tests

   `sbt test:compile`

3. Run project

   `sbt run`

4. Run tests

   `sbt test`


### Screenshots

![Tests](test/resources/tests.png)
