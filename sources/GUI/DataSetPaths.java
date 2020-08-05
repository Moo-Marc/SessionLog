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
    public static String subjID;
        
    public static void init() {
        String acqPath;
        String date;
        String user;
        
        // get current user and date paths
        date = DateUtils.getDateFolder();
        //user = System.getProperty("user.home");
        user = System.getProperty("user.name");
        
        // set default paths
        acqPath = "/exportctfmeg/data/" + user + File.separator + "ACQ_Data" + File.separator + date;
            
        currentDataPath = acqPath;
        acqDatabase = acqPath;
        //currentSessionLog = acqPath + File.separator + "sessionLog_" + date + "_01.txt";
        currentSessionLog = new String();
        posFilePath = "/exportctfmeg/Pos_Files";
        picFilePath = "/media"; // /media/ECBD_C079/DCIM didn't work.
        
        helpFileName = "sessionlog_help.txt";
        
        userAppLogPath = "/exportctfmeg/data/" + user + File.separator + ".sessionLog";
        
        currentDataSets = new ArrayList<String>();
    }
}
