/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runnable;

import GUI.LogUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author Beth
 */
public class TerminalCommandRunnable implements Runnable{
    private String command;
    
    public TerminalCommandRunnable(String cmd){        
            this.command = cmd;
    }

    @Override
    public void run() {
        LogUtils.writeToLog("Starting terminal: " + command);
        try{
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                    LogUtils.writeToLog(line);
                    System.out.println(line);
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
            LogUtils.writeToLog(e.toString());
        }
    }    
}
