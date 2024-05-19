#!/bin/sh
java --add-opens java.base/sun.nio.ch=ALL-UNNAMED -Djava.net.preferIPv4Stack=true -Daeron.ipc.mtu.length=8k "$@" -jar /home/aeron/jar/bank-cluster.jar
