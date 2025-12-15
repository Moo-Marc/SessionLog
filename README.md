# SessionLog
Small java tool to take notes during MEG recordings and conveniently move files.

# History
This tool was originally designed to work with an Elekta system and therefore a
sessionLog_help file gave instructions for its original use in that context.
That version is not in this repository.

2016 It was later thorougly modified to be used with a CTF system.  This does
not seem to have been documented, but the usage is fairly intuitive.
This is the initial commit in this repository.

2018 It was further modified to run with the latest version of the CTF software
on an upgraded acquisition computer in 2018.  Those changes were minimal and
mainly related to changes in default paths for copying pictures and digitized
head shape files.  But this was done in NetBeans IDE so there are many
additional files related to that.

2025 The program was re-written from scratch, with simpler interface and to fix
the previous issue of using 100% of a CPU core to monitor the disk for new
datasets.

# License
This code is provided under the GNU LGPL 3.0 license.  See file LICENSE

JSch is Copyright (c) 2002-2015 Atsuhiko Yamanaka, JCraft,Inc.
See file JSch_LICENSE.txt

