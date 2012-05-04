#!/bin/bash

RUNCLASS=HelloWorldApp
CLASSPATH=/usr/share/java:/usr/share/java/bcel.jar:.
PROJECT=Paralen

java -cp "$CLASSPATH" -javaagent:"$PROJECT.jar" "$RUNCLASS"
