#!/usr/bin/env bash

DEBUG=1

newArgs=""
while getopts "d:p:" arg; do
    case $arg in
        d)  newArgs+="-d $(pwd)/"${OPTARG}" ";;
        # d)  documentFilesArr=(${OPTARG// / })
        #     for i in "${!documentFilesArr[@]}"; do
        #       documentFilesArr[i]="$(pwd)/${documentFilesArr[i]}"
        #     done
        #     newArgs+="-d '${documentFilesArr[@]}' "
        #     ;;
        p)  newArgs+="-p $(pwd)/"${OPTARG}" ";;
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

