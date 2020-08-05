#!/bin/bash
#Usage: process.sh datePath posFile

datePath=$1
posFile=$2
DsFiles=$(ls -d $datePath/*.ds)

#Update EEG info for each of the .ds folders
for f in $DsFiles
do
	changeeeginfo -f $posFile $f >> $datePath/updateEEG.log 2>&1	
done

cp $posFile $datePath	
