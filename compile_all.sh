#!/usr/bin/env bash

rm -rf output && mkdir -p output

export PROP="spring.threads.virtual.enabled"
R=$(cd . && pwd)

function virtual_threads() {
  R_F=$R/src/main/resources
  cat $R_F/application.properties | grep -v $PROP  >> $R_F/tmp.properties
  echo $PROP=$1 >> $R_F/tmp.properties
  mv $R_F/tmp.properties $R_F/application.properties
  cat $R_F/application.properties
}

function jre(){
  echo "doing JRE compilation with virtual threads: $1"
  virtual_threads $1
  rm -rf target
  ./mvnw -DskipTests package
  mv target/loom*jar output/jre-${1}.jar
}

function graalvm(){
  echo "doing GraalVM compilation with virtual threads: $1"
  virtual_threads $1
  rm -rf target
  ./mvnw -DskipTests -Pnative native:compile
  mv target/loom output/graalvm-$1
}

for i in true false; do
  jre $i
  graalvm $i
done


