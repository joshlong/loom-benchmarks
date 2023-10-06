#!/usr/bin/env bash
echo jre-${1} > ../output/type
java -jar ../output/jre-${1}.jar