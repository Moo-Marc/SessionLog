package GUI;

import java.io.File;
import java.util.ArrayList;

/*
 * DefaultFilePaths.java
 *
 * Created on July 9, 2009, 3:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author ebock
 */
public class DataSetPaths {

    public static String acqDatabase;
    public static String currentDataPath;
    public static String currentSessionLog;
    public static String helpFileName;
    public static String posFilePath;
    public static String picFilePath;
    public static String userAppLogPath;
    public static ArrayList<String> currentDataSets;
        
    public static void init() {
        String acqPath;
        String date;
        String user;
        
        // get current user and date paths
        date = DateUtils.getDateFolder();
        user = System.getProperty("user.home");
        
        // set default paths
        if (user.equals("C:\\Users\\Beth")){
            acqPath = "D:\\MEG_data\\timing_tests\\20111213";
        }else {
            acqPath = user + File.separator + "MEG_data" +File.separator + date;
        }    
        currentDataPath = acqPath;
        acqDatabase = acqPath;
        currentSessionLog = acqPath + File.separator + "sessionLog_" + date + "_01.txt";
        posFilePath = "/POS_files";
        picFilePath = "/misc/usb/dcim";
        
        helpFileName = "sessionlog_help.txt";
        
        userAppLogPath = user + File.separator + ".sessionLog";
       
        currentDataSets = new ArrayList<String>();
    }
}
