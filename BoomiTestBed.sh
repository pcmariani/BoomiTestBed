#!/usr/bin/env bash

DEBUG=0
newArgs=""

while getopts "s:d:p:o:bn" arg; do
    case $arg in
        s)  newArgs+="-s $(pwd)/"${OPTARG}" ";;
        d)  newArgs+="-d $(pwd)/"${OPTARG}" ";;
        p)  newArgs+="-p $(pwd)/"${OPTARG}" ";;
        o)  newArgs+="-o ${OPTARG} ";;
        n)  newArgs+="-n ";;
        b)  DEBUG=1; echo ">>> DEBUG ON" ;;
        *)  echo "Usage: [-bn] [-d datafile] [-p propsfile] scriptfile"; exit 1;;
    esac
done

newArgs+="$(pwd)/${@: -1}"

warn() {
   if [ "$DEBUG" -eq 1 ]; then 
       tput setaf 2
       [ -z "$@" ] && echo "$@" || echo ">>> $@"
       tput sgr0
   fi
}


warn "BOOMI_HOME: $BOOMI_HOME"
warn "pwd: $(pwd)"
warn "pushd"
warn

pushd $BOOMI_HOME > /dev/null

warn "pwd: $(pwd)"
warn "newArgs: $newArgs"
warn "groovyclient $BOOMI_HOME/BoomiTestBed.groovy $newArgs"
warn

groovyclient "$BOOMI_HOME"/BoomiTestBed.groovy $newArgs

warn "popd"

popd > /dev/null

warn "pwd: $(pwd)"
warn

# if !"$BOOMI_HOME"; then
# echo "ERROR: Need to set BOOMI_HOME"
# fi

