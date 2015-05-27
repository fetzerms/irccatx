#!/bin/bash
command=$1
args=$2

if [ "$command" == "df" ]; then
    df $args
fi

if [ "$command" == "uptime" ]; then
    uptime $args
fi