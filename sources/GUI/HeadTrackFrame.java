/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Beth
 */
public class HeadTrackFrame extends JFrame{
    JButton doneButton;
    
    public HeadTrackFrame(){
        // Create a new frame
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        JTextArea text = new JTextArea("Head Tracking Enabled:\n\n\n"
                + "--Load study: spontaneous (Acq>File>Load study>spontaneous)\n\n"
                + "--Load protocol: headCheck.rp (Acq>File>Load protocol>headCheck.rp\n\n"
                + "--Click Acquire data\n"
                + "*it is not necessary to localize head or start saving data\n");
        doneButton = new JButton("Done");
        doneButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    exitHeadCheck();     
                }            
        });
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
        panel.add(text);
        panel.add(Box.createVerticalStrut(5));
        panel.add(doneButton);
        panel.add(Box.createVerticalStrut(5));
        
        Container contentPane = this.getContentPane(); 

        //Add tool bar
        contentPane.add(panel);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        int h = d.height/3;
        int w = d.width/4;
        this.setBounds(d.width/2, 0, w, h);
        this.setTitle("Head Tracking"); //setting the title 
        this.setVisible(true); 
    }
    
    public JButton getDoneButton(){
        return(doneButton);
    }
    public void exitHeadCheck() {
        this.dispose();
    }
    
}
