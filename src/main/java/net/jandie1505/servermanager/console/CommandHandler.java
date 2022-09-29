package net.jandie1505.servermanager.console;

import net.jandie1505.consolecommandapi.builder.CommandAPICommandBuilder;
import net.jandie1505.consolecommandapi.builder.CommandAPICommandHandlerBuilder;
import net.jandie1505.consolecommandapi.builder.CommandAPIOptionBuilder;
import net.jandie1505.consolecommandapi.command.CommandAPICommandHandler;
import net.jandie1505.consolecommandapi.enums.CommandAPIOptionType;
import net.jandie1505.consolecommandapi.executors.CommandAPIPermissionRequest;
import net.jandie1505.servermanager.ServerManager;
import org.json.JSONException;

public class CommandHandler {
    private final ServerManager serverManager;
    private final CommandAPICommandHandler commandHandler;

    public CommandHandler(ServerManager serverManager) {
        this.serverManager = serverManager;

        this.commandHandler = new CommandAPICommandHandlerBuilder()
                .executesCommandNotFound(result -> ((CommandSender) result.getSender()).respond("Command not found"))
                .executesNoPermission(DefaultCommandExecutors.noPermissionExecutor())
                .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                .build();

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
    }

    public void executeCommand(CommandSender sender, String[] command) {
        this.commandHandler.onCommand(sender, command);
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
