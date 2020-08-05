# SessionLog
Small java tool to take notes during MEG recordings and conveniently move files.

This tool was originally designed to work with an Elekta system and therefore the sessionLog_help file gives instructions for its original use in that context.

It was later thorougly modified to be used with a CTF system.  This does not seem to have been documented, but the usage is fairly intuitive.
This is the initial commit in this repository, and dates from 2016.

It was further modified to run with the latest version of the CTF software on an upgraded acquisition computer in 2018.  Those changes were minimal and mainly related to changes in default paths for copying pictures and digitized head shape files.

To do:
1. Redesign note taking so that one field is for describing artefacts common to all recordings (such as it is right now), and one is for artefacts specific to the currently selected recording, and a third is for other notes.  This will allow automatic inclusion of artefact notes when converting to BIDS.
2. Fix continuous high CPU usage.
3. Change hard coded default paths to be editable through the GUI or "last used path" remembered.
4. Clean-up old files from the Elekta version which are no longer necessary.
