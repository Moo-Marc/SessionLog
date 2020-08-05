/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import runnable.TerminalCommandRunnable;
/* for use with java 7 when acq is upgraded
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
/**
 *
 * @author Beth
 */
public class DataTreePanel extends JPanel{
    public JTree patientFileTree;
    public DefaultTreeModel fileTreeModel;
    private DefaultMutableTreeNode selectedNode;
    public JButton refreshTreeButton;
    public JButton changeDirButton;
    private JPopupMenu treePopUp;
    private JMenuItem locateFileMenuItem;
    private JMenuItem openEditorMenuItem;
    private JMenuItem openAveragerMenuItem;
    private JMenuItem loadLogMenuItem;
    public JButton loadTextButton;
    public ArrayList<String> dataSets;

    public DataTreePanel(){
        initComponents();   
    }
    
    private void initComponents(){
        treePopUp = new JPopupMenu();
        locateFileMenuItem = new JMenuItem();
        openEditorMenuItem = new JMenuItem();
        openAveragerMenuItem = new JMenuItem();
        loadLogMenuItem = new JMenuItem();
        patientFileTree = new JTree();
        selectedNode = new DefaultMutableTreeNode();
        loadTextButton = new JButton();
        dataSets = new ArrayList<String>();
        
        // Set panel properties
        setBorder(javax.swing.BorderFactory.createTitledBorder("New Data Files"));
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.setAlignmentX(LEFT_ALIGNMENT);

        // tree popup menu
        // menu item for opening the data set with the file explorer
        locateFileMenuItem.setText("Open with Nautilus file manager");
        locateFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               locateFileMenuItemActionPerformed(evt);
            }
        });
        treePopUp.add(locateFileMenuItem);
        
        treePopUp.add(new JSeparator());
        // menu item for opening the dataset with the data editor
        openEditorMenuItem.setText("Open with DataEditor");
        openEditorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               openEditorMenuItemActionPerformed(evt);
            }
        });
        treePopUp.add(openEditorMenuItem);
             
        // menu item for opening the dataset with the data editor
        openAveragerMenuItem.setText("Open with DataAverager");
        openAveragerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               openAveragerMenuItemActionPerformed(evt);
            }
        });
        treePopUp.add(openAveragerMenuItem);
               
        treePopUp.add(new JSeparator());
        // menu item for opening the dataset with the data editor
        loadLogMenuItem.setText("Load SessionLog");
        loadLogMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               loadLogMenuItemActionPerformed(evt);
            }
        });
        treePopUp.add(loadLogMenuItem);
        
        // setup tree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Subject Data");
        fileTreeModel = new DefaultTreeModel(root);
        fileTreeModel.addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
            }
            @Override
            public void treeNodesInserted(TreeModelEvent e) {                
            }
            @Override
            public void treeNodesRemoved(TreeModelEvent e) {                
            }
            @Override
            public void treeStructureChanged(TreeModelEvent e) {
            }
        });
        patientFileTree.setModel(fileTreeModel);
        patientFileTree.setComponentPopupMenu(treePopUp);
        patientFileTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                patientFileTreeValueChanged(evt);
            }
        });
        patientFileTree.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                
            }

            @Override
            public void mousePressed(MouseEvent evt) {
                TreePath selPath = patientFileTree.getPathForLocation(evt.getX(), evt.getY());
                if (selPath == null){
                    
                }else{
                    patientFileTree.setSelectionPath(selPath);
                }
            }
            @Override
            public void mouseReleased(MouseEvent evt) {
                TreePath selPath = patientFileTree.getPathForLocation(evt.getX(), evt.getY());
                if (selPath == null){
                    
                }else{
                    patientFileTree.setSelectionPath(selPath);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
            
        });
        JScrollPane treeView = new JScrollPane();
        treeView.setViewportView(patientFileTree);

        // create a new panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
        
        // create a refresh button for the file tree
        refreshTreeButton = new JButton("Refresh");
        refreshTreeButton.setPreferredSize(new Dimension(20, 10));
        refreshTreeButton.setFont(new Font("sansserif",Font.PLAIN,8));
        refreshTreeButton.setAlignmentX(LEFT_ALIGNMENT);
        refreshTreeButton.setToolTipText("Refresh");
        refreshTreeButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    updateFileTree();
                    
                }
        });
        // create a change dir button for the file tree
        changeDirButton = new JButton("Change directory");
        changeDirButton.setPreferredSize(new Dimension(20, 10));
        changeDirButton.setFont(new Font("sansserif",Font.PLAIN,8));
        changeDirButton.setAlignmentX(LEFT_ALIGNMENT);
        changeDirButton.setToolTipText("Change to a new folder and display owner's contents");
        changeDirButton.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    changeDirectory();
                    
                }
        });
        // add buttons to panel
        buttonPanel.add(refreshTreeButton);
        //buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(changeDirButton);

        // Add the tree and button
        add(buttonPanel);
        add(treeView);      
    }

    private void patientFileTreeValueChanged(TreeSelectionEvent evt){
         selectedNode = (DefaultMutableTreeNode)patientFileTree.getLastSelectedPathComponent();

        // Update the visible menu options
        if (selectedNode == null){
            return;
        }
        
        // diable the commandline function calls until check is performed, below
        openEditorMenuItem.setEnabled(false);
        openAveragerMenuItem.setEnabled(false);
        loadLogMenuItem.setEnabled(false);
        
        String nodeName = selectedNode.getUserObject().toString();
        // check for ds folder
        int ind = nodeName.lastIndexOf(".");
        if (ind > 0){
            // this file name has an extension
            String ext = nodeName.substring(ind);
            if (ext.equals(".ds")){
                // this is a .ds folders and can be opened from commandline function
                openEditorMenuItem.setEnabled(true);
                openAveragerMenuItem.setEnabled(true);               
            }
        }
        // check for sessionlog file
        if (nodeName.contains("sessionLog")){
            loadLogMenuItem.setEnabled(true);
        }
    }

    public JButton getUpdateButton(){
        return(refreshTreeButton);
    }

    public void updateFileTree(){
        DefaultMutableTreeNode fileNode;
        DefaultMutableTreeNode folderNode;
        File[] listOfFiles;
        File[] folderNames;
        String name;
        File newFile;
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)fileTreeModel.getRoot();

        /**************Remove all children from patient data ******************/
        dataSets.clear();
        root.removeAllChildren();
        fileTreeModel.setRoot(new DefaultMutableTreeNode(DataSetPaths.currentDataPath));
        fileTreeModel.reload();

        File selectedPath = new File(DataSetPaths.currentDataPath);
        if (!new File(DataSetPaths.currentDataPath).exists()){
            // the folder does not yet exist...update later
            return;
        }

        /* List .ds folders and files in the data path */
        folderNames = selectedPath.listFiles();
        
        if (folderNames.length > 0){
            for (int i=0; i<folderNames.length; i++){
                name = folderNames[i].getName();
                // add ds names to current list of datasets
                if (name.endsWith("ds")){
                    dataSets.add(name);
                }
                folderNode = new DefaultMutableTreeNode(name);
                addFileTreeNode(folderNode, null);
                // find and create nodes for the files in the folder
                listOfFiles = folderNames[i].listFiles();
                if (listOfFiles != null){
                    for (int ii=0; ii<listOfFiles.length; ii++){
                        newFile = listOfFiles[ii];
                        name = newFile.getName();
                        if(newFile.isFile()){
                            // create a tree node for this file
                            fileNode = new DefaultMutableTreeNode(name);
                            addFileTreeNode(fileNode, folderNode);
                        }
                    }
                }
            }
        }
        this.revalidate();
        DataSetPaths.currentDataSets = dataSets;
    }

    private String[] getDsFolderNames(){
        ArrayList<String> folderNames = new ArrayList<String>();
        String user = System.getProperty("user.name");
        // create the custom script for linux OS
        String fileToWrite = "listfiles.sh";
        try{
            PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(fileToWrite, false)));
            out.print("find "+DataSetPaths.currentDataPath+" -user "+user+" -maxdepth 1\n");
            out.flush();
            out.close();
        }
        catch (Exception ex){
            System.out.println("Write to log: " + ex);
        }
        
        // find folders in the date folder that belong to this user
        String command = "./listfiles.sh";
        System.out.println(command);
        // run the command
        try{
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;

            while((line = br.readLine()) !=null){
            System.out.println(line);
                if (line.endsWith(".ds")){ // only include the ds folder names
                    folderNames.add(new File(line).getName());
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
            // if the sort does not work, just display all folders
            File[] listOfFiles = new File(DataSetPaths.currentDataPath).listFiles();
                if (listOfFiles != null){
                    for (int ii=0; ii<listOfFiles.length; ii++){
                        folderNames.add(listOfFiles[ii].getName());
                    }
                }
        }
        
        String[] names = new String[folderNames.size()];
        return (folderNames.toArray(names));
    }
    
    public void addFileTreeNode(DefaultMutableTreeNode newChild, DefaultMutableTreeNode parent){
        if (parent == null)
            parent = (DefaultMutableTreeNode)fileTreeModel.getRoot();

        fileTreeModel.insertNodeInto(newChild, parent, parent.getChildCount());
        if (parent == (DefaultMutableTreeNode)fileTreeModel.getRoot()){
            patientFileTree.makeVisible(new TreePath(newChild.getPath()));
        }
    }

    public void changeDirectory(){
        // open a dialog box for user to choose date folder
        File fFile;
        JFileChooser fch = new JFileChooser ();
        fch.setDialogTitle ("Open Data Directory");

        // Choose only files, not directories
        fch.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
        // Start in the MEG directory
        fch.setCurrentDirectory (new File (DataSetPaths.acqDatabase));
        // Now open chooser
        int result = fch.showOpenDialog (this);
        if (result == JFileChooser.APPROVE_OPTION){
            fFile = fch.getSelectedFile ();
            DataSetPaths.currentDataPath = fFile.getPath();
            this.updateFileTree();
        }
        else {
            return;
        }
        
        // search for most recent and load this one
        File dateFolder = new File(DataSetPaths.currentDataPath);
        SessionFilter filter = new SessionFilter("sessionLog");
        File[] sessionFiles = dateFolder.listFiles(filter);
        if (sessionFiles == null){
            String date = DateUtils.getDateFolder();
            DataSetPaths.currentSessionLog = DataSetPaths.currentDataPath + File.separator + "sessionLog_" + date + "_01.txt";
            return;
        }
        
        long lastMod = sessionFiles[0].lastModified();
        for (int ii=0; ii < sessionFiles.length; ii++){
            if (sessionFiles[ii].lastModified() >= lastMod){
                DataSetPaths.currentSessionLog = sessionFiles[ii].getPath();
            }
        }
        loadTextButton.doClick();
    }
    
    private void locateFileMenuItemActionPerformed(ActionEvent evt) {
        // Start the file explorer
        Object[] selectedFile = selectedNode.getUserObjectPath();
        String fileToOpen = (String)selectedFile[0];
        for (int i=1; i<selectedFile.length; ++i){
            fileToOpen = fileToOpen + File.separator + selectedFile[i];
        }
                
        String command = "nautilus " + fileToOpen;
        Runnable r = new TerminalCommandRunnable(command);
        Thread cr = new Thread(r);
        cr.setName("nautilus");
        cr.start();
    }
    
    private void openEditorMenuItemActionPerformed(ActionEvent evt) {
        // Start the DataEditor
        Object[] selectedFile = selectedNode.getUserObjectPath();
        String fileToOpen = (String)selectedFile[0];
        for (int i=1; i<selectedFile.length; ++i){
            fileToOpen = fileToOpen + File.separator + selectedFile[i];
        }
                
        String command = "DataEditor -dataset " + fileToOpen;
        Runnable r = new TerminalCommandRunnable(command);
        Thread cr = new Thread(r);
        cr.setName("dataeditor");
        cr.start();
    }
    
    private void openAveragerMenuItemActionPerformed(ActionEvent evt) {
        // Start the Averager
        Object[] selectedFile = selectedNode.getUserObjectPath();
        String fileToOpen = (String)selectedFile[0];
        for (int i=1; i<selectedFile.length; ++i){
            fileToOpen = fileToOpen + File.separator + selectedFile[i];
        }
                
        String command = "Averager -dataset " + fileToOpen;
        Runnable r = new TerminalCommandRunnable(command);
        Thread cr = new Thread(r);
        cr.setName("averager");
        cr.start();
    }
     
    private void loadLogMenuItemActionPerformed(ActionEvent evt) {
        // Start the Averager
        Object[] selectedFile = selectedNode.getUserObjectPath();
        String fileToOpen = (String)selectedFile[0];
        for (int i=1; i<selectedFile.length; ++i){
            DataSetPaths.currentSessionLog = fileToOpen + File.separator + selectedFile[i];
        }
        loadTextButton.doClick();
    }
    
    public JButton getLoadTextButton(){
        return (loadTextButton);
    }
}
