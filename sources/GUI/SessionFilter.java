/*
 * SessionFilter.java
 *
 * © 2012 Mcgill University, MNI-BIC
 */

package GUI;
import java.io.File;
import java.io.FilenameFilter;

/**
 * Implements a FilenameFilter
 *
 * @author Elizabeth Bock
 */
public class SessionFilter implements FilenameFilter {
    private String pattern;

    public SessionFilter(String pat) {
        this.pattern = pat;
    }

    public boolean accept(File dir, String name) {
        return name.startsWith(pattern);
  }

}
