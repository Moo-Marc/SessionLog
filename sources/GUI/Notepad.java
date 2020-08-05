package GUI;
//import statements

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import runnable.*;


/**
 *	A public class
 */
class Notepad extends JFrame{	
    private Container contentPane;
    private StatusUtils statusUtils;

    public NotesPanel notesPanel;
    public DataTreePanel dataTreePanel;
    public StatusPanel statusPanel;
    public AcqWorkflow workflowPanel;
    public JButton uploadButton;
    public JTextField loginText;
    public JTextField logoutText;

    public int runNumber;

    public Notepad() {        
        /* Initialize all components */
        runNumber = 0;
        statusUtils = new StatusUtils();
        statusUtils.initialize();
        DataSetPaths.init();
        initComponents();

        /* Start the logging file */
        LogUtils.startLogFile();
        createDateFolder(); 
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Notepad.class.getName()).log(Level.SEVERE, null, ex);
        }
        createSessionFile();
        
        // Open the sessionLog
        notesPanel.open(new File(DataSetPaths.currentSessionLog));
        
        // Update the file tree
        dataTreePanel.updateFileTree();
        // Update session log notes
        notesPanel.updateNotesButton.doClick();
        // write the startup date time in the scan time log 
        LogUtils.writeToScanLog("\nSessionLog Start", DateUtils.getDateFolder());
        LogUtils.writeToScanLog("SessionLog Start", loginText.getText());
        
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        };
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        addWindowListener(exitListener);

    }
    
    private void createDateFolder(){
        /* check for date folder */
        String path = DataSetPaths.currentDataPath;
        String command = "mkdir "+path;
        // run the command from a terminal window
        Runnable r = new TerminalCommandRunnable(command);
        Thread cr = new Thread(r);
        cr.setName("mkdir");
        cr.start();                 
    }
    
    private void createSessionFile(){
        File dateFolder = new File(DataSetPaths.currentDataPath);
        /* Search for existing sessionLog.txt for this date */
        File sessionLog = new File(DataSetPaths.currentSessionLog);
        if (!sessionLog.exists()){
            String command = "touch "+DataSetPaths.currentSessionLog;
            // run the command from a terminal window
            Runnable r = new TerminalCommandRunnable(command);
            Thread cr = new Thread(r);
            cr.setName("touch");
            cr.start();
        }
        // wait for thread to finish
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Notepad.class.getName()).log(Level.SEVERE, null, ex);
        }
        // search for most recent and load this one
        SessionFilter filter = new SessionFilter("sessionLog");
        File[] sessionFiles = dateFolder.listFiles(filter);
        long lastMod = sessionFiles[0].lastModified();
        for (int ii=0; ii < sessionFiles.length; ii++){
            if (sessionFiles[ii].lastModified() >= lastMod){
                DataSetPaths.currentSessionLog = sessionFiles[ii].getPath();
            }
        }
    }
    
    public DataTreePanel getDataTreePanel(){
        return (dataTreePanel);
    }
    
    public NotesPanel getNotesPanel(){
        return (notesPanel);
    }
    
    public void newRecordActionPerformed(){
        // Clear textArea
        notesPanel.reset();

        // Clear textAreaStatus
        statusPanel.reset();

        // Set data path to root data path
        DataSetPaths.currentDataPath = DataSetPaths.acqDatabase;

        // Update file tree
        dataTreePanel.updateFileTree();

        // Reset run number
        runNumber = 0;

        // Reset monitor
        //resetActionPerformed();

        // Reset Status flags
        statusUtils.initialize();

        // Start new log file
        LogUtils.startLogFile();
        
    }
    
    public void setRunNumber(int number){
        runNumber = number;
    }
    
    private void initComponents(){
        // menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        fileMenu.setMnemonic('f');
        helpMenu.setMnemonic('h');

        // File Menu
        java.net.URL myURL;
        //Open - Menu Item
        myURL = ClassLoader.getSystemResource("open.gif");
        ImageIcon openIcon = new ImageIcon(myURL);
        JMenuItem openMenuItem = new JMenuItem("Open", openIcon);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));
        openMenuItem.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    notesPanel.openActionPerformed();
                }
        });
        // Save - Menu Item/
        myURL = ClassLoader.getSystemResource("save.gif");
        ImageIcon saveIcon = new ImageIcon(myURL);
        JMenuItem saveMenuItem = new JMenuItem("Save", saveIcon);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    notesPanel.save();
                }
        });
        // Save As - Menu Item
        myURL = ClassLoader.getSystemResource("save.gif");
        ImageIcon saveAsIcon = new ImageIcon(myURL);
        JMenuItem saveAsMenuItem = new JMenuItem("Save As...", saveAsIcon);
        saveAsMenuItem.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    notesPanel.saveAs();
                }
        });
        // New Record - Menu Item
        myURL = ClassLoader.getSystemResource("new.gif");
        ImageIcon newIcon = new ImageIcon(myURL);
        JMenuItem newRecordMenuItem = new JMenuItem("New", newIcon);
        newRecordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newRecordMenuItem.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    newRecordActionPerformed();
                }
        });
        // Exit - Menu Item
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    exit();
                }
        });

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(newRecordMenuItem);
        fileMenu.add(exitMenuItem);
        fileMenu.insertSeparator(3);

        //Help Menu
        JMenuItem helpMenuItem = new JMenuItem("Help");
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        helpMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                    Runnable viewJob = new viewFileRunnable(DataSetPaths.helpFileName);
                    Thread vfr = new Thread(viewJob);
                    vfr.start();
            }
        });
        helpMenu.add(helpMenuItem);

        // Create toolbar
        // Login time
        JLabel loginLabel = new JLabel("Scan Start (hh:mm):");
        loginText = new JTextField(DateUtils.getCurrentTime("HH:mm"));
        loginText.setBackground(Color.LIGHT_GRAY);
        
        JLabel logoutLabel = new JLabel("Scan End (hh:mm):");
        logoutText = new JTextField();
        logoutText.setBackground(Color.LIGHT_GRAY);
        
        JButton update = new JButton("Update Scan Time");
        update.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    LogUtils.writeToScanLog("Scan Start Update", loginText.getText());
                    LogUtils.writeToScanLog("Scan End Update", logoutText.getText());
                }
        });

        JToolBar toolBar = new JToolBar();
        toolBar.add(loginLabel);
        toolBar.addSeparator();
        toolBar.add(loginText);
        toolBar.addSeparator();
        toolBar.add(logoutLabel);
        toolBar.addSeparator();
        toolBar.add(logoutText);
        toolBar.addSeparator();
        toolBar.add(update);
        toolBar.add(Box.createHorizontalGlue());

        /*************** Add Components ***************************************/
        contentPane = getContentPane(); 

        //Add tool bar
        contentPane.add(toolBar, BorderLayout.NORTH);
        
        // Add main text area
        notesPanel = new NotesPanel();
        contentPane.add(notesPanel, BorderLayout.CENTER);

        //Set up east-side panel
        dataTreePanel = new DataTreePanel();
        contentPane.add(dataTreePanel, BorderLayout.EAST);
        
        // setup workflow panel
        workflowPanel = new AcqWorkflow();
        contentPane.add(workflowPanel, BorderLayout.WEST);
        
        // Add bottom "Status" area
        statusPanel = new StatusPanel();
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        JTextArea textArea = statusPanel.getStatusArea();
        LogUtils.setStatusArea(textArea);
        
        /*  add callbacks */
        JButton load = dataTreePanel.getLoadTextButton();
        load.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                notesPanel.open(new File(DataSetPaths.currentSessionLog));
                setNewTitle("SessionLog: " + DataSetPaths.currentSessionLog );
            }
        });
        JButton updateEEG = workflowPanel.getUpdateEEGButton();
        updateEEG.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                updateEEGPositionCallback();
            }
        });
        JButton updateBioSig = workflowPanel.getUpdateBioSigButton();
        updateBioSig.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                updateBioSigNamesCallback();
            }
        });
        // AcqComplete - Button
        JButton acqCompleteButton = workflowPanel.getAcqCompleteButton();
        acqCompleteButton.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    acqCompleteButtonActionPerformed(e);
                }
        });

    }

    public void exit(){
        notesPanel.exit();
        // check for upload still running
        int nThreads = Thread.activeCount();
        Thread[] tarray = new Thread[nThreads];
        Thread.enumerate(tarray);
        String name;
        for (int ii=0; ii<nThreads; ii++){
            name = tarray[ii].getName();
            if (name.equals("rsync")){
                return;
            }                
        }
        // upload process not found, exit
        System.exit(0);
    }

    public void acqCompleteButtonActionPerformed(java.awt.event.ActionEvent e){
        LogUtils.writeToLog("Acquisition Complete");
        statusUtils.setAcquisitionComplete(true);
        
        
        boolean k = notesPanel.save();  //save the text area
        if (k){
            LogUtils.writeToLog("Saved text area.");
        }
        
        // confirm pos file exists
        String posName = DataSetPaths.posFilePath+File.separator+workflowPanel.getPosFileName();
        if (!new File(posName).isFile()){
            // ask the user to select a pos file
            File fFile;
            JFileChooser fch = new JFileChooser ();
            fch.setDialogTitle ("Select a .pos file");

            // Choose only files, not directories
            fch.setFileSelectionMode (JFileChooser.FILES_ONLY);
            fch.setCurrentDirectory (new File (DataSetPaths.posFilePath));
            // Now open chooser
            int result = fch.showOpenDialog (this);
            if (result == JFileChooser.APPROVE_OPTION){
                fFile = fch.getSelectedFile ();
                posName = fFile.getPath();                
            }
            else if (result == JFileChooser.CANCEL_OPTION){
                posName = "";  
            }
        }
        
        // Get the picture files if available
        String picText = workflowPanel.getPicFileNames();
        String subjName = workflowPanel.getSubjName();
        
        // update end recording time
        logoutText.setText(DateUtils.getCurrentTime("HH:mm"));
        LogUtils.writeToScanLog("Acquisition Complete", logoutText.getText());

        // run the process script
        String path = DataSetPaths.currentDataPath;
        String command = "sh /usr/local/SessionLog/process.sh "+subjName+ " " +path+ " " +posName+ " " +picText;
        // run the command from a terminal window
        Runnable r = new ProcessCommandRunnable(command, workflowPanel);
        Thread cr = new Thread(r);
        cr.setName("acqComplete");
        cr.start();
        
        // clear the notes area to start over
        notesPanel.clearNotesArea();
        k = notesPanel.save();  //save the text area
        if (k){
            LogUtils.writeToLog("Saved text area.");
        }
    }
    
    public void updateEEGPositionCallback(){
        String eegName;
        LogUtils.writeToLog("UpdateEEG");        

        boolean k = notesPanel.save();  //save the text area
        if (k){
            LogUtils.writeToLog("Saved text area.");
        }
        
        String subjName = workflowPanel.getSubjName();
        eegName = DataSetPaths.posFilePath+File.separator+workflowPanel.getEEGFileName();
        if (!new File(eegName).isFile()){
            // ask the user to select a pos file
            File fFile;
            JFileChooser fch = new JFileChooser ();
            fch.setDialogTitle ("Select a .pos file");

            // Choose only files, not directories
            fch.setFileSelectionMode (JFileChooser.FILES_ONLY);
            // Start in the MEG directory
            fch.setCurrentDirectory (new File (DataSetPaths.posFilePath));
            // Now open chooser
            int result = fch.showOpenDialog (this);
            if (result == JFileChooser.APPROVE_OPTION){
                fFile = fch.getSelectedFile ();
                eegName = fFile.getPath();                
            }
            else{ 
                return;
            }
        }
        // run the changeeeginfo script
        String path = DataSetPaths.currentDataPath;
        String command = "sh /usr/local/SessionLog/updateEEG.sh "+subjName+ " " +path+ " " +eegName;
        // run the command from a terminal window
        Runnable r = new ProcessCommandRunnable(command, workflowPanel);
        Thread cr = new Thread(r);
        cr.setName("changeeeg");
        cr.start();

    }
    
    public void updateBioSigNamesCallback(){
        // create the bio sig file
        String text = workflowPanel.getBioSigText();
        String bioSigFile = DataSetPaths.currentDataPath + "/biosignals.txt";
        try{
            PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(bioSigFile, false)));
            out.print(text);
            out.flush();
            out.close();
        }
        catch (Exception ex){
            System.out.println("Write to log: " + ex);
        }
        // run the changeeeginfo script
        String subjName = workflowPanel.getSubjName();
        String path = DataSetPaths.currentDataPath;
        String command = "sh /usr/local/SessionLog/updateBioSig.sh "+subjName+ " " +path+ " "+bioSigFile;
        // run the command from a terminal window
        Runnable r = new ProcessCommandRunnable(command, workflowPanel);
        Thread cr = new Thread(r);
        cr.setName("changeBioSig");
        cr.start();
    }

    public void setNewTitle(String title){
        this.setTitle(title);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                // Create a new frame
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice[] gs = ge.getScreenDevices();

                // Start the sessionlog
                Notepad frame = new Notepad();
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension d = tk.getScreenSize();
                int h = d.height/10;
                int w = d.width/3;
                //frame.setSize(new Dimension(w,h));
                frame.setBounds(d.width/2, 0, w, h*6);
                String logPath = DataSetPaths.currentSessionLog;
                frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

                frame.setTitle("SessionLog: " + logPath ); //setting the title
                frame.setVisible(true); //showing the frame
                
                // Start the monitor
                Runnable r = new MonitorRunnable(frame.getDataTreePanel().getUpdateButton(),frame.getNotesPanel().getSaveButton(),frame.getNotesPanel().getUpdateNotesButton());
                Thread cr = new Thread(r);
                cr.setName("monitor");
                cr.start();
            }
        });
    }


}
