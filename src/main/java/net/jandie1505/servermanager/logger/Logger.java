package net.jandie1505.servermanager.logger;

import net.jandie1505.servermanager.ServerManager;
import net.jandie1505.servermanager.events.events.LogEvent;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private final ServerManager serverManager;
    private final File logFile;

    public Logger(ServerManager serverManager) {
        this.serverManager = serverManager;
        this.logFile = new File(System.getProperty("user.dir"), "log.txt");
    }

    public void info(String content) {
        this.logEntry("[INFO] " + content);
    }

    public void warning(String content) {
        this.logEntry("[WARNING] " + content);
    }

    public void error(String content) {
        this.logEntry("[ERROR] " + content);
    }

    private void logEntry(String content) {
        this.serverManager.getTerminalConsole().printLog(content);

        LocalDateTime localDateTime = LocalDateTime.now();

        this.fileLogEntry(localDateTime, content);

        if (this.serverManager.getEventHandler() != null) {
            this.serverManager.getEventHandler().fireSMEvent(new LogEvent(this, localDateTime, content));
        }
    }

    private void fileLogEntry(LocalDateTime timestamp, String content) {
        try {
            if (!this.logFile.exists()) {
                this.logFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(this.logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

            printWriter.println("[" + dateTimeFormatter.format(timestamp) + "] " + content);

            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            this.serverManager.getLogger().error("IO Exception while writing log file");
        }
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
