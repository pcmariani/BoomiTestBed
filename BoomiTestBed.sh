#!/usr/bin/env bash

DEBUG=0
newArgs=""

while getopts "s:d:p:o:w:bn" arg; do
    case $arg in
        s)  newArgs+="-s $(pwd)/"${OPTARG}" ";;
        d)  newArgs+="-d $(pwd)/"${OPTARG}" ";;
        p)  newArgs+="-p $(pwd)/"${OPTARG}" ";;
        e)  newArgs+="-e ${OPTARG} ";;
        w)  newArgs+="-e $(pwd) ";;
        n)  newArgs+="-n ";;
        b)  DEBUG=1; echo ">>> DEBUG ON" ;;
        # *)  echo "Usage: [-bn] [-d datafile] [-p propsfile] scriptfile"; exit 1;;
        *)  groovy "$BOOMI_HOME"/BoomiTestBed.groovy -h; exit 1;;
    esac
done

newArgs+="$(pwd)/${@: -1}"

_echo() {
   if [ "$DEBUG" -eq 1 ]; then 
       [ -z "$@" ] && echo "$@" || echo "$(tput setaf 2)>>> $@$(tput sgr0)"
   fi
}


_echo "BOOMI_HOME: $BOOMI_HOME"
_echo "pwd: $(pwd)"
_echo "pushd"
_echo

pushd $BOOMI_HOME > /dev/null

_echo "pwd: $(pwd)"
_echo "newArgs: $newArgs"
_echo "groovyclient $BOOMI_HOME/BoomiTestBed.groovy $newArgs"
_echo

groovyclient "$BOOMI_HOME"/BoomiTestBed.groovy $newArgs

_echo "popd"

popd > /dev/null

_echo "pwd: $(pwd)"
_echo

# if !"$BOOMI_HOME"; then
# echo "ERROR: Need to set BOOMI_HOME"
# fi

