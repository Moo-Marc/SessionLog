#!/bin/bash
#Usage: process.sh subjID datePath posFile

#set -x
# inputs
subjID=$1
datePath=$2

DsFiles=$(ls -d $datePath/$subjID*.ds | grep -v 'Noise')
logFile=$(ls $datePath/sessionLog*.txt)

#Find the date folder and update owner, group and access permissions
me=$(whoami)	
chown -R $me:$me $datePath
chmod -R 775 $datePath

# check that there is a .pos file input
if [ $3 == *.pos ]
then
	posFile=$3
	#Move the selected pos file to each of the .ds folders
	for f in $DsFiles
	do
		# See if a pos file already exists
		pos=$(ls $f/*.pos)
		if [ -z "$pos" ]
		then    
			# if the pos file does not have a matching subject ID, 
			# then prepend the ID while copying		
			posName=$(echo $(basename $posFile))		
			if [[ $posName == ${subjID}_* ]] ; then		
				echo "copying $posFile to ${datePath}/$f"		
				cp $posFile $f
			else			
				echo "copying $posFile to ${datePath}/${f}/${subjID}_$posName"		
				cp $posFile ${f}/${subjID}_$posName
			fi
		
		else
			echo "$pos already exists" 
		fi
	done
done

# copy the SessionLog and prepend the ID to the date folder	
logName=$(echo $(basename $logFile))			
echo "copying $logFile to ${datePath}/${subjID}_$logName"		
mv $logFile ${datePath}/${subjID}_$logName

#Move the selected picture files to the dataset folder (datePath)
for f in "$@"
do
	if [ -f "$f" ]
	then
		fileName=$(echo $(basename $f))
		echo "copying $f to ${datePath}/${subjID}_$fileName"		
		cp $f ${datePath}/${subjID}_$fileName
	fi
done

#Check that all files have the subject ID
#extraFiles=$(ls -d $datePath/* | grep -v '.ds')
#for f in $extraFiles
#do
#	echo "$f"
#	fileName=$(echo $(basename $f))	
#	if [[ $fileName == ${subjID}_* ]] ; then
#		echo "$f has subject ID"
#	else
#		fileName=$(echo $(basename $f))
#		echo "copying $f to ${datePath}/${subjID}_$fileName"		
#		mv $f ${datePath}/${subjID}_$fileName
#	fi
#done

# rename the folder
#folderName=$(echo $(basename $datePath))
#pathName=$(echo $(dirname $datePath))
#mv $datePath ${pathName}/${folderName}_$subjID
