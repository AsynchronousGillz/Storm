#!/bin/sh

################################
# Gerhard van Andel            #
# Version 1.0, 2016-09-29      #
# Colorado State University    #
################################

RED='\033[0;31m'
NC='\033[0m' # No Color

if [ $# -ne 1 ]; then
	echo "Error needs file." >&2
	exit 1
fi

# Login and kick up all messaging nodes
while read -r line; do
	tmux splitw "ssh $line"
	tmux select-layout even-vertical
done < "$1"

# Makes all the terminals share the same input
tmux set-window-option synchronize-panes on
