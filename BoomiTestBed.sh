#!/usr/bin/env bash

# if !"$BOOMI_HOME"; then
  # echo "ERROR: Need to set BOOMI_HOME"
# fi

# echo $(pwd)/$1
dir=$(pwd)
pushd $BOOMI_HOME > /dev/null
groovyclient "$BOOMI_HOME"/BoomiTestBed.groovy $dir/$*
popd > /dev/null
