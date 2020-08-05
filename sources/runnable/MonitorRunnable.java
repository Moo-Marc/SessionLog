package runnable;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ebock
 */
import GUI.DataSetPaths;
import GUI.FileListFilter;
import GUI.StatusUtils;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;

/* for use with java 7 when acq is upgraded
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.*;
* */


public class MonitorRunnable implements Runnable {
    private JButton updateTree;
    private JButton saveNotes;
    private JButton updateNotes;
    private Long lastTime;
    private boolean executeRsync;
    private boolean isExit;
    private FileListFilter dsFilt;
    private FileListFilter m4Filt;
           
    public MonitorRunnable(JButton update, JButton save, JButton notes){
        this.updateTree = update; 
        this.saveNotes = save;
        this.updateNotes = notes;
        lastTime = System.currentTimeMillis();
        String[] filt1 = new String[1];
        filt1[0] = "ds";
        dsFilt = new FileListFilter(null,filt1);
        String[] filt2 = new String[1];
        filt2[0] = "meg4";
        m4Filt = new FileListFilter(null,filt2);
    }
    
    public void setExit(){
        // user has indicated the desire to exit the program
        isExit = true;
    }

    @Override
    public void run() {
        File currentDS;
        boolean acqStartUp = false;
        while(true){
            if ((new File(DataSetPaths.currentDataPath).lastModified() > lastTime) || acqStartUp) {
                // find most recent dataset
                currentDS = getMostRecentDS();
                File[] m4Files = new File(DataSetPaths.currentDataPath+File.separator+currentDS.getName()).listFiles(m4Filt);
                if (m4Files.length == 0){
                    // wait for the ds to be populated
                    acqStartUp = true;
                    continue;
                }

                if (m4Files.length == 1 && m4Files[0].exists() && m4Files[0].length() > 100) {
                    acqStartUp = false;
                    System.out.println("updating tree and saving log");
                    updateTree.doClick();
                    saveNotes.doClick();
                    updateNotes.doClick();
                    lastTime = System.currentTimeMillis();
                }
            }
        }
    }
              
    private File getMostRecentDS() {
        File fl = new File(DataSetPaths.currentDataPath);
        File[] files = fl.listFiles(dsFilt);
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (int i=0; i<files.length; i++) {
            if (files[i].lastModified() > lastMod) {
                    choice = files[i];
                    lastMod = files[i].lastModified();
            }
        }
        return choice;
    }       
        /* for use with java 7 when acq is upgraded
        // create a watch service
        WatchService watcher = null;
        Path dir;
        try {
            watcher = FileSystems.getDefault().newWatchService();
            dir = FileSystems.getDefault().getPath(DataSetPaths.currentDataPath);
            dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE);
        } catch (IOException x) {
            System.err.println(x);
        }
        
        for (;;) {

            // wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
            // registered to watch for CREATE_EVENTS
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                // but an OVERFLOW event can occur if events are lost or discarded.
                if (kind == OVERFLOW) {
                    continue;
                }
               
                eventButton.doClick();
                saveButton.doClick();
            }          
            // Reset the key, if the key is no longer valid, the directory is inaccessible
            // so exit the loop.
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }*/
}// end class