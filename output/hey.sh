#!/usr/bin/env bash
FN=$(cat type)-logs
echo $FN
hey http://localhost:9090/customers