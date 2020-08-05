/*
 * StatusUtils.java
 *
 * Created on October 28, 2009, 9:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package GUI;

/**
 *
 * @author ebock
 */
public class StatusUtils { 
    private boolean updateEEGComplete;
    private boolean acquisitionComplete;
    private boolean cleanStarted;
    private boolean cleanComplete;
    private boolean uploadStarted;
    private boolean uploadComplete;
    private boolean emailStarted;
    private boolean emailComplete;
    private boolean sessionComplete;
    private boolean sendEmail;

    public void initialize(){
        updateEEGComplete = false;
        acquisitionComplete = false;

        cleanStarted = false;
        cleanComplete = false;
        
        uploadStarted = false;
        uploadComplete = false;
        
        emailStarted = false;
        emailComplete = false;
        sendEmail = false;
        
        sessionComplete = false;
        
    }

    public boolean isAcquisitionComplete(){
        return(acquisitionComplete);
    }
    public boolean isCleanStarted(){
        return(cleanStarted);
    }
    public boolean isCleanComplete(){
        return(cleanComplete);
    }
    public boolean isDataPostStarted(){
        return(uploadStarted);
    }
    public boolean isDataPostComplete(){
        return(uploadComplete);
    }
    public boolean isSessionComplete(){
        return(sessionComplete);
    }
    public boolean isEmailStarted(){
        return(emailStarted);
    }
    public boolean isEmailComplete(){
        return(emailComplete);
    }
    public boolean isSendEmail(){
        return(sendEmail);
    }
    public void setUpdateEEGComplete(boolean stat){
        updateEEGComplete = stat;
    }
    public void setAcquisitionComplete(boolean stat){
        acquisitionComplete = stat;
    }
    public void setCleanStarted(boolean stat){
        cleanStarted = stat;
    }
    public void setCleanComplete(boolean stat){
        cleanComplete = stat;
    }
    public void setDataPostStarted(boolean stat){
        uploadStarted = stat;
    }
    public void setDataPostComplete(boolean stat){
        uploadComplete = stat;
    }
    public void setEmailStarted(boolean stat){
        emailStarted = stat;
    }
    public void setEmailComplete(boolean stat){
        emailComplete = stat;
    }
    public void setSendEmail(boolean stat){
        sendEmail = stat;
    }
    public void setSessionComplete(boolean stat){
        sessionComplete = stat;
    }
}
