package net.jandie1505.servermanager.console;

import net.jandie1505.consolecommandapi.executors.CommandAPICommandExecutor;
import net.jandie1505.consolecommandapi.result.CommandAPICommandResult;

public interface DefaultCommandExecutors {

    static CommandAPICommandExecutor noPermissionExecutor() {
        return new CommandAPICommandExecutor() {
            @Override
            public void onCommand(CommandAPICommandResult result) {
                ((CommandSender) result.getSender()).respond("No permission");
            }
        };
    }
}
