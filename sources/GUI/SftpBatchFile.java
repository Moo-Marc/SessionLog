/*
 * SftpBatchFile.java
 *
 * Created on November 3, 2009, 4:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GUI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author ebock
 */
public class SftpBatchFile {
    private int nLines;
    
    public SftpBatchFile(String dataPath, String researchGroup){
        String fileToWrite = "/MEG_data/Notepad/sftp_batch";
        File folder;
        File[] listOfFiles = null;
        File runFolder;
        File[] list;
        String run;
        String dir;
        nLines = 0;
        
        try{
            PrintWriter out = new PrintWriter (new BufferedWriter(new FileWriter(fileToWrite, false)));
            String sssPath = dataPath+"/sss";
            
            /***************** Make directories and put sessionLog*************/
            // go to the research group folder
            out.println("cd "+researchGroup);
            
            // make a directory for /project/subject/date
            String[] parts = dataPath.split("/");
            int l = parts.length;
            String baseDir = parts[l-3]+"/"+parts[l-2]+"/"+parts[l-1];
            System.out.println("Base Directory: "+baseDir);
            out.println("mkdir " + baseDir);
            out.println("chmod 777 "+ baseDir);
            
            // go to that direcotry and put sessionLog.txt into it
            out.println("cd "+ baseDir);
            out.println("put "+ dataPath + "/sessionLog.txt");
            
            // make a directory for sss and go to it
            out.println("mkdir sss");
            out.println("chmod 777 sss");
            out.println("cd sss");
            out.flush();
            nLines += 7;
            /***************** find all the files *****************************/
            folder = new File(sssPath);
            listOfFiles = folder.listFiles();
            if (listOfFiles == null){
                nLines = 0;
                return;
            }
            
            /***************** Write names to batch file **********************/
            for (int i=0; i<listOfFiles.length; i++){
                if (listOfFiles[i].isFile()){ // write any sss files that are not in run folders
                    out.println("put "+ listOfFiles[i].getName());
                    nLines++;
                }
                else if (listOfFiles[i].isDirectory()){ // find run folders
                    // make a directory for the run folder
                    dir = listOfFiles[i].getName();
                    out.println("mkdir "+dir);
                    out.println("chmod 777 "+ dir);
                    out.println("cd "+ dir);
                    out.flush();
                    nLines += 3;
                    
                    run = sssPath+"/"+listOfFiles[i].getName();    
                    // find all files in that folder and put each one
                    list = null;
                    runFolder = new File(run);
                    list = runFolder.listFiles();
                    
                    // if there are file in this folder
                    if (list != null){
                        for (int j=0; j<list.length; j++){
                            if (list[j].isFile()){ // write any sss files that are not in run folders
                                out.println("put "+ run+"/"+list[j].getName());
                                nLines++;
                            }
                        }
                    }
                    out.println("cd ..");
                    out.flush();
                    nLines++;
                }
            }
            /****************** Exit ******************************************/
            out.print("bye");
            out.flush();
            out.close();  
            System.out.println("Wrote sftp batch file (nLines = "+nLines+")");
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            nLines = 0;
        }        
    }    

    public int getNumberOfLines() {
        return nLines;
    }
}
