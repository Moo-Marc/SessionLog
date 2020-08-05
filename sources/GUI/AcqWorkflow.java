/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

/**
 *
 * @author Beth
 */
public class AcqWorkflow extends JPanel{
    private final JTextField subjName;
    private final JButton newSubj;
    private final JTextArea picNames;
    private File[] picFiles;
    private final JButton selectPIC;
    private final JTextField headShapeFile;
    private final JButton selectPos;
    private final JTextField eegFile;
    private final JButton selectEEG;
    private final JButton updateEEG;
    private final JTextArea bioSigNames;
    private final JButton updateBioSig;
    private final JButton acqCompleteButton;
    
    public AcqWorkflow(){
        // set up panel
        setBorder(javax.swing.BorderFactory.createTitledBorder("Acquisition Workflow"));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX( Component.LEFT_ALIGNMENT );
        Font buttonFont = new Font("sansserif",Font.PLAIN,10);
        Dimension buttonDim = new Dimension(100, 15);
        // workflow tasks
        JLabel subjLabel = new JLabel("Recording ID");
        subjName = new JTextField("");
        subjName.setAlignmentX(Component.LEFT_ALIGNMENT);
        subjName.setMaximumSize(new Dimension(400,25));
        newSubj = new JButton("New Subject");
        newSubj.setFont(buttonFont);
        newSubj.setPreferredSize(buttonDim);
        newSubj.setAlignmentX(Component.LEFT_ALIGNMENT);
        newSubj.setToolTipText("Open a SessionLog for a new subject");
                
        JLabel picLabel = new JLabel("Coil Location Photos:");
        picNames = new JTextArea("");
        picNames.setAlignmentX(Component.LEFT_ALIGNMENT);
        picNames.setMaximumSize(new Dimension(400,25));
        picNames.setLineWrap(true);
        picNames.setWrapStyleWord(true);
        picNames.setRows(4);
        JScrollPane picScroll = new JScrollPane(picNames);
        picScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectPIC = new JButton("Select");
        selectPIC.setFont(buttonFont);
        selectPIC.setPreferredSize(buttonDim);
        selectPIC.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectPIC.setToolTipText("Select the .jpg files which indicate coil locations");
        selectPIC.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    updatePICFiles();     
                }
        });
        
        JLabel posLabel = new JLabel("Head shape file:");
        headShapeFile = new JTextField("");
        headShapeFile.setAlignmentX(Component.LEFT_ALIGNMENT);
        headShapeFile.setMaximumSize(new Dimension(400,25));
        selectPos = new JButton("Select");
        selectPos.setFont(buttonFont);
        selectPos.setPreferredSize(buttonDim);
        selectPos.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectPos.setToolTipText("Select the .pos file containing headshape points");
        selectPos.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    updatePosFile();     
                }
        });
        
        JLabel eegLabel = new JLabel("EEG electrode file:");
        eegFile = new JTextField("");
        eegFile.setAlignmentX(Component.LEFT_ALIGNMENT);
        eegFile.setMaximumSize(new Dimension(400,25));
        selectEEG = new JButton("Select");
        selectEEG.setFont(buttonFont);
        selectEEG.setPreferredSize(buttonDim);
        selectEEG.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectEEG.setToolTipText("Select the .pos file containing EEG electrode position and labels");
        selectEEG.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e){
                    updateEEGFile();     
                }
        });
        updateEEG = new JButton("Update EEG positions");
        updateEEG.setFont(buttonFont);
        updateEEG.setPreferredSize(buttonDim);
        updateEEG.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateEEG.setToolTipText("Change the EEG electrode positions and channel names");
        
        JLabel bioSigLabel = new JLabel("EKG, EOG labels:");
        bioSigNames = new JTextArea("EEG057\tECG\nEEG058\tVEOG\nEEG059\tHEOG");
        bioSigNames.setAlignmentX(Component.LEFT_ALIGNMENT);
        bioSigNames.setRows(4);
        JScrollPane bioSigScroll = new JScrollPane(bioSigNames);
        bioSigScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateBioSig = new JButton("Update");
        updateBioSig.setFont(buttonFont);
        updateBioSig.setPreferredSize(buttonDim);
        updateBioSig.setAlignmentX(Component.LEFT_ALIGNMENT);
        updateBioSig.setToolTipText("<html>Change the channel names in the dataset<br>One channel per line, tab-delimited</html>");
        
        acqCompleteButton = new JButton("Acquisition Complete");
        acqCompleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        acqCompleteButton.setToolTipText("<html>Acq session is complete<br>Headshape file will be copied into *.ds folders</html>");
 
        // add workflow buttons
        add(subjLabel);
        add(subjName);
        add(newSubj);
        add(Box.createVerticalStrut(20));
        add(picLabel);
        add(picScroll);
        add(selectPIC);
        add(Box.createVerticalStrut(20));
        add(posLabel);
        add(headShapeFile);
        add(selectPos);
        add(Box.createVerticalStrut(20));
        add(eegLabel);
        add(eegFile);
        add(selectEEG);
        add(Box.createVerticalStrut(1));
        add(updateEEG);
        add(Box.createVerticalStrut(20));
        add(bioSigLabel);
        add(bioSigScroll);
        add(updateBioSig);
        add(Box.createVerticalStrut(20));
        add(acqCompleteButton);
        add(Box.createVerticalGlue());

    }
    
    public String getSubjName(){
        return(subjName.getText());
    }
    
    public JButton getNewSubjButton(){
        return(newSubj);
    }
    
    public void setSubjName(String name){
        subjName.setText(name);
    }
    
    public String getPicFileNames(){
        return(picNames.getText());
    }
    
    public String getPosFileName(){
        return(headShapeFile.getText());
    }
    
    private void updatePICFiles(){
        JFileChooser fch = new JFileChooser ();
        fch.setDialogTitle ("Select Coil Location Photos");

        // Choose only files, not directories
        fch.setFileSelectionMode ( JFileChooser.FILES_ONLY);
        fch.setMultiSelectionEnabled(true);
        fch.setCurrentDirectory (new File (DataSetPaths.picFilePath));

        // Now open chooser
        int result = fch.showOpenDialog (this);

        if(result == JFileChooser.APPROVE_OPTION){
            picFiles = fch.getSelectedFiles();
            String s = "";
            for (File picFile : picFiles) {
                s = s + picFile.getPath() + " ";
            }
            picNames.setText(s);
        }
    }
    
    private void updatePosFile(){
        JFileChooser fch = new JFileChooser ();
        fch.setDialogTitle ("Select POS File");

        // Choose only files, not directories
        fch.setFileSelectionMode ( JFileChooser.FILES_ONLY);

        // Start in the MEG directory
        fch.setCurrentDirectory (new File (DataSetPaths.posFilePath));

        // Now open chooser
        int result = fch.showOpenDialog (this);

        if(result == JFileChooser.APPROVE_OPTION){
            File fFile = fch.getSelectedFile ();
            headShapeFile.setText(fFile.getName());          
        }
    }
    
    public String getEEGFileName(){
        return(eegFile.getText());
    }
    
    private void updateEEGFile(){
        JFileChooser fch = new JFileChooser ();
        fch.setDialogTitle ("Select EEG Electrode File");

        // Choose only files, not directories
        fch.setFileSelectionMode ( JFileChooser.FILES_ONLY);

        // Start in the MEG directory
        fch.setCurrentDirectory (new File (DataSetPaths.posFilePath));

        // Now open chooser
        int result = fch.showOpenDialog (this);

        if(result == JFileChooser.APPROVE_OPTION){
            File fFile = fch.getSelectedFile ();
            eegFile.setText(fFile.getName());          
        }
    }
    
    public JButton getUpdateEEGButton() {
        return(updateEEG);
    }
    
    public JButton getAcqCompleteButton() {
        return (acqCompleteButton);
    }
    
    public JButton getUpdateBioSigButton(){
        return (updateBioSig);
    }
    
    public String getBioSigText(){
        return (bioSigNames.getText());
    }
    
    public void setUpdateInProgress(boolean update){
        if (update == true){
            updateEEG.setEnabled(false);
            updateBioSig.setEnabled(false);
            acqCompleteButton.setEnabled(false);
        }
        else{
            updateEEG.setEnabled(true);
            updateBioSig.setEnabled(true);
            acqCompleteButton.setEnabled(true);
        }
        this.revalidate();
    }

         
}
