package net.jandie1505.servermanager.bot;

import net.jandie1505.servermanager.ServerManager;

public final class JDAManager {
    private final ServerManager serverManager;
    private ExtendedShardManager shardManager;

    public JDAManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    public ExtendedShardManager getShardManager() {
        return this.shardManager;
    }
}
