#!/usr/bin/env bash

DEBUG=0

newArgs=""
while getopts "d:p:b" arg; do
    case $arg in
        d)  newArgs+="-d $(pwd)/"${OPTARG}" ";;
        p)  newArgs+="-p $(pwd)/"${OPTARG}" ";;
        b)  DEBUG=1 ;;
        *)  echo "Usage: [-b] [-d datafile] [-p propsfile] scriptfile"; exit 1;;
    esac
done

newArgs+="$(pwd)/${@: -1}"

[ "$DEBUG" -eq 1 ] && echo "BoomiTestBed.sh DEBUG..."
[ "$DEBUG" -eq 1 ] && echo "> BOOMI_HOME: $BOOMI_HOME"
[ "$DEBUG" -eq 1 ] && echo "> pwd: $(pwd)"
[ "$DEBUG" -eq 1 ] && echo "> pushd"

pushd $BOOMI_HOME > /dev/null

[ "$DEBUG" -eq 1 ] && echo "> pwd: $(pwd)"
[ "$DEBUG" -eq 1 ] && echo "> newArgs: $newArgs"
[ "$DEBUG" -eq 1 ] && echo "> groovyclient $BOOMI_HOME/BoomiTestBed.groovy $newArgs"
[ "$DEBUG" -eq 1 ] && echo ""

groovyclient "$BOOMI_HOME"/BoomiTestBed.groovy $newArgs

[ "$DEBUG" -eq 1 ] && echo "> popd"

popd > /dev/null

[ "$DEBUG" -eq 1 ] && echo "> pwd: $(pwd)"
[ "$DEBUG" -eq 1 ] && echo ""

# if !"$BOOMI_HOME"; then
# echo "ERROR: Need to set BOOMI_HOME"
# fi

