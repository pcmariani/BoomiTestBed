#!/usr/bin/env bash

workingDir="$(pwd)"

pushd $BOOMI_HOME > /dev/null
groovy "$BOOMI_HOME"/BoomiTestBed.groovy $@ -w "$workingDir"
exitCode="$?"

popd > /dev/null

if [[ "$exitCode" != "0" ]]; then
    exit 1
fi
