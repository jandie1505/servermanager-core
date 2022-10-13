package net.jandie1505.servermanager.console;

import net.jandie1505.consolecommandapi.builder.CommandAPICommandBuilder;
import net.jandie1505.consolecommandapi.builder.CommandAPICommandHandlerBuilder;
import net.jandie1505.consolecommandapi.builder.CommandAPIOptionBuilder;
import net.jandie1505.consolecommandapi.command.CommandAPICommand;
import net.jandie1505.consolecommandapi.command.CommandAPICommandHandler;
import net.jandie1505.consolecommandapi.enums.CommandAPIOptionType;
import net.jandie1505.consolecommandapi.executors.CommandAPIPermissionRequest;
import net.jandie1505.servermanager.ServerManager;
import net.jandie1505.servermanager.plugins.PluginHandler;
import org.json.JSONException;

import java.util.List;

public final class CommandManager {
    private final ServerManager serverManager;
    private final CommandAPICommandHandler commandHandler;

    public CommandManager(ServerManager serverManager) {
        this.serverManager = serverManager;

        this.commandHandler = new CommandAPICommandHandlerBuilder()
                .executesCommandNotFound(result -> ((CommandSender) result.getSender()).respond("Command not found"))
                .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                .build();
    }

    public void setupSystemCommands() {
        // SHUTDOWN COMMAND
        this.commandHandler.addCommand("shutdown",
                new CommandAPICommandBuilder()
                        .executes(result -> {
                            ((CommandSender) result.getSender()).respond("Shutting down...");
                            this.serverManager.shutdown();
                        })
                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                        .build()
        );

        // HELP COMMAND
        this.commandHandler.addCommand("help",
                new CommandAPICommandBuilder()
                        .executes(result -> {
                            ((CommandSender) result.getSender()).respond("SERVERMANAGER HELP:\n" +
                                    "shutdown - shut down the bot\n");
                        })
                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                        .build()
        );

        // CONFIG COMMAND
        this.commandHandler.addCommand("config",
                new CommandAPICommandBuilder()
                        .executes(result -> {
                            ((CommandSender) result.getSender()).respond("COMMAND USAGE:\n" +
                                    "config get <key>\n" +
                                    "config set <key> <value>\n" +
                                    "config list\n" +
                                    "config load\n");
                        })
                        .withSubcommand("list",
                                new CommandAPICommandBuilder()
                                        .executes(result -> ((CommandSender) result.getSender()).respond(this.serverManager.getConfigManager().getConfig().toString(4)))
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .withSubcommand("get",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            try {
                                                ((CommandSender) result.getSender()).respond("{\"" + result.getOptions().get(0).getAsString() + "\":\"" + this.serverManager.getConfigManager().getConfig().get(result.getOptions().get(0).getAsString()) + "\"}");
                                            } catch (JSONException e) {
                                                ((CommandSender) result.getSender()).respond("Key not found");
                                            }
                                        })
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config get <key>"))
                                        .setOption(
                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                        .setRequired(true)
                                                        .build()
                                        )
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .withSubcommand("set",
                                new CommandAPICommandBuilder()
                                        .executes(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .withSubcommand("INTEGER",
                                                new CommandAPICommandBuilder()
                                                        .executes(result -> {
                                                            this.serverManager.getConfigManager().getConfigObject().put(result.getOptions().get(0).getAsString(), result.getOptions().get(1).getAsInteger());
                                                            ((CommandSender) result.getSender()).respond("Updated value");
                                                        })
                                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                                        .setOption(
                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                        .setRequired(true)
                                                                        .setNextOption(
                                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.INTEGER)
                                                                                        .setRequired(true)
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                                        .build()
                                        )
                                        .withSubcommand("LONG",
                                                new CommandAPICommandBuilder()
                                                        .executes(result -> {
                                                            this.serverManager.getConfigManager().getConfigObject().put(result.getOptions().get(0).getAsString(), result.getOptions().get(1).getAsLong());
                                                            ((CommandSender) result.getSender()).respond("Updated value");
                                                        })
                                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                                        .setOption(
                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                        .setRequired(true)
                                                                        .setNextOption(
                                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.LONG)
                                                                                        .setRequired(true)
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                                        .build()
                                        )
                                        .withSubcommand("FLOAT",
                                                new CommandAPICommandBuilder()
                                                        .executes(result -> {
                                                            this.serverManager.getConfigManager().getConfigObject().put(result.getOptions().get(0).getAsString(), result.getOptions().get(1).getAsFloat());
                                                            ((CommandSender) result.getSender()).respond("Updated value");
                                                        })
                                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                                        .setOption(
                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                        .setRequired(true)
                                                                        .setNextOption(
                                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.FLOAT)
                                                                                        .setRequired(true)
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                                        .build()
                                        )
                                        .withSubcommand("DOUBLE",
                                                new CommandAPICommandBuilder()
                                                        .executes(result -> {
                                                            this.serverManager.getConfigManager().getConfigObject().put(result.getOptions().get(0).getAsString(), result.getOptions().get(1).getAsDouble());
                                                            ((CommandSender) result.getSender()).respond("Updated value");
                                                        })
                                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                                        .setOption(
                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                        .setRequired(true)
                                                                        .setNextOption(
                                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.DOUBLE)
                                                                                        .setRequired(true)
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                                        .build()
                                        )
                                        .withSubcommand("BOOLEAN",
                                                new CommandAPICommandBuilder()
                                                        .executes(result -> {
                                                            this.serverManager.getConfigManager().getConfigObject().put(result.getOptions().get(0).getAsString(), result.getOptions().get(1).getAsBoolean());
                                                            ((CommandSender) result.getSender()).respond("Updated value");
                                                        })
                                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                                        .setOption(
                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                        .setRequired(true)
                                                                        .setNextOption(
                                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.BOOLEAN)
                                                                                        .setRequired(true)
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                                        .build()
                                        )
                                        .withSubcommand("CHARACTER",
                                                new CommandAPICommandBuilder()
                                                        .executes(result -> {
                                                            this.serverManager.getConfigManager().getConfigObject().put(result.getOptions().get(0).getAsString(), result.getOptions().get(1).getAsChar());
                                                            ((CommandSender) result.getSender()).respond("Updated value");
                                                        })
                                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                                        .setOption(
                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                        .setRequired(true)
                                                                        .setNextOption(
                                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.CHARACTER)
                                                                                        .setRequired(true)
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                                        .build()
                                        )
                                        .withSubcommand("STRING",
                                                new CommandAPICommandBuilder()
                                                        .executes(result -> {
                                                            this.serverManager.getConfigManager().getConfigObject().put(result.getOptions().get(0).getAsString(), result.getOptions().get(1).getAsString());
                                                            ((CommandSender) result.getSender()).respond("Updated value");
                                                        })
                                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: config set <INTEGER/LONG/FLOAT/DOUBLE/BOOLEAN/CHARACTER/STRING> <key> <value>"))
                                                        .setOption(
                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                        .setRequired(true)
                                                                        .setNextOption(
                                                                                new CommandAPIOptionBuilder(CommandAPIOptionType.STRING)
                                                                                        .setRequired(true)
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                                        .build()
                                        )
                                        .build()
                        )
                        .withSubcommand("load",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            this.serverManager.getConfigManager().loadConfig(null);
                                            ((CommandSender) result.getSender()).respond("Loaded config file");
                                        })
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                        .build()
        );

        this.commandHandler.addCommand("plugin",
                new CommandAPICommandBuilder()
                        .executes(result -> ((CommandSender) result.getSender()).respond("PLUGIN COMMAND USAGE:\n" +
                                "plugin list\n" +
                                "plugin enable <plugin-id>\n" +
                                "plugin disable <plugin-id>\n" +
                                "plugin services\n"))
                        .withSubcommand("list",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            String msg = "| ID | NAME | ENABLED |\n";
                                            List<PluginHandler> pluginHandlers = this.serverManager.getPluginManager().getPlugins();

                                            for (int i = 0; i < pluginHandlers.size(); i++) {
                                                msg = msg + "| " + i + " | " + pluginHandlers.get(i) + " | " + pluginHandlers.get(i).isEnabled() + " |\n";
                                            }

                                            msg = msg + pluginHandlers.size() + " plugin(s) loaded";

                                            ((CommandSender) result.getSender()).respond(msg);
                                        })
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .withSubcommand("enable",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            try {
                                                PluginHandler pluginHandler = this.serverManager.getPluginManager().getPlugins().get(result.getOptions().get(0).getAsInteger());

                                                if (!pluginHandler.isEnabled()) {
                                                    pluginHandler.enablePlugin();
                                                    ((CommandSender) result.getSender()).respond("Plugin enabled");
                                                } else {
                                                    ((CommandSender) result.getSender()).respond("Plugin is already enabled");
                                                }
                                            } catch (IndexOutOfBoundsException e) {
                                                ((CommandSender) result.getSender()).respond("Plugin ID does not exist");
                                            }
                                        })
                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: plugin enable <plugin-id>"))
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setOption(
                                                new CommandAPIOptionBuilder(CommandAPIOptionType.INTEGER)
                                                        .setRequired(true)
                                                        .build()
                                        )
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .withSubcommand("disable",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            try {
                                                PluginHandler pluginHandler = this.serverManager.getPluginManager().getPlugins().get(result.getOptions().get(0).getAsInteger());

                                                if (pluginHandler.isEnabled()) {
                                                    pluginHandler.disablePlugin();
                                                    ((CommandSender) result.getSender()).respond("Plugin disabled");
                                                } else {
                                                    ((CommandSender) result.getSender()).respond("Plugin is already disabled");
                                                }
                                            } catch (IndexOutOfBoundsException e) {
                                                ((CommandSender) result.getSender()).respond("Plugin ID does not exist");
                                            }
                                        })
                                        .executesUnsuccessful(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE: plugin disable <plugin-id>"))
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setOption(
                                                new CommandAPIOptionBuilder(CommandAPIOptionType.INTEGER)
                                                        .setRequired(true)
                                                        .build()
                                        )
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                        .build()
        );

        this.commandHandler.addCommand("bot",
                new CommandAPICommandBuilder()
                        .executes(result -> ((CommandSender) result.getSender()).respond("COMMAND USAGE:\n" +
                                "bot status\n" +
                                "bot start\n" +
                                "bot stop\n" +
                                "bot shards\n"))
                        .withSubcommand("status",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            String response = "CURRENT BOT STATUS:\n";

                                            if (this.serverManager.getBotManager().isShardManagerRunning()) {
                                                response = response + "Status: RUNNING\n";

                                                response = response + "Shards:\n";
                                                response = response + "Running: " + this.serverManager.getBotManager().getShardManager().getShardsRunning() + "\n";
                                                response = response + "Queued: " + this.serverManager.getBotManager().getShardManager().getShardsQueued() + "\n";
                                                response = response + "Total: " + this.serverManager.getBotManager().getShardManager().getShardsTotal() + "\n";
                                            } else {
                                                response = response + "Status: SHUTDOWN";
                                            }

                                            ((CommandSender) result.getSender()).respond(response);
                                        })
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .withSubcommand("start",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            if (!this.serverManager.getBotManager().isShardManagerRunning()) {
                                                ((CommandSender) result.getSender()).respond("Starting bot...");
                                                this.serverManager.getBotManager().startShardManager();
                                            } else {
                                                ((CommandSender) result.getSender()).respond("Bot is already running");
                                            }
                                        })
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .withSubcommand("stop",
                                new CommandAPICommandBuilder()
                                        .executes(result -> {
                                            if (!this.serverManager.getBotManager().isShardManagerRunning()) {
                                                ((CommandSender) result.getSender()).respond("Bot is already stopped");
                                            } else {
                                                ((CommandSender) result.getSender()).respond("Stopping bot...");
                                                this.serverManager.getBotManager().stopShardManager();
                                            }
                                        })
                                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                                        .build()
                        )
                        .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                        .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                        .build()
        );

        this.serverManager.getLogger().info("System commands loaded");
    }

    public void executeCommand(CommandSender sender, String[] command) {
        this.commandHandler.onCommand(sender, command);
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
