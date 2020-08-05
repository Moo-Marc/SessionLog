/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runnable;

import GUI.DateUtils;
import GUI.LogUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Beth
 */
public class LoginTimeRunnable implements Runnable{
    public Long loginTime;
    
    public LoginTimeRunnable(){
    
    }
    
    public Long getLoginTime(){
        return (loginTime);
    }
    
    @Override
    public void run(){
        
        try{
            Runtime r = Runtime.getRuntime(); //get the runtime environment
            Process proc = r.exec("last -R :0 | awk '/still logged in/ {print $6}'"); //run process

            InputStream in = proc.getInputStream();  //input stream coming from sftp
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader b = new BufferedReader(isr);
            String line;
            System.out.println("Checking user login time");
            
            while ( (line = b.readLine()) !=null){ // read each line of input stream
                LogUtils.writeToLog(line+"\n");
                String[] s = line.split(":");
                loginTime = DateUtils.getDateLong(Integer.valueOf(s[0]), Integer.valueOf(s[1]));    
                System.out.print(line);
            }
            int exitVal = proc.waitFor(); // wait for process to complete
            LogUtils.writeToLog("Done, exitVal="+exitVal);
            System.out.println("Done, exitVal="+exitVal);
        }       
        catch (Exception ex){
            System.out.println("Evaluate login time failed");
            LogUtils.writeToLog("Evaluate login time failed");
        }
        
    }
    
}
