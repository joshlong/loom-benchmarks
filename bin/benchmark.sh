#!/usr/bin/env bash
cd `dirname $0`


for i in true false; do
 ./jre.sh $i &
 ./hey.sh
done