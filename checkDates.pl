#!/path/to/PERL/bin/perl

###########################################################
## Programmer: Ryan Admire                                #
## File: checkDates.pl                                    #
## Description: Reads items and their creation date from  #
##              the cleanlist.tmp file. Then determines   #
##              the age of the items and if they are older#
##              than the desired retention period they are#
##              added to another file marking them for    #
##              removal.                                  #
###########################################################

use strict;
use Time::Local;

my $inputFile = "/path/to/file/cleanlist.tmp";
my $cleanFile = "/path/to/file/cleanup";

my $now    = time();                #Current time
my $maxAge = 60 * 60 * 24 * 14;     #Max allowed age in seconds


open (OUT, ">>$cleanFile") || die "Couldn't open file for writing\n"; #Open file for writing. Items in this file are marked for deletion
printf OUT ("Name\tGroup\tDateStarted\tDaysOld\n");                   #Output a tab delimited header for the new file


open (INPUT, "$inputFile") || die "Couldn't open file for reading\n";                  #Open the input file for reading.
while (<INPUT>){                                                                       #While the file is not at its end
   if (/^(\S+)\s+(\S+)\s+(\S+)\s+(\S+\s+\S+)\s+\d+\s*$/) {                             #If the line matches the "Location Group Name Timestamp" pattern that is expected 
        my ($location,$group,$name,$timestamp) = ($1,$2,$3,&parse_timestamp($4));      #Store the values. The timestamp will be converted into epoch time.

		if ($timestamp < ($now - $maxAge)){                                            #If the items creation time is less than now minus the maximum age
            my $daysOld = (($now - $timestamp) / 60 / 60 / 24);                        #Age of the item in days rather than epoch time (seconds * (1 min/60 sec) * (1 hour/60 min) * (1 day/24 hours))              
            printf OUT ("%s\t%s\t%s\t%s\t%1.1f\n",$location,$group,$name,$4,$daysOld); #Output item info to a file delimited with tabs
        }
    }
}

#Close the files that were opened
close (SNAPS);
close (OUT);

### Subroutines ###---------------------------------------------------------------------------------

#will be used to convert a timestamp in YYYY-MM-DD-hh:mm:ss into epoch time
sub parse_timestamp
{
   my ($timestamp) = @_;

   my ($mon,$dd,$hh,$mm,$ss,$yyyy);

   if ($timestamp =~ /^\s*(\d{4})-(\d{2})-(\d{2})\s(\d{2}):(\d{2}):(\d{2})\s*$/) { #if timestamp is in the expected format
      ($yyyy,$mon,$dd,$hh,$mm,$ss) = ($1,$2,$3,$4,$5,$6);                          #pull each piece out into its own variable
   }

   my $epochtime = timelocal($ss,$mm,$hh,$dd,$mon - 1,$yyyy);                      #utilize timelocal to convert passed in values to epoch time

   return $epochtime;                                                              #return the value
}
