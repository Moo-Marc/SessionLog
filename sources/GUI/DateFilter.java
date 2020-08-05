package GUI;
import javax.swing.*;
import java.io.*;

/** Filter to find files > 30 days old. **/
public class DateFilter implements FileFilter {
    private long timeToCheck;
    private boolean compareGreater;
    
    public DateFilter(long time, boolean compare){
        this.timeToCheck = time;
        this.compareGreater = compare;
    }

    public boolean accept (File f) {
      long currentMinute = System.currentTimeMillis()/60000;
      boolean stat = false;
      // compare the file to time (in seconds)
      // If compareGreater is true, look for a change greater than the reference time
      if (compareGreater){      
          if (currentMinute - (f.lastModified()/60000) > timeToCheck){
              // this is more than 30 days old
              stat = true;
          }
      }
      else{
          if (currentMinute - (f.lastModified()/60000) < timeToCheck){
              // this is more than 30 days old
              stat = true;
          }
      }
      return(stat);
  }

    public String getDescription() {
        return("File is more than specified minutes old");
    }

}
