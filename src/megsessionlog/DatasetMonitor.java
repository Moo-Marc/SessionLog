package megsessionlog;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.*;

public class DatasetMonitor {

    private final Path root;
    private final String participantId;
    private final LogManager logManager;
    private final Set<Path> seenDs = new HashSet<>();
    private final Map<Path, FileTime> lastFiles = new HashMap<>();
    private Runnable onFolderChange;

    private final ScheduledExecutorService executor
            = Executors.newSingleThreadScheduledExecutor();

    public DatasetMonitor(Path root, String participantId, LogManager logManager) {
        this.root = root;
        this.participantId = participantId;
        this.logManager = logManager;
    }

    // This just has this class see the method we want to run, but it still 
    // needs to be run from this class. It's called in scan().
    public void setOnFolderChange(Runnable r) {
        this.onFolderChange = r;
    }

    public void start() {
        scan(); // initial
        // initialDelay, period for checking files
        executor.scheduleAtFixedRate(this::scan, 5, 5, TimeUnit.SECONDS);
    }

    public void forceScan() {
        executor.submit(this::scan);
    }

    // This checks for new datasets to log, and calls file list refresh when needed.
    private void scan() {
        if (!Files.exists(root)) {
            return; // Folder doesn’t exist yet, wait until next scheduled scan
        }
        Map<Path, FileTime> currentFiles = new HashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path p : stream) {
                FileTime mtime = Files.getLastModifiedTime(p);
                currentFiles.put(p, mtime);

                String name = p.getFileName().toString();
                if (isDs(p) && !seenDs.contains(p)) {
                    seenDs.add(p);
                    Instant ts = getCreationTime(p);
                    logManager.appendDataset(name, ts);
                }
            }
        } catch (IOException ignored) {
        }

        // Detect ds deletions to potentially add them to the log again if recreated.
        if (!seenDs.equals(currentDsFolders(currentFiles))) {
            seenDs.retainAll(currentDsFolders(currentFiles));
        }

        // Update displayed file list only when something changed
        if (!currentFiles.equals(lastFiles) && onFolderChange != null) {
            onFolderChange.run();
            // Store snapshot for next comparison
            lastFiles.clear();
            lastFiles.putAll(currentFiles);
        }
    }

    // Helper: extract only the .ds folders from Map hash set
    private Set<Path> currentDsFolders(Map<Path, FileTime> files) {
        Set<Path> ds = new HashSet<>();
        for (Path p : files.keySet()) {
            if (isDs(p)) {
                ds.add(p);
            }
        }
        return ds;
    }

    private Boolean isDs(Path p) {
        String name = p.getFileName().toString();
        return Files.isDirectory(p) && name.endsWith(".ds") && name.contains(participantId);
    }

    private Instant getCreationTime(Path p) {
        try {
            BasicFileAttributes attrs
                    = Files.readAttributes(p, BasicFileAttributes.class
                    );
            return attrs.creationTime().toInstant();
        } catch (Exception e) {
            try {
                return Files.getLastModifiedTime(p).toInstant();
            } catch (IOException ex) {
                return Instant.now();
            }
        }
    }
}
