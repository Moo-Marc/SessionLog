#!/bin/bash
#Usage: process.sh subjID datePath posFile

set -x
subjID=$1
datePath=$2
posFile=$3
DsFiles=$(ls -d $datePath/*.ds | grep -v 'Noise')
logFile=$(ls $datePath/sessionLog*.txt)

#Find the date folder and update owner, group and access permissions
#me=$(whoami)	
#chown -R $me:$me $datePath
#chmod -R 775 $datePath

#Make sure the pos file has the subjectID
if [ $posFile ?? $subjID ] ; then
    echo $posFile
else
    mv "$posFile" "${subjID}_$posFile"
fi

#Move the selected pos file to each of the .ds folders
for f in $DsFiles
do
	pos=$(ls $f/*.pos)
	if [ -z "$pos" ]
	then    
		echo "copying $posFile to $f"		
		cp $posFile $f
		
	else
		echo "$pos already exists" 
	fi

	echo "copying $logFile to $f"
	cp -u $logFile $f
done

#Move the selected picture files to the dataset folder (datePath)
for f in "$@"
do
	if [ -f "$f" ]&&[ "$posFile" != "$f" ]
	then
		cp $f $datePath
	fi
done

#Check that all files have the subject ID
for f in .*
do
	if [ $f ?? $subjID ]; then
		echo $f
	else
		mv "$f" "${subjID}_$f"
	fi
done
