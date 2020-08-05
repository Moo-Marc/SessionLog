package runnable;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;
/*
 * viewFileRunnable.java
 *
 * Created on July 9, 2009, 12:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author ebock
 */
public class viewFileRunnable implements Runnable{
    String viewFile;
    /** Creates a new instance of viewFileRunnable */
    public viewFileRunnable(String viewFile) {
        this.viewFile = viewFile;
    }
    
    public void run(){
        int type;
        /************************** Determine file type ***********************/
        if (viewFile.indexOf("help")>=0)
            type = 0; //help file
        else if (viewFile.indexOf(".fif")>=0)
            type = 1; //fif file
        else
            type = 2; //all other files including text files
        
        switch (type){
           /******************** View Help File *******************************/
            case 0: 
                JFrame jFrame = new JFrame();
                Container contentPane = jFrame.getContentPane();
                JEditorPane jEditorPane = new JEditorPane();

                jEditorPane.setEditable(false);
                jEditorPane.setContentType("text/html");
                java.net.URL helpURL = ClassLoader.getSystemResource(viewFile);

                if (helpURL != null){
                    try{
                        jEditorPane.setPage(helpURL);
                    }
                    catch (IOException e){
                        System.out.println(e);
                    }
                }
                else { 
                    System.out.println("Could not find file: viewFile");
                }

                JScrollPane scroller = new JScrollPane(jEditorPane);
                contentPane.add(scroller);
                jFrame.setSize(300,800);
                jFrame.setTitle("SessionLog Help");
                jFrame.setDefaultCloseOperation(jFrame.HIDE_ON_CLOSE);
                jFrame.validate();
                jFrame.setVisible(true);
                break;
           /************************ View FIFF File with MNE ******************/
            case 1:
                try{
                    System.out.println("Render with MNE: "+viewFile);
                    //Process mne = Runtime.getRuntime().exec("ssh -X meg@megneto mne_browse_raw --raw "+viewFile);
                    //int exitVal = mne.waitFor(); // wait for process to complete
                }
                catch(Exception ex){
                    System.out.println("MNE: "+ex);
                }
                break;
           /*************** View All Other Files with EMACS editor ************/
            case 2:
                try{
                    Process sav = Runtime.getRuntime().exec("emacs "+viewFile);
                    int exitVal = sav.waitFor(); // wait for process to complete
                }
                catch(Exception ex){
                    System.out.println("runtime emacs failed");
                }  
            }// end switch
        }// end run    
}
