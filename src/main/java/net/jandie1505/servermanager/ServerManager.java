package net.jandie1505.servermanager;

import net.jandie1505.servermanager.bot.JDAManager;
import net.jandie1505.servermanager.config.ConfigManager;
import net.jandie1505.servermanager.console.CommandManager;
import net.jandie1505.servermanager.console.TerminalConsole;
import net.jandie1505.servermanager.database.DatabaseManager;
import net.jandie1505.servermanager.events.EventHandler;
import net.jandie1505.servermanager.events.events.StartCompleteEvent;
import net.jandie1505.servermanager.logger.Logger;
import net.jandie1505.servermanager.plugins.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class ServerManager {
    private Thread managerThread;
    private final ConfigManager configManager;
    private final CommandManager commandManager;
    private final TerminalConsole terminalConsole;
    private final Logger logger;
    private final DatabaseManager databaseManager;
    private final JDAManager jdaManager;
    private final PluginManager pluginManager;
    private final EventHandler eventHandler;

    public ServerManager(String overrideConfig) {
        // -- INIT --

        this.configManager = new ConfigManager(this, overrideConfig);

        this.commandManager = new CommandManager(this);

        this.terminalConsole = new TerminalConsole(this);

        this.logger = new Logger(this);

        this.logger.info("---- STARTING SERVERMANAGER ----");

        // Database Manager
        this.databaseManager = new DatabaseManager(this);
        this.logger.info("Created database manager");

        // Event Handler
        this.eventHandler = new EventHandler(this);
        this.logger.info("Created event handler");

        // JDA Manager
        this.jdaManager = new JDAManager(this);
        this.logger.info("Created JDA manager");

        // Plugin Manager
        this.pluginManager = new PluginManager(this);
        this.logger.info("Created plugin manager");

        // -- STARTING --

        // Start Terminal Console
        this.terminalConsole.start();
        this.logger.info("Terminal console started");

        // Setup system commands
        this.commandManager.setupSystemCommands();
        this.logger.info("System commands loaded");

        // Loading plugins
        this.pluginManager.loadPlugins();
        this.logger.info("Loaded plugins");

        // Run last
        this.eventHandler.fireSMEvent(new StartCompleteEvent(this));
        this.logger.info("STARTUP COMPLETE");
    }

    public void shutdown() {
        Thread thread = new Thread(() -> {
            int time = 60;
            while (true) {
                if (time <= 0) {
                    try {
                        this.logger.warning("Calling System.exit because normal shutdown is not working");
                    } catch (Exception ignored) {
                        // ignored
                    }
                    System.exit(1);
                }
                time--;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception ignored) {
                    // ignored
                }
            }
        });
        thread.setName("servermanager-shutdownenforcer");
        thread.setDaemon(true);
        thread.start();
        this.logger.info("Shutting down...");
        this.pluginManager.disableAll();
        this.terminalConsole.stop();
        //this.managerThread.interrupt();
        this.terminalConsole.printLog("---- SHUTDOWN COMPLETED ----");
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public CommandManager getCommandHandler() {
        return this.commandManager;
    }

    public TerminalConsole getTerminalConsole() {
        return this.terminalConsole;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public JDAManager getJdaManager() {
        return this.jdaManager;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public EventHandler getEventHandler() {
        return this.eventHandler;
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
