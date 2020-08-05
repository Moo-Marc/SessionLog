/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Component;
import java.awt.Font;
import javax.swing.*;

/**
 *
 * @author Beth
 */
public class StatusPanel extends JPanel{
    public JTextArea textAreaStatus;

    public StatusPanel(){
        // set up panel
        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));


        // create text area
        JLabel statusLabel = new JLabel("Acquisition Log:");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textAreaStatus = new JTextArea("");
        textAreaStatus.setFont(new Font("Courier New", Font.PLAIN, 14));
        textAreaStatus.setLineWrap(true);
        textAreaStatus.setWrapStyleWord(true);
        textAreaStatus.setRows(8);
        JScrollPane textScroll = new JScrollPane(textAreaStatus);
        
        // add components
        add(statusLabel);
        add(textScroll);
    }

    public JTextArea getStatusArea() {
        return(textAreaStatus);
    }
    public void appendText(String text){
        textAreaStatus.append(text);
        textAreaStatus.setCaretPosition(textAreaStatus.getDocument().getLength());
    }

    public void reset(){
        textAreaStatus.setText("");
    }

}
