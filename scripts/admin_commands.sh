#!/bin/bash
#
# These commands should only be triggered
# from private queries with matching hostmasks.
#
command=$1
hostmask=$2
args=$3

if [ "$command" == "df" ]; then
    df $args
fi

if [ "$command" == "uptime" ]; then
    uptime $args
fi