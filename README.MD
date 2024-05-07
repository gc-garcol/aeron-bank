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

## ISSUES

### Problem
Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make field private final java.util.Set sun.nio.ch.SelectorImpl.selectedKeys accessible: module java.base does not "opens sun.nio.ch" to unnamed module @XXXXXX

### Solution
Add ``--add-opens java.base/sun.nio.ch=ALL-UNNAMED`` to the JVM arguments.

![img.png](docs/fix_issue_InaccessibleObjectException.png)
