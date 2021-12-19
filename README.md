# spring-jpa-seek-pagination

Keyset/Seek pagination with Springs repository pattern

Inspired by
the [JOOQ synthetic SEEK clause](https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/select-statement/seek-clause/)
and [use-the-index-luke](https://use-the-index-luke.com/no-offset) call to arms blog post.

### Testing
#### Prerequisites
This project makes use of [Testcontainers](https://www.testcontainers.org/) when not running with
default spring profile. If you wish to run the test suite against one of these databases you need to
make sure you have
[Docker](https://www.docker.com/) installed. The version required can be
found [here](https://www.testcontainers.org/supported_docker_environment/)

#### Running the tests
To run the test suite (Using a H2 embedded database) you can run the following command:
```shell
mvn clean test
```

To target a different database, you can set the active spring profile, for example:

```shell
mvn -Dspring.profiles.active=postgres clean test 
```

The current list of databases supported:

| Database      | Profile value |
| ------------- |:-------------:|
| Postgresql    | postgres      |
| MySQL         | mysql         |