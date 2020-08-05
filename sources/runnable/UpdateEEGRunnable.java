/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runnable;

import GUI.LogUtils;

/**
 *
 * @author Beth
 */
public class UpdateEEGRunnable implements Runnable{
    private String command;
    
    public UpdateEEGRunnable(String cmd){        
        this.command = cmd;
    }

    @Override
    public void run() {
        LogUtils.writeToLog("Starting terminal: " + command);
        try{
            Process p = Runtime.getRuntime().exec(command);
        }
        catch(Exception e){
            System.out.println(e.toString());
            LogUtils.writeToLog(e.toString());
        }
        
    }    
}

