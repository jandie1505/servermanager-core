package net.jandie1505.servermanager.events.events;

import net.jandie1505.servermanager.ServerManager;
import net.jandie1505.servermanager.events.Event;

public class StartCompleteEvent extends Event {
    private final ServerManager serverManager;

    public StartCompleteEvent(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
