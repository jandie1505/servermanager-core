package net.jandie1505.servermanager.database;

import net.jandie1505.servermanager.ServerManager;

public final class DatabaseManager {
    private final ServerManager serverManager;

    public DatabaseManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
