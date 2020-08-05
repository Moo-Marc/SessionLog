/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import runnable.viewFileRunnable;

/**
 *
 * @author Beth
 */
public class NotesPanel extends JPanel{
    public JTextArea textArea;
    public JToolBar toolBar;
    public JMenuBar menuBar;
    public JButton saveButton;
    public JButton updateNotesButton;
    private final Font datasetFont;

    public NotesPanel(){
        // Setup the panel
        setBorder(javax.swing.BorderFactory.createTitledBorder("Session Notes"));
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.setAlignmentX(LEFT_ALIGNMENT);
        // Init components
        initComponents();
        // add componenets
        //add(toolBar);
        add(new JScrollPane(textArea));
        datasetFont = new Font("Courier", Font.BOLD,14);
    }

    private void initComponents(){
        // create the text area
        textArea = new JTextArea("");
        textArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setAlignmentX(LEFT_ALIGNMENT);

        // Icons
        java.net.URL myURL;

        // Save - Button
        myURL = ClassLoader.getSystemResource("save.gif");
        ImageIcon saveIcon = new ImageIcon(myURL);
        saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    boolean go = save();
                }
        });
        
        updateNotesButton = new JButton(saveIcon);
        updateNotesButton.setToolTipText("Save");
        updateNotesButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    appendNewDataset();
                }
        });

        // NewRun - Button
        JButton newRunButton = new JButton("New Run");
        newRunButton.setToolTipText("Insert New Run Time Stamp");
        newRunButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                    newRunActionPerformed();
                }
        });

        // Comment - Button
        JButton commentButton = new JButton("Comment");
        commentButton.setToolTipText("Insert Comment Flag");
        commentButton.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    commentActionPerformed();
                }
        });

        // Cut - Button
        myURL = ClassLoader.getSystemResource("cut.gif");
        ImageIcon cutIcon = new ImageIcon(myURL);
        JButton cutButton = new JButton(cutIcon);
        cutButton.setToolTipText("Cut");
        cutButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                        textArea.cut();
                }
        });

        // Copy - Button
        myURL = ClassLoader.getSystemResource("copy.gif");
        ImageIcon copyIcon = new ImageIcon(myURL);
        JButton copyButton = new JButton(copyIcon);
        copyButton.setToolTipText("Copy");
        copyButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                        textArea.copy();
                }
        });

        // Paste - Button
        myURL = ClassLoader.getSystemResource("paste.gif");
        ImageIcon pasteIcon = new ImageIcon(myURL);
        JButton pasteButton = new JButton(pasteIcon);
        pasteButton.setToolTipText("Paste");
        pasteButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                        textArea.paste();
                }
        });

        
        /****************** Tool Bar ***************************************/
        toolBar = new JToolBar();
        toolBar.setAlignmentX(LEFT_ALIGNMENT);
        toolBar.setMargin(new Insets(0,0,0,0));
        toolBar.add(saveButton);
        toolBar.addSeparator();
        toolBar.addSeparator();
        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);
        toolBar.addSeparator();
        toolBar.add(newRunButton);
        toolBar.add(commentButton);
    }

    public JButton getSaveButton(){
        return(saveButton);
    }
    
    public JButton getUpdateNotesButton() {
        return(updateNotesButton);
    }
    
    public void clearNotesArea(){
        textArea.setText("");
    }
    
    public void openActionPerformed(){
        if(!textArea.getText().equals(""))
        {
            int option = JOptionPane.showConfirmDialog(textArea,"Do you want to save the changes ??");
            if(option == 0) {
                //to save the text into the file
                boolean go = save();
                go = open(null);
            }
            if(option == 1) {
                boolean go = open(null);
            }

        }
        else
        {
                boolean go = open(null);
        }
    }

    public void commentActionPerformed(){
        textArea.insert("> "+ DateUtils.getCurrentTime()+ " Comment: ",textArea.getCaretPosition());
        LogUtils.writeToLog("Comment");
    }

    public void newRunActionPerformed(){
        /*runNumber = runNumber + 1;
        textArea.insert("----------------------------------------\n",textArea.getCaretPosition());
        textArea.insert("*** " +  DateUtils.now() + " ***" + "\n", textArea.getCaretPosition());
        String runN = ("RUN "+runNumber+": ");
        textArea.insert(runN,textArea.getCaretPosition());
        LogUtils.writeToLog(runN);*/
    }

    public void resetActionPerformed(){
        /* kill the monitor and start it up again.
        statusUtils.setSessionComplete(true);
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            // Interrupt may be thrown manually by stop()
        }

        // Start the monitor
        statusUtils.initialize();
        Runnable r = new MonitorRunnable(DataSetPaths, textAreaStatus, exitMenuItem, statusUtils);
        Thread mr = new Thread(r);
        mr.start();
        textAreaStatus.append("Monitor Started.\n");
        textAreaStatus.setCaretPosition(textAreaStatus.getDocument().getLength());
        LogUtils.writeToLog("Monitor Started.\n");*/
    }

    public void reset(){
        textArea.setText("");
    }
    
    public String readFile (File file) {
            StringBuffer fileBuffer;
            String fileString=null;
            String line;

            try {
                FileReader in = new FileReader (file);
                BufferedReader dis = new BufferedReader (in);
                fileBuffer = new StringBuffer () ;

                while ((line = dis.readLine ()) != null) {
                        fileBuffer.append (line + "\n");
                }

                in.close ();
                fileString = fileBuffer.toString ();
            }
            catch  (IOException e ) {
                return null;
            }
            return fileString;
    } // readFile

    public boolean writeFile (File file, String dataString) {
            try {
                PrintWriter out =new PrintWriter (new BufferedWriter(new FileWriter (file)));
                out.print (dataString);
                out.flush ();
                out.close ();
            }
            catch (IOException e) {
                System.out.println(e);
                return false;
            }
            return true;
    } // writeFile

    public boolean open(File fFile){
        if (fFile == null){
            JFileChooser fch = new JFileChooser ();
            fch.setDialogTitle ("Open SessionLog File");

            // Choose only files, not directories
            fch.setFileSelectionMode ( JFileChooser.FILES_AND_DIRECTORIES);

            // Start in the MEG directory
            fch.setCurrentDirectory (new File (DataSetPaths.acqDatabase));

            // Now open chooser
            int result = fch.showOpenDialog (this);

            if (result == JFileChooser.CANCEL_OPTION) return true;
            else if (result == JFileChooser.APPROVE_OPTION) fFile = fch.getSelectedFile ();
            else return false;

        }
        // Invoke the readFile method in this class
        String file_string = readFile (fFile);

        if (file_string != null){
                textArea.setText (file_string);                
        }
        else return false;

        return true;
    }

    public boolean save() {
        File txtFile = new File(DataSetPaths.currentSessionLog);
        return writeFile (txtFile, textArea.getText ());

    } // saveFile

    public boolean saveAs() {
            JFileChooser fc = new JFileChooser ();
            File fFile = new File(DataSetPaths.currentSessionLog);

            // Start in current directory
            String cd = DataSetPaths.currentDataPath;
            if (cd == null) cd = DataSetPaths.acqDatabase;
            fc.setCurrentDirectory (new File (cd));
            fc.setSelectedFile (fFile);
            // Open chooser dialog
            int result = fc.showSaveDialog (this);

            if (result == JFileChooser.CANCEL_OPTION) {
                return false;
            } else if (result == JFileChooser.APPROVE_OPTION) {
                fFile = fc.getSelectedFile ();
                if (fFile.exists ()) {
                        int response = JOptionPane.showConfirmDialog (null,"Overwrite existing file?","Confirm Overwrite",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) return false;}
                return writeFile (fFile, textArea.getText ());
            } else {
                return false;
            }
    } // saveAs

    public void openSelectedFile(String viewFile){
       try{
           Runnable viewJob = new viewFileRunnable(viewFile);
           Thread vfr = new Thread(viewJob);
           vfr.start();

       }
       catch(Exception ex){
            System.out.println(ex);

       }// end catch

    }
    
    public void exit(){
        // If sessionLog.txt already exists, save
        String path = DataSetPaths.currentSessionLog;
        if (new File(path).exists()){
            save();
        }

        // If sessionLog.txt does not exist, saveAs
        else{
            int option = JOptionPane.showConfirmDialog(this,"Do you want to save the changes ??");
            if (option == 0){
                    saveAs();
            }
        }
    }
    
    public void appendNewDataset() {
        String dataset;
        String temp[];
        String delimiter = "\\.";
        String text = textArea.getText();
        // List all datasets
        ArrayList<String> ds = DataSetPaths.currentDataSets;
        for (int i=0; i<ds.size(); i++){
            dataset = ds.get(i);
            int ind = text.indexOf(dataset);
            int aux = dataset.indexOf("AUX");
            // check if dataset is already reported in the notes
            if (ind == -1 && aux == -1){                
                textArea.insert("\n----------------------------------------\n",textArea.getDocument().getLength());
                textArea.insert("*** " +  DateUtils.getCurrentTime() + " ***" + "\n", textArea.getDocument().getLength());
                textArea.insert(dataset+"\n",textArea.getDocument().getLength());
                // check for matching AUX file and put these together
                temp = dataset.split(delimiter);
                String auxName = temp[0]+"_AUX.ds";
                if (ds.indexOf(auxName) > -1){
                    // thre is an AUX file
                    textArea.insert(auxName+"\n",textArea.getDocument().getLength());
                }

            }
            save();
        }
    }



}
