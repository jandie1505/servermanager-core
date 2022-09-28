package net.jandie1505.servermanager;

import net.jandie1505.servermanager.config.ConfigManager;
import net.jandie1505.servermanager.console.CommandHandler;
import net.jandie1505.servermanager.console.TerminalConsole;

import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private Thread managerThread;
    private final ConfigManager configManager;
    private final CommandHandler commandHandler;
    private final TerminalConsole terminalConsole;

    public ServerManager(String overrideConfig) {
        this.configManager = new ConfigManager(this, overrideConfig);

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

    public TerminalConsole getTerminalConsole() {
        return this.terminalConsole;
    }

    public static ServerManager instance;

    public static void main(String[] args) {
        System.out.println("ServerManager by jandie1505 (https://github.com/jandie1505)");

        int waitTime = 3;
        Map<String, String> startArguments = new HashMap<>();
        try {
            for (String arg : args) {
                if (arg.startsWith("-")) {
                    arg = arg.replace("-", "");
                    try {
                        String[] argument = arg.split("=");
                        startArguments.put(argument[0], argument[1]);
                    } catch (Exception e) {
                        System.out.println("Incorrect start argument: " + arg);
                        waitTime = 10;
                    }
                } else {
                    System.out.println("Wrong start argument format: " + arg);
                    waitTime = 10;
                }
            }
        } catch (Exception e) {
            System.out.println("Error with start arguments. Starting with default arguments...");
            waitTime = 30;
        }

        String overrideConfig = null;
        if (startArguments.containsKey("overrideConfig")) {
            overrideConfig = startArguments.get("overrideConfig");
        }

        instance = new ServerManager(overrideConfig);
    }
}
