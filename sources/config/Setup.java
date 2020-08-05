/*
 * Setup.java
 *
 * Created on March 1, 2010, 4:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package config;

/**
 *
 * @author ebock
 */
public class Setup {
    
    /** Creates a new instance of Setup */
    public static void main (String args[]){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ResearchList().buildDefaultFile();                
            }
        });
    }
    
}
