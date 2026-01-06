package megsessionlog;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class Settings {

    private static final String DIR_NAME = ".acqlog";
    private static final String FILE_NAME = "settings.properties";

    private final Properties props = new Properties();
    private final Path settingsFile;

    public Settings() {
        Path jarDir;
        try {
            String path = MainFrame.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();
            jarDir = Paths.get(path).getParent();
        } catch (Exception e) {
            jarDir = Paths.get(".");
        }

        settingsFile = jarDir.resolve("settings.properties");

        if (Files.exists(settingsFile)) {
            try (InputStream in = Files.newInputStream(settingsFile)) {
                props.load(in);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getDigitizationPath() {
        return props.getProperty("digitization.path", "/exportctfmeg/Pos_Files");
    }
    public void setDigitizationPath(String path) {
        props.setProperty("digitization.path", path);
    }

    public String getCameraPath() {
        return props.getProperty("camera.path",
                "/media/5839-08C7/DCIM/Camera");
    }
    public void setCameraPath(String path) {
        props.setProperty("camera.path", path);
    }

    public String getPicPath() {
        return props.getProperty("pictures.path",
                "/exportctfmeg/secure/encrypted");
    }
    public void setPicPath(String path) {
        props.setProperty("pictures.path", path);
    }

    public String getPicKey() {
        return props.getProperty("pictures.key",
                "F5B6CB154448C91A");
    }
    public void setPicKey(String path) {
        props.setProperty("pictures.key", path);
    }

    public void save() {
        try (OutputStream out = Files.newOutputStream(settingsFile)) {
            props.store(out, "Session Log Settings");
        } catch (IOException ex) {
            System.err.println("Settings save failed: " + ex.getMessage());
        }
    }
}
