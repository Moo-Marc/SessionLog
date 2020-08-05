/*
 * PostResearchRunnable.java
 *
 * Created on February 25, 2010, 3:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package runnable;

import java.io.*;

/**
 *
 * @author ebock
 */
public class RsyncUpload implements Runnable{
    private String inputCommand;
    
    /** Creates a new instance of RsyncUpload */
    public RsyncUpload(String com){
        inputCommand = com;
    } 
        
    @Override
    public void run(){
        // create an rsync script to upload any changes
        String fileToWrite = "rsync.sh";
        try{
            PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(fileToWrite, false)));  
            out.print(inputCommand);
            out.flush();
            out.close();
        }
        catch (Exception ex){
            System.out.println("Write to log: " + ex);
        }

        // find folders in the date folder that belong to this user
        String command = "sh rsync.sh";
        System.out.println(command);
        // run the command
        try{
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;

            while((line = br.readLine()) !=null){
                System.out.println(line);     
            }
        }
        catch(Exception e){
            System.out.println(e.toString());        
        }
    }
}
