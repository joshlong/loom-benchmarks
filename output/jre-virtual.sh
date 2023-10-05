#!/usr/bin/env bash
echo jre-virtual > type
export SPRING_THREADS_VIRTUAL_ENABLED=true
java -jar jre.jar