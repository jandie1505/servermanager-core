package net.jandie1505.servermanager;

import net.jandie1505.servermanager.console.CommandHandler;
import net.jandie1505.servermanager.console.TerminalConsole;

import java.io.IOException;

public class ServerManager {
    private Thread managerThread;
    private CommandHandler commandHandler;
    private final TerminalConsole terminalConsole;

    public ServerManager() throws IOException {
        this.commandHandler = new CommandHandler(this);

        this.terminalConsole = new TerminalConsole(this);
        this.terminalConsole.start();
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public static ServerManager instance;

    public static void main(String[] args) throws IOException {
        instance = new ServerManager();
    }
}
