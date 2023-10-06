#!/usr/bin/env bash
FN=$(cat ../output/type)-logs
hey -n 1000000 -c 1000 http://localhost:9090/customers > ../output/$FN
curl -XPOST http://localhost:9090/actuator/shutdown