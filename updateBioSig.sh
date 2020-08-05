#!/bin/bash
#Usage: process.sh datePath nameFile

datePath=$1
nameFile=$2
DsFiles=$(ls -d $datePath/*.ds)

#Update EEG info for each of the .ds folders
for f in $DsFiles
do
	changeeeginfo -f $nameFile $f >> $datePath/updateBioSig.log 2>&1	
done
