#!/usr/bin/env bash
sleep 5 # wait for the java program to start
FN=$(cat ../output/type)-logs
echo "logging requests in: $FN"
hey -n 10000  http://localhost:9090/block > ../output/$FN
#curl http://localhost:9090/threads >> ../output/$FN
curl -XPOST http://localhost:9090/actuator/shutdown