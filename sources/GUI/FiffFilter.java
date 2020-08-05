package GUI;
import java.io.*;

/** Filter to work with JFileChooser to select text file types. **/
public class FiffFilter extends javax.swing.filechooser.FileFilter
{
  public boolean accept (File f) {
    return f.getName ().toLowerCase ().endsWith (".fif")
          || f.isDirectory ();
  }

  public String getDescription () {
    return "FIFF Files (*.fif)";
  }
} // class JavaFilter
