#!/usr/bin/env bash

mkdir -p output

export SPRING_THREADS_VIRTUAL_ENABLED=true

## JRE
rm -rf target
./mvnw -DskipTests package
mv target/loom*jar output/jre.jar


## GRAALVM VIRTUAL
rm -rf target
./mvnw -DskipTests -Pnative native:compile
mv target/loom output/graalvm-virtual

## GRAALVM TRADITIONAL
export SPRING_THREADS_VIRTUAL_ENABLED=false

rm -rf target
./mvnw -DskipTests -Pnative native:compile
mv target/loom output/graalvm-traditional


