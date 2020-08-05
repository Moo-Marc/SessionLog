/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runnable;

import GUI.LogUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Beth
 */
public class HeadTrackRunnable implements Runnable{
    private JButton doneButton;
    public HeadTrackRunnable(JButton b){
        doneButton = b;
    }

    @Override
    public void run(){
        String line;
        int exitVal = 0;
        String command = "sh /usr/local/SessionLog/headtrack.sh &";
        try{
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            while((line = br.readLine()) !=null){
                LogUtils.writeToLog(line);
                System.out.println(line);     
            }
            exitVal = p.waitFor();
        }
        catch(Exception e){
            System.out.println(e.toString());
            LogUtils.writeToLog(e.toString());
        }
        
        if (exitVal == 1){
            LogUtils.writeToLog("exitval: " + String.valueOf(exitVal));
            JOptionPane.showMessageDialog(null, "Cannot start realtime monitor.\n\n"
                                              + "Be sure the Head Tracking application\n"
                                              + "is started on the Stim PC.");
        }
        doneButton.doClick();
    }
    
}
