/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runnable;

import GUI.AcqWorkflow;
import GUI.LogUtils;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JProgressBar;



/**
 *
 * @author Beth
 */
public class ProcessCommandRunnable implements Runnable{
    private String command;
    private JFrame frame;
    private AcqWorkflow workflow;
    
    public ProcessCommandRunnable(String cmd, AcqWorkflow wkflw){        
            this.command = cmd;
            this.workflow = wkflw;
            frame = new JFrame("Updating");
            frame.setLocationRelativeTo(workflow);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JProgressBar jProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);
            jProgressBar.setStringPainted(true);
            jProgressBar.setIndeterminate(true);
            
            frame.add(jProgressBar, BorderLayout.CENTER);
            frame.setSize(300, 60);
            frame.setVisible(true);
    }

    @Override
    public void run() {
        workflow.setUpdateInProgress(true);
        LogUtils.writeToLog("Starting terminal: " + command);
        try{

            Process p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                    LogUtils.writeToLog(line);
                    System.out.println(line);
            }
            int exitVal = p.waitFor();
            if (exitVal == 0){
                LogUtils.writeToLog("Done.");
                frame.setVisible(false);
                workflow.setUpdateInProgress(false);
            }else{
                LogUtils.writeToLog("Incomplete.  Error while executing process.");
                workflow.setUpdateInProgress(false);
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
            LogUtils.writeToLog(e.toString());
            frame.setVisible(false);
            workflow.setUpdateInProgress(false);
        }
    }    
    
}
