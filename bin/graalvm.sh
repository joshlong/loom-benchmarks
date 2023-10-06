#!/usr/bin/env bash
S=$(cd `dirname $0` && pwd )
echo graalvm-$1 > $S/../output/type
$S/../output/graalvm-$1