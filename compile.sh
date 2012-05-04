#!/bin/bash

CLASSPATH=/usr/share/java:/usr/share/java/bcel.jar
PROJECT=Paralen
JAVAFILE="$PROJECT.java"
CLASSFILE="$PROJECT.class"

DATJAVA=`date +%s --reference="$JAVAFILE"`
DATCLASS=0
if [ -r "$CLASSFILE" ]; then
	DATCLASS=`date +%s --reference="$CLASSFILE"`
fi

if [ $DATJAVA -gt $DATCLASS ]; then
	rm -f "$CLASSFILE"
	javac --classpath "$CLASSPATH" "$JAVAFILE"
	RET=$?
	if [ $RET -eq 0 ]; then
		rm -f "$PROJECT.jar"
		jar -cvfm0 "$PROJECT.jar" "$PROJECT.MF" "$CLASSFILE"
	fi
fi


