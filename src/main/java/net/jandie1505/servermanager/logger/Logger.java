package net.jandie1505.servermanager.logger;

import net.jandie1505.servermanager.ServerManager;

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
        try {
            if (!this.logFile.exists()) {
                this.logFile.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(this.logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

            printWriter.println("[" + dateTimeFormatter.format(localDateTime) + "] " + content);

            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            this.serverManager.getTerminalConsole().printLog("LOG FILE IO EXCEPTION");
        }
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
