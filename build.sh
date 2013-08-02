#!/bin/sh
find src -name *java >sources.list
cat sources.list
javac -classpath . -d bin/ @sources.list
jar cvf ./bin/eqq.jar -C bin/ .
jar umf ./MANIFEST.MF ./bin/eqq.jar
