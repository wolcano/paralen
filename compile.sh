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
		echo "Compiling '$JAVAFILE'..."
		rm -f "$CLASSFILE"
		javac -cp "$CLASSPATH" "$JAVAFILE"
	fi
done

DATJAR=0
if [ -r "$JARFILE" ]; then
	DATJAR=`date +%s --reference="$JARFILE"`
fi

if [ $DATJAVA -gt $DATJAR ]; then
	echo "Creating '$JARFILE'..."
	rm -f "$JARFILE"
	jar -cvfm0 "$JARFILE" "$PROJECT.MF" "$CLASSFILE"
fi

