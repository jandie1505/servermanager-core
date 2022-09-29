package net.jandie1505.servermanager.console;

import net.jandie1505.servermanager.ServerManager;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TerminalConsole implements Runnable, CommandSender {
    private final ServerManager serverManager;
    private final LineReader lineReader;
    private Thread thread;

    public TerminalConsole(ServerManager serverManager) {
        this.serverManager = serverManager;
        this.lineReader = LineReaderBuilder.builder().build();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && Thread.currentThread() == this.thread) {
            this.serverManager.getCommandHandler().executeCommand(this, this.lineReader.readLine("console@servermanager# ").split(" "));
        }
    }

    public void start() {
        if (this.thread == null || !this.thread.isAlive()) {
            this.thread = new Thread(this);
            this.thread.setName("servermanager-console");
            this.thread.start();
        }
    }

    public void stop() {
        this.thread.interrupt();
    }

    public Thread.State getStatus() {
        if (this.thread != null) {
            return this.thread.getState();
        } else {
            return null;
        }
    }

    public void print(String out) {
        this.lineReader.printAbove(out);
    }

    public void printLog(String out) {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        this.lineReader.printAbove("[" + dateTimeFormatter.format(localDateTime) + "] " + out);
    }

    @Override
    public void respond(String out) {
        this.print(out);
    }

    @Override
    public int getPermissionLevel() {
        return Integer.MAX_VALUE;
    }
}
