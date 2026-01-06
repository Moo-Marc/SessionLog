package megsessionlog;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

//import javax.swing.JFileChooser;
public class MainFrame extends JFrame {

    private final Path acqFolder;
    private final JTextArea logArea = new JTextArea();
    private final DefaultTableModel fileTableModel = new DefaultTableModel(
            new String[]{"Name", "Modified"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // read-only
        }
    };
    private final JTable fileTable = new JTable(fileTableModel);
    private final DatasetMonitor monitor;
    private LogManager logManager;
    private final Settings settings = new Settings();
    private final String participantId;
    private Path logFile;
    private JButton saveAsBtn;

    public MainFrame() {
        super("Session Logger");

        participantId = askParticipantId();
        if (participantId == null || participantId.trim().isEmpty()) {
            System.exit(0);
        }

        String user = System.getProperty("user.name");
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String logFilename = participantId + "_ses-" + date + "_sessionLog.log";
        acqFolder = Paths.get("/exportctfmeg/data", user, "ACQ_Data", date);
        logFile = acqFolder.resolve(logFilename);

        try {
            logManager = new LogManager(logArea, logFile);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to create or access log file:\n" + ex.getMessage(),
                    "Critical Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        setTitle("Session Log: " + logFile.toAbsolutePath().toString());
        monitor = new DatasetMonitor(acqFolder, participantId, logManager);

        // set the callback so file list refreshes whenever scan runs
        monitor.setOnFolderChange(this::refreshFileList);
        fileTable.setAutoCreateRowSorter(true);

        setLayout(new BorderLayout());
        add(createToolbar(), BorderLayout.NORTH);
        logManager.addDirtyListener(dirty -> {
            if (logManager.isAutoSaveFailed()) {
                saveAsBtn.setBackground(dirty ? Color.RED : null);
            }
        });
        JScrollPane logScroll = new JScrollPane(logArea);
        JScrollPane listScroll = new JScrollPane(fileTable);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logScroll, listScroll);
        split.setDividerLocation(650); // log panel larger

        add(split, BorderLayout.CENTER);

        logArea.setEditable(true);  // user can type freely
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        // Word wrap in log text area
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        DateTimeFormatter timeFmt
                = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateTimeFmt
                = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DefaultTableCellRenderer base = new DefaultTableCellRenderer();
        fileTable.getColumnModel().getColumn(1)
                .setCellRenderer((table, value, isSelected, hasFocus, row, col) -> {

                    JLabel lbl = (JLabel) base.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, col);
                    if (value instanceof FileTime) {
                        Instant inst = ((FileTime) value).toInstant();
                        ZonedDateTime zdt = inst.atZone(ZoneId.systemDefault());

                        if (zdt.toLocalDate().equals(LocalDate.now())) {
                            lbl.setText(zdt.format(timeFmt));
                        } else {
                            lbl.setText(zdt.format(dateTimeFmt));
                        }
                    }
                    return lbl;
                });
        fileTable.getColumnModel().getColumn(0).setPreferredWidth(330); // Name
        fileTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Modified
        fileTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableRowSorter<TableModel> sorter
                = (TableRowSorter<TableModel>) fileTable.getRowSorter();
        List<RowSorter.SortKey> keys = new ArrayList<>();
        keys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(keys);
        sorter.sort();

        setSize(1100, 600);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });

        setLocationRelativeTo(null);

        monitor.start();
    }

    private JToolBar createToolbar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        ImageIcon cameraIcon = new ImageIcon(getClass().getResource("resources/Camera_1F4F7_color.png"));
        ImageIcon saveIcon = new ImageIcon(getClass().getResource("resources/Floppy_1F4BE_color.png"));
        ImageIcon settingsIcon = new ImageIcon(getClass().getResource("resources/Gear_2699_color.png"));
        ImageIcon headIcon = new ImageIcon(getClass().getResource("resources/Sunglasses_1F60E_color.png"));
        ImageIcon artefactIcon = new ImageIcon(getClass().getResource("resources/Tooth_1F9B7_color.png"));

        saveAsBtn = new JButton(
                new ImageIcon(saveIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH))
        );
        saveAsBtn.setToolTipText("Save log copy");
        saveAsBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
            chooser.setDialogTitle("Save Log Copy (if auto save error)");
            chooser.setSelectedFile(logFile.toFile()); // default current log file name

            int res = chooser.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                Path newFile = chooser.getSelectedFile().toPath();
                try {
                    Files.write(newFile, logArea.getText().getBytes(StandardCharsets.UTF_8));
                    if (logManager.isAutoSaveFailed()) {
                        logManager.markClean();
                        logFile = newFile; // update log manager or variable
                        setTitle("Session Log: " + logFile.toAbsolutePath().toString());
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Failed to save log:\n" + ex.getMessage(),
                            "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        bar.add(saveAsBtn);

        // Button to insert artefact description at top of log.
        JButton insertArtefactBtn = new JButton(
                new ImageIcon(artefactIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH))
        );
        insertArtefactBtn.setToolTipText("Artefact description (for BIDS)");
        insertArtefactBtn.addActionListener(e -> insertArtefactLine());
        bar.add(insertArtefactBtn);

        // Copy from camera 
        JButton cameraBtn = new JButton(
                new ImageIcon(cameraIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH))
        );
        cameraBtn.setToolTipText("Copy pictures");
        cameraBtn.addActionListener(e -> copyPictures(settings.getCameraPath()));
        bar.add(cameraBtn);

        // Copy digitization from local folder
        JButton copyBtn = new JButton(
                new ImageIcon(headIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH))
        );
        copyBtn.setToolTipText("Copy digitization");
        copyBtn.addActionListener(e -> copyFromFolder(settings.getDigitizationPath(), false));
        bar.add(copyBtn);

        JButton settingsBtn = new JButton(
                new ImageIcon(settingsIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH))
        );
        settingsBtn.setToolTipText("Edit defaults");
        settingsBtn.addActionListener(e -> openSettingsDialog());
        bar.add(settingsBtn);

        // refresh tree, though it's automatic every 5 seconds.
        /*        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> monitor.forceScan());
        bar.add(refresh);*/
        return bar;
    }

    private void refreshFileList() {
        SwingUtilities.invokeLater(() -> {
            if (!Files.exists(acqFolder)) {
                return;
            }

            fileTableModel.setRowCount(0); // clear
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(acqFolder)) {
                for (Path p : stream) {
                    fileTableModel.addRow(new Object[]{
                        p.getFileName().toString(),
                        Files.getLastModifiedTime(p)
                    });
                }
            } catch (IOException ignored) {
            }
        });
    }

    private String askParticipantId() {
        return JOptionPane.showInputDialog(
                this,
                "Enter Participant ID: \n(same as for MEG acquisition)",
                "Participant",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    private void insertArtefactLine() {
        SwingUtilities.invokeLater(() -> {
            String templateStart = "{ \"SubjectArtefactDescription\" : \"";
            String templateEnd = "\" }";
            String logText = logArea.getText();
            int caretPos;

            // Search for existing template line
            int existingIndex = logText.indexOf(templateStart);
            if (existingIndex >= 0) {
                // Found existing template
                int quoteIndex = logText.indexOf("\"", existingIndex + templateStart.length());
                caretPos = quoteIndex; // place caret just before the closing quote
            } else {
                // Insert new template at top
                String artefactTemplate = templateStart + templateEnd + System.lineSeparator();
                logArea.setText(artefactTemplate + System.lineSeparator() + logText);
                caretPos = templateStart.length(); // inside the quotes
            }

            // Set focus and cursor position to start typing straight away
            logArea.requestFocusInWindow();
            logArea.setCaretPosition(caretPos);

            // Ensure it's visible
            try {
                logArea.scrollRectToVisible(logArea.modelToView(caretPos));
            } catch (BadLocationException e) {
                // ignore
            }

            // Saves automatically
        });
    }

    private void copyPictures(String startFolder) {
        List<Path> copiedFiles = copyFromFolder(startFolder, true);
        // Make backup copy, encrypted for megadm only.
        Path encryptedFolder = Paths.get(settings.getPicPath());
        for (Path p : copiedFiles) {
            Path destFile = encryptedFolder.resolve(p.getFileName().toString() + ".gpg");
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "ionice", "-c", "2", "-n", "7", "nice", "-n", "10",
                        "gpg", "--encrypt", "--batch", "yes", "--trust-model", "always",
                        "--recipient", settings.getPicKey(),
                        "--output", destFile.toString(), p.toString()
                );
                pb.inheritIO(); // optional: inherit stdout/stderr
                pb.start().waitFor();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null,
                        "Failed to start encryption process: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt(); // restore interrupted status
                JOptionPane.showMessageDialog(null,
                        "Encryption was interrupted",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Only change permissions if the FS supports POSIX attributes
            try {
                if (Files.exists(destFile) && 
                        Files.getFileStore(destFile).supportsFileAttributeView(PosixFileAttributeView.class)) {
                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
                    Files.setPosixFilePermissions(destFile, perms);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to set permissions: " + destFile.getFileName(),
                        "Permission error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private List<Path> copyFromFolder(String startFolder, boolean deleteAfterCopy) {

        List<Path> copiedFiles = new ArrayList<>();

        //Path startDir = Paths.get(settings.getCameraPath());
        Path startDir = (startFolder != null && !startFolder.isEmpty())
                ? Paths.get(startFolder)
                : Paths.get("/media");

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(startDir.toFile());
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select camera files to copy");

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return copiedFiles;
        }

        File[] files = chooser.getSelectedFiles();
        if (files == null || files.length == 0) {
            return copiedFiles;
        }
        for (File f : files) {
            Path dest = acqFolder.resolve(participantId + "_" + f.getName());
            try {
                Files.copy(f.toPath(), dest,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES);
                if (deleteAfterCopy) {
                    Files.delete(f.toPath());
                }
                copiedFiles.add(dest);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to copy: " + f.getName(),
                        "Copy error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            // Only change permissions if the FS supports POSIX attributes
            try {
                if (Files.exists(dest) && 
                        Files.getFileStore(dest).supportsFileAttributeView(PosixFileAttributeView.class)) {
                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-------");
                    Files.setPosixFilePermissions(dest, perms);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to set permissions: " + dest.getFileName(),
                        "Permission error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        refreshFileList();

        StringBuilder sb = new StringBuilder("Copied files: ");
        for (int i = 0; i < files.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(participantId).append("_").append(files[i].getName());
        }
        logManager.appendLine(sb.toString(), Instant.now());

        return copiedFiles;
    }

    private void openSettingsDialog() {
        JTextField digitizationField = new JTextField(settings.getDigitizationPath(), 40);
        JTextField cameraField = new JTextField(settings.getCameraPath(), 40);
        JTextField picPathField = new JTextField(settings.getPicPath(), 40);
        JTextField picKeyField = new JTextField(settings.getPicKey(), 40);

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Digitization folder:"));
        panel.add(digitizationField);
        panel.add(new JLabel("Camera folder:"));
        panel.add(cameraField);
        panel.add(new JLabel("Encrypted pictures backup folder:"));
        panel.add(picPathField);
        panel.add(new JLabel("Encryption key:"));
        panel.add(picKeyField);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Settings",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            settings.setDigitizationPath(digitizationField.getText().trim());
            settings.setCameraPath(cameraField.getText().trim());
            settings.setPicPath(picPathField.getText().trim());
            settings.setPicKey(picKeyField.getText().trim());
            settings.save();
            dispose();
        }
    }

    private void onExit() {
        logManager.flushAndShutdown();
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
