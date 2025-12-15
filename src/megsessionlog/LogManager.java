package megsessionlog;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

public class LogManager {

    private boolean autoSaveFailed = false;
    private boolean logDirty = false;
    private final JTextArea area;
    private final Path logFile;
    private final DateTimeFormatter fmt
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    private final ScheduledExecutorService saver
            = Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> pendingSave;
    // This is to be able to enforce pending saves when exiting.
    private final Object saveLock = new Object();

    public void markDirty() {
        logDirty = true;
        notifyDirtyListeners();
    }

    public void markClean() {
        logDirty = false;
        notifyDirtyListeners();
    }

    public boolean isDirty() {
        return logDirty;
    }

    public interface DirtyStateListener {

        void dirtyStateChanged(boolean dirty);
    }
    private final List<DirtyStateListener> dirtyListeners = new ArrayList<>();

    public void addDirtyListener(DirtyStateListener l) {
        dirtyListeners.add(l);
    }

    private void notifyDirtyListeners() {
        boolean dirty = logDirty;
        for (DirtyStateListener l : dirtyListeners) {
            l.dirtyStateChanged(dirty);
        }
    }

    public boolean isAutoSaveFailed() {
        return autoSaveFailed;
    }

    public LogManager(JTextArea area, Path logFile) throws IOException {
        this.area = area;
        this.logFile = logFile;
        area.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                markDirty();
                scheduleSave();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markDirty();
                scheduleSave();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                markDirty();
                scheduleSave();
            }
        });
        loadIfExists();

        // Attempt initial save.
        Files.createDirectories(logFile.getParent());
        save(); // may throw IOException, will close exit in MainFrame
    }

    public void appendLine(String text, Instant time) {
        SwingUtilities.invokeLater(() -> {
            ensureBlankLine();
            area.append(fmt.format(time) + " " + text + "\n");
        });
    }

    public void appendDataset(String datasetName, Instant time) {
        appendLine(datasetName, time);
    }

    private void ensureBlankLine() {
        String text = area.getText();
        if (!text.endsWith("\n\n") && !text.isEmpty()) {
            area.append("\n");
        }
    }

    // This schedules (or re-schedules) a save after a fixed delay
    private void scheduleSave() {
        if (autoSaveFailed) {
            return;
        }

        synchronized (saveLock) {
            // Cancel any pending save, if there is one and it didn't complete yet
            if (pendingSave != null && !pendingSave.isDone()) {
                // If a new save is asked, e.g. while typing, delay save.
                // Cancel the pending save, but don't interrupt if it has started (false).
                pendingSave.cancel(false); 
            }
            // Schedule a new save after (time) delay
            pendingSave = saver.schedule(this::saveWithError, 2, TimeUnit.SECONDS);
        }
    }

    // This is to save and warn in case of error but not throwing error.
    private void saveWithError() {
        synchronized (saveLock) {
            try {
                save(); // the actual save method that throws IOException
                markClean();
            } catch (IOException ex) {
                // Critical alert during autosave: show a dialog but do NOT exit
                autoSaveFailed = true;
                // Repeat setting dirty to activate button color now that auto save has failed
                markDirty();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                        null,
                        "Failed to automatically save log file:\n" + ex.getMessage()
                        + "\nYou must now manually save the log file \nusing the 'Save As' button",
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE
                ));
            }
        }
    }

    // This does the actual saving to disk, using a temporary file to avoid
    // completely loosing log in case of error.
    private void save() throws IOException {
        Path tempFile = logFile.resolveSibling(logFile.getFileName() + ".bak");
        Files.write(tempFile, area.getText().getBytes(StandardCharsets.UTF_8));
        Files.move(tempFile, logFile,
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }

    public void flushAndShutdown() {
        synchronized (saveLock) {
            if (pendingSave != null && !pendingSave.isDone()) {
                pendingSave.cancel(false);
            }

            // Force final save if autosave is still enabled
            if (!autoSaveFailed) {
                saveWithError();
            }
            // Stop the autosave executor
            saver.shutdown();
        }
    }
    
    private void loadIfExists() {
        if (Files.exists(logFile)) {
            try {
                area.setText(new String(
                        Files.readAllBytes(logFile),
                        StandardCharsets.UTF_8));
            } catch (IOException ignored) {
            }
        }
    }
}
