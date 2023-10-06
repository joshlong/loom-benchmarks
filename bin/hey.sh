#!/usr/bin/env bash
sleep 5 # wait for the java program to start
FN=$(cat ../output/type)-logs
hey -n 70 -c 20 http://localhost:9090/customers > ../output/$FN
curl -XPOST http://localhost:9090/actuator/shutdown