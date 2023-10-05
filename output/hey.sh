#!/usr/bin/env bash
FN=$(cat type)-logs
hey http://localhost:9090/customers > $FN