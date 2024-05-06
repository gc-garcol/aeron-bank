# BANK ACCOUNT

## Tech stacks
- `Raft consensus algorithm`
- `Aeron Cluster`
- `Java 21`

## Pre-requisites
- `Java 21`

## Run project
```shell
./gradlew

docker-compose build --no-cache

docker-compose up &
```

### Generate SBE codecs
```shell
./gradlew generateCodecs
```
