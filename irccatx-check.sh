#!/bin/bash

binary="./target/irccatx.jar"
pidFile="irccatx.pid"
running=false

if [ -e $pidFile ]; then
    pid=$( cat "$pidFile" )
    if [ -n "$pid" -a -e /proc/$pid ]; then
        running=true
    else
        rm $pidFile
    fi
fi

if [ "$running" = true ]; then
    exit
fi

java -jar $binary &
echo $! > $pidFile
