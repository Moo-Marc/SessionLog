#!/bin/bash
#Usage: subjID process.sh datePath posFile

subjID=$1
datePath=$2
posFile=$3
DsFiles=$(ls -d $datePath/*.ds)

#Update EEG info for each of the .ds folders
for f in $DsFiles
do
	changeeeginfo -f $posFile $f >> $datePath/${subjID}_updateEEG.log 2>&1	
done

# if the pos file does not have a matching subject ID, 
# then prepend the ID while copying		
posName=$(echo $(basename $posFile))		
if [[ $posName == ${subjID}_* ]] ; then		
	echo "copying $posFile to $datePath"		
	cp $posFile $datePath
		
else			
	echo "copying $posFile to ${datePath}/${subjID}_$posName"		
	cp $posFile ${datePath}/${subjID}_$posName
fi
