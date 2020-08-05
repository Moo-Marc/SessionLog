package GUI;
import java.io.File;
/*
 * CurrentFile.java
 *
 * Created on July 28, 2009, 2:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author ebock
 */
public class MaxFilterFile extends File{
    private String type;
    private String maxLog;
    private String cHPIMaxLog;
    private String outName;
    private String outRunName;
    private String cHPIOutName;
    private String cHPIPos;
    private String command;
    private String cHPICommand;
    private boolean maxFilterDefault;
    private boolean maxFiltercHPI;
    private String retryCommand;
    private String retryOutName;
    
    /** Creates a new instance of MaxFilterFile */
    public MaxFilterFile(String fileName){
        super(fileName);
        String runName;
        //default type = raw
        type = "raw";
        
        String subName = getName().substring(0,getName().lastIndexOf("."));        
        // output goes into new folder 
        outName = getParent()+"/sss/"+subName+"_sss.fif";
        // if the runs have already been sorted into run folders...
        // Check for _raw
        int rawIndex = getName().lastIndexOf("_raw");
        if (rawIndex < 0){
            // the _raw was omitted by the user
            runName = getName().substring(0,getName().lastIndexOf(".fif"));
        }
        else{
            runName = getName().substring(0,rawIndex);
        }

        System.out.println("runName: "+runName);
        outRunName = getParent()+"/sss/"+runName+"/"+subName+"_sss.fif";
        // log goes into logs folder
        maxLog = getParent()+"/sss/logs/maxfilter_"+subName+".log";       
        
        /********************* Default Command ********************************/
        command = "ssh -X meg@megana1 /neuro/bin/util/maxfilter -gui -f " + getPath() + " -o "+outName+" " +
                "-ctc /neuro/databases/ctc/ct_sparse.fif -cal /neuro/databases/sss/sss_cal.dat -autobad off "; 
        
        
        /********************* Default Head Coordinates Command ***************/
        retryOutName = getParent()+"/sss/"+subName+"_defaultHead_sss.fif";
        retryCommand = "ssh -X meg@megana1 /neuro/bin/util/maxfilter -gui -f " + getPath() + " -o "+retryOutName+" " +
                "-ctc /neuro/databases/ctc/ct_sparse.fif -cal /neuro/databases/sss/sss_cal.dat -autobad off -origin 0.00 0.00 40.00 -frame head"; 
                
        if (isType(fileName, "_cHPI")){
            type = "cHPI";
            maxFilterDefault = true;
            maxFiltercHPI = true;
            cHPIMaxLog = getParent()+"/sss/logs/maxfilter_"+subName+"_cHPI.log";
            cHPIOutName = getParent()+"/sss/"+subName+"_cHPIsss.fif";
            cHPIPos = getParent()+"/"+subName+".pos";
            
            /********************** cHPI Command ******************************/
            cHPICommand = "ssh -X meg@megana1 /neuro/bin/util/maxfilter -gui -f " + getPath() + " -o "+cHPIOutName + " " +
                    "-ctc /neuro/databases/ctc/ct_sparse.fif -cal /neuro/databases/sss/sss_cal.dat -autobad off " +
                    "-trans default -movecomp -hpistep 150 -hpisubt amp -hp "+cHPIPos; 
            //System.out.println(cHPICommand);
        }
        else if (isType(fileName, "_raw")){
            type = "raw";
            maxFilterDefault = true;
            maxFiltercHPI = false;
        }
        else if (isType(fileName, "_ave")){
            type = "ave";
            maxFilterDefault = true;
            maxFiltercHPI = false;
        }
        System.out.println("Current file: "+fileName+", Type: "+type);
    }
            
    public boolean isType(String fileName, String compareString){
        int searchLength = fileName.length();
        int compareLength = compareString.length();
        boolean match = false;
        
        for (int i = 0; i<(searchLength-compareLength);i++){
            if(fileName.regionMatches(true, i, compareString, 0, compareLength)){
                match = true;
                break;
            }
        }
        return(match);
    }
    
    public String getType(){
        return(type);
    }
    
    public boolean getMaxFilterDefault(){
        return(maxFilterDefault);
    }
    
    public boolean getMaxFiltercHPI(){
        return(maxFiltercHPI);
    }
    
    public String getMaxLog(){
        return(maxLog);
    }
    
    public String getcHPIMaxLog(){
        return(cHPIMaxLog);
    }
    
    public String getOutName(){
         return(outName);
    }
    
    public String getcHPIOutName(){
        return(cHPIOutName);
    }
    
    public String getcHPIPos(){
        return(cHPIPos);
    }
    
    public String getcHPICommand(){
        return(cHPICommand);
    }
    
    public String getDefaultCommand(){
        return(command);
    }
    
    public boolean defaultOutputExists(){
        boolean stat1 = new File(outName).exists();
        boolean stat2 = new File(outRunName).exists();
        
        return(stat1 | stat2);
    }
    
    public boolean cHPIOutputExists(){
        boolean status = new File(cHPIOutName).exists();
        return(status);
    }
    
    public String getRetryCommand(){
        return(retryCommand);
    }
    
}
   
