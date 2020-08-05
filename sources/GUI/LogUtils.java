/*
 * LogUtils.java
 *
 * Created on October 15, 2009, 9:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GUI;

import java.io.*;
import javax.swing.JTextArea;

/**
 *
 * @author ebock
 */
public class LogUtils {
    public static String logFile;
    public static String scanTimeFile;
    public static JTextArea statusArea;
    
    public static void startLogFile(){
        String record = null;
        DateFilter filt;
        File[] recent = null;
        File[] old = null;
        
        String path = DataSetPaths.userAppLogPath;
        
        // Find any records that are > 7 days (10080 min) old and delete those
        filt = new DateFilter(10080, true);
        old = new File(path).listFiles(filt);
        if (old == null){
            System.out.println("No log files to delete");
        }
        else if (old.length == 0){
            System.out.println("No log files to delete");
        }
        else{
            for (int i=0; i<old.length; i++){
                if (old[i].getName().startsWith("SessionLog")){
                    System.out.println("Deleting log file " + old[i].getName());
                    old[i].delete();
                }
            }
        }
        
        // Define and create the new log file
        logFile = path + File.separator+"SessionLog_" + System.currentTimeMillis() + ".log";
        try {
                new File(logFile).createNewFile();
        } 
        catch (IOException ex) {
                System.out.println("Create "+logFile+" failed...");
                ex.printStackTrace();
        }
        
        // Define and create the scanlog
        scanTimeFile = path + File.separator+"scanTime.log";
        try {
                new File(scanTimeFile).createNewFile();
        } 
        catch (IOException ex) {
                System.out.println("Create "+scanTimeFile+" failed...");
                ex.printStackTrace();
        }
        
        
    }

    public static void writeToLog(String line){
        String fileToWrite = logFile;
        try{
            PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(fileToWrite, true)));
            out.print(line + "\n");
            out.flush();
            out.close();
            
            updateStatusArea(line + "\n");
        }
        catch (Exception ex){
            System.out.println("Write to log: " + ex);
        }
            
    }
    
    public static void setStatusArea(JTextArea textArea){
        statusArea = textArea;
    }
    
    private static void updateStatusArea(String text){
        statusArea.append(text);
        statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }
    
    public static void writeToScanLog(String stat, String time){
        String fileToWrite = scanTimeFile;
        try{
            PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(fileToWrite, true)));
            out.print(stat + " " + time + "\n");
            out.flush();
            out.close();
            
            updateStatusArea(stat + " " + time + "\n");
        }
        catch (Exception ex){
            System.out.println("Write to log: " + ex);
        }
    }
    
}
