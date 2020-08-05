/*
 * CleanRunnable.java
 *
 * Created on October 22, 2009, 4:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package runnable;

import GUI.LogUtils;
import java.io.*;

/**
 *
 * @author ebock
 */
public class CleanRunnable implements Runnable{
    private String path;
    private int nFiles;
    /** Creates a new instance of CleanRunnable */
    public CleanRunnable(String path){
        this.path = path;
        
        String[] list = new File(path).list();
        if (list == null){
            LogUtils.writeToLog("No Files in " + path);
            return;
        }            
        // This is the number of raw files (minus the two directories and .txt file)
        nFiles = list.length-3;
    }
    
    public void run() {
        String lastline = null;
        int i=0;
        int temp = -1;
        int buff = 0;
        
        buildMEGClinicWrapperFile();
        
        try{
            PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(path+"/sss/logs/clean.log")));

            String cmd = "ssh -X meg@megneto /MEG_data/megclinic/megclinic_startup";
            String[] env = null;
            File workingDir = new File("/MEG_data/megclinic");
            
            Runtime r = Runtime.getRuntime(); //get the runtime environment
            Process proc = r.exec(cmd, env, workingDir); //run process
            
            InputStream in = proc.getInputStream();  //input stream coming from MaxFilter
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader b = new BufferedReader(isr);
            String line = null;

            while ( (line = b.readLine()) !=null){ // read each line of input stream
                lastline = line;
                out.print(line + "\n");
                out.flush();
                LogUtils.writeToLog(line + "\n");
                
                temp = line.indexOf("Processing /MEG_data"); // Raw data sets
                if (temp >= 0){
                    i = i + 1;
                    System.out.println("Processing run "+i);
                }
                temp = line.indexOf("MEGClinic Stopped"); 
                if (temp >= 0){
                    LogUtils.writeToLog("Artifact Cleaning Done");
                    proc.destroy();
                }
                
            }
            
            int exitVal = proc.waitFor(); // wait for process to complete
            
            if (exitVal > 0){
                LogUtils.writeToLog("exitVal= " + exitVal);
            }
            out.close();
        }
        catch (Exception exep){
            LogUtils.writeToLog("MEGClinic clean: " + exep.getMessage());
        }
    }
        
    public void buildMEGClinicWrapperFile(){
        PrintWriter wrapper;
        String wrapperFile = "/MEG_data/megclinic/megClinicWrapper.m";
        try {
            wrapper = new PrintWriter(new BufferedWriter(new FileWriter(wrapperFile)));
            wrapper.print("meg_clinic('clean','" + path + "')");
            wrapper.flush();
            wrapper.close();
            
            Runtime runTime = Runtime.getRuntime(); //get the runtime environment
            Process proc = runTime.exec("chmod 777 "+wrapperFile); //run process
        } 
        catch (IOException ex) {
            LogUtils.writeToLog(ex.getMessage());
        } 
         
         
    }
    
}
