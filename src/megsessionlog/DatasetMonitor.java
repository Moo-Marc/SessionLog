package megsessionlog;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class DatasetMonitor {

    private final Path root;
    private final String participantId;
    private final LogManager logManager;
    private final Set<Path> seenDs = new HashSet<>();
    private Set<Path> lastFiles = new HashSet<>();
    private Runnable onFolderChange;

    private final ScheduledExecutorService executor
            = Executors.newSingleThreadScheduledExecutor();

    public DatasetMonitor(Path root, String participantId, LogManager logManager) {
        this.root = root;
        this.participantId = participantId;
        this.logManager = logManager;
    }

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

    private void scan() {
        if (!Files.exists(root)) {
            return; // Folder doesn’t exist yet, wait until next scheduled scan
        }
        boolean dsChanged = false;
        boolean anyChanged = false;

        Set<Path> currentFiles = new HashSet<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path p : stream) {
                currentFiles.add(p);
                String name = p.getFileName().toString();

                if (Files.isDirectory(p)
                        && name.endsWith(".ds")
                        && name.contains(participantId)
                        && !seenDs.contains(p)) {
                    seenDs.add(p);
                    dsChanged = true;
                    Instant ts = getCreationTime(p);
                    logManager.appendDataset(name, ts);
                }
            }
        } catch (IOException ignored) {
        }

        // Detect deletions
        if (!seenDs.equals(currentDsFolders(currentFiles))) {
            seenDs.retainAll(currentDsFolders(currentFiles));
            dsChanged = true;
        }

        // Update displayed file list only when something changed
        if (!currentFiles.equals(lastFiles) && onFolderChange != null) {
            onFolderChange.run();
        }
        // Store snapshot for next comparison
        lastFiles = currentFiles;
    }

// Helper: extract only the .ds folders from hash set
    private Set<Path> currentDsFolders(Set<Path> all) {
        Set<Path> ds = new HashSet<>();
        for (Path p : all) {
            String name = p.getFileName().toString();
            if (Files.isDirectory(p) && name.endsWith(".ds") && name.contains(participantId)) {
                ds.add(p);
            }
        }
        return ds;

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
