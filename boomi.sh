#!/usr/bin/env bash

workingDir="$(pwd)"

pushd $BOOMI_HOME > /dev/null
groovy "$BOOMI_HOME"/BoomiTestBed.groovy $@ -w "$workingDir"
popd > /dev/null
