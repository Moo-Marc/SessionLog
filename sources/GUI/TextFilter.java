package GUI;
import javax.swing.*;
import java.io.*;

/** Filter to work with JFileChooser to select text file types. **/
public class TextFilter extends javax.swing.filechooser.FileFilter
{
  public boolean accept (File f) {
    return f.getName ().toLowerCase ().endsWith (".txt")
          || f.isDirectory ();
  }
 
  public String getDescription () {
    return "Text files (*.txt)";
  }
} // class JavaFilter
