#!/bin/bash

CLASSPATH=/usr/share/java:/usr/share/java/bcel.jar
PROJECT=Paralen
JAVAFILE="$PROJECT.java"
CLASSFILE="$PROJECT.class"
JARFILE="$PROJECT.jar"

for JAVAFILE in *.java; do
	DATJAVA=`date +%s --reference="$JAVAFILE"`
	DATCLASS=0
	if [ -r "$CLASSFILE" ]; then
		DATCLASS=`date +%s --reference="$CLASSFILE"`
	fi

	if [ $DATJAVA -gt $DATCLASS ]; then
		rm -f "$CLASSFILE"
		javac --classpath "$CLASSPATH" "$JAVAFILE"
	fi
done

DATJAR=`date +%s --reference="$JARFILE"`

if [ $DATJAVA -gt $DATJAR ]; then
	rm -f "$JARFILE"
	jar -cvfm0 "$JARFILE" "$PROJECT.MF" "$CLASSFILE"
fi

