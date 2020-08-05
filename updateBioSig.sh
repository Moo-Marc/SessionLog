#!/bin/bash
#Usage: process.sh subjID datePath nameFile

subjID=$1
datePath=$2
nameFile=$3
DsFiles=$(ls -d $datePath/*.ds)

#Update EEG info for each of the .ds folders
for f in $DsFiles
do
	changeeeginfo -f $nameFile $f >> $datePath/${subjID}_updateBioSig.log 2>&1	
done

# rename the biosignals.txt
labelName=$(echo $(basename $nameFile))
mv $nameFile ${datePath}/${subjID}_biosignals.txt
