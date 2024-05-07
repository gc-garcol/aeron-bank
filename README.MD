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

## Run local
```shell
./gradlew runSingleNodeCluster --args='--aeron.cluster.port=9000 --aeron.cluster.node=0 --aeron.cluster.baseDir=node0'
./gradlew runSingleNodeCluster --args='--aeron.cluster.port=9001 --aeron.cluster.node=1 --aeron.cluster.baseDir=node1'
./gradlew runSingleNodeCluster --args='--aeron.cluster.port=9002 --aeron.cluster.node=2 --aeron.cluster.baseDir=node2'
```

## ISSUES

### Problem
Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make field private final java.util.Set sun.nio.ch.SelectorImpl.selectedKeys accessible: module java.base does not "opens sun.nio.ch" to unnamed module @XXXXXX

### Solution
Add ``--add-opens java.base/sun.nio.ch=ALL-UNNAMED`` to the JVM arguments.

![img.png](docs/fix_issue_InaccessibleObjectException.png)
