package net.jandie1505.servermanager.console;

import net.jandie1505.consolecommandapi.builder.CommandAPICommandBuilder;
import net.jandie1505.consolecommandapi.builder.CommandAPICommandHandlerBuilder;
import net.jandie1505.consolecommandapi.command.CommandAPICommandHandler;
import net.jandie1505.consolecommandapi.executors.CommandAPIPermissionRequest;
import net.jandie1505.servermanager.ServerManager;

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
    }

    public void executeCommand(CommandSender sender, String[] command) {
        this.commandHandler.onCommand(sender, command);
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
