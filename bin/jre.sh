#!/usr/bin/env bash
S=$(cd `dirname $0` && pwd )
echo jre-${1} > $S/../output/type
java -jar $S/../output/jre-${1}.jar