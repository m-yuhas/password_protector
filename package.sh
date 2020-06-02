#!/bin/bash

# This script takes the build artifacts from password protector and packages
# them into one of many common packages.

function help {
    echo "Stub for help menu"
}

declare -A options
options["-h"]="help" 
options["--help"]="help"
for arg in $@
do
  echo $arg
done
