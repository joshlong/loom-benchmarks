#!/usr/bin/env bash
sleep 4 # wait for the java program to start
FN=$(cat ../output/type)-logs
hey -n 100000 -c 1000 http://localhost:9090/customers > ../output/$FN
curl -XPOST http://localhost:9090/actuator/shutdown