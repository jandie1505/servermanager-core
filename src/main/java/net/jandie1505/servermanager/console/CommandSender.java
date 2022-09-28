package net.jandie1505.servermanager.console;

import net.jandie1505.consolecommandapi.utilities.permissionlevels.CommandAPIPermissionLevelCommandSender;

public interface CommandSender extends CommandAPIPermissionLevelCommandSender {

    void respond(String out);

}
