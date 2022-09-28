package net.jandie1505.servermanager.console;

import net.jandie1505.consolecommandapi.builder.CommandAPICommandHandlerBuilder;
import net.jandie1505.consolecommandapi.command.CommandAPICommandHandler;
import net.jandie1505.consolecommandapi.executors.CommandAPIPermissionRequest;
import net.jandie1505.consolecommandapi.utilities.permissionlevels.CommandAPIPermissionLevelCommandSender;
import net.jandie1505.servermanager.ServerManager;
import net.jandie1505.servermanager.console.sender.CommandSender;

public class CommandHandler {
    private final CommandAPICommandHandler commandHandler;

    public CommandHandler(ServerManager serverManager) {
        this.commandHandler = new CommandAPICommandHandlerBuilder()
                .executesCommandNotFound(result -> ((CommandSender) result.getSender()).respond("Command not found"))
                .executesNoPermission(result -> ((CommandSender) result.getSender()).respond("No permission"))
                .setPermissionRequest(CommandAPIPermissionRequest.requirePermissionLevel(1))
                .build();
    }

    public void executeCommand(CommandSender sender, String[] command) {
        this.commandHandler.onCommand(sender, command);
    }
}
