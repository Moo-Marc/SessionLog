/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a FilenameFilter
 *
 * @author Elizabeth Bock
 */
public class FileListFilter implements FilenameFilter {
    private String[] patterns;
    private String[] extension;

    /** Creates a new FileListFilter *
     * 
     * @param name {@link String} containing the name or pattern to match
     * @param extension {@link String} array of extentions to match
     */
    public FileListFilter(String[] p, String[] extension) {
        this.patterns = p;
        this.extension = extension;
    }

    @Override
    public boolean accept(File directory, String name) {
        boolean fileOK = false;
        boolean extOK = false;

        // When searching for a matching name (extension = null)
        if (patterns != null) {
            extOK = true;
            for (int j=0; j<patterns.length; j++){
                Pattern p = Pattern.compile(patterns[j]);
                Matcher m = p.matcher(name.toLowerCase());
                boolean b = m.matches();
              
                if (b){
                    fileOK = true;
                    break;
                }
            }
        }

        // When searching for a matching extension (name = null)
        if (extension != null) {
            fileOK = true;
            for (int i=0; i<extension.length; i++){
                if (name.toLowerCase().endsWith("." + extension[i])){
                    extOK = true;
                    break;
                }

            }
        }
        
        return (fileOK &= extOK);
  }

}
