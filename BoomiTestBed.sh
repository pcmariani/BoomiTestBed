#!/usr/bin/env bash

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
# echo "newArgs: $newArgs"

pushd $BOOMI_HOME > /dev/null
groovyclient "$BOOMI_HOME"/BoomiTestBed.groovy $newArgs
popd > /dev/null

# if !"$BOOMI_HOME"; then
# echo "ERROR: Need to set BOOMI_HOME"
# fi

