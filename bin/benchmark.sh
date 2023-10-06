#!/usr/bin/env bash

cd `dirname $0`

START=`pwd`

echo "The benchmark start directory is ${START} "

function reset(){
  pkill java
  pkill graalvm-false
  pkill graalvm-true

  cd "$START"/..
  docker compose down
  docker compose up -d
  sleep 5
  cd "$START"
}

for i in true false; do

  reset
  ./jre.sh $i &
  ./hey.sh

  reset
  ./graalvm.sh $i &
  ./hey.sh
done