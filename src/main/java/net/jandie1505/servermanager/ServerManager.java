package net.jandie1505.servermanager;

import net.jandie1505.servermanager.console.CommandHandler;
import net.jandie1505.servermanager.console.TerminalConsole;

public class ServerManager {
    private Thread managerThread;
    private final CommandHandler commandHandler;
    private final TerminalConsole terminalConsole;

    public ServerManager() {
        this.commandHandler = new CommandHandler(this);

        this.terminalConsole = new TerminalConsole(this);
        this.terminalConsole.start();
    }

    public void shutdown() {
        this.terminalConsole.stop();
        //this.managerThread.interrupt();
        this.terminalConsole.print("Shutdown finished");
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public static ServerManager instance;

    public static void main(String[] args) {
        instance = new ServerManager();
    }
}
