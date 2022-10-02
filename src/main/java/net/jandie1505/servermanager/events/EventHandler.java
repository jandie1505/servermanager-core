package net.jandie1505.servermanager.events;

import net.dv8tion.jda.api.events.GenericEvent;
import net.jandie1505.servermanager.ServerManager;

import java.util.Arrays;

public final class EventHandler {
    private final ServerManager serverManager;
    private final InternalListener internalListener;

    public EventHandler(ServerManager serverManager) {
        this.serverManager = serverManager;
        this.internalListener = new InternalListener(this);
    }

    public void fireSMEvent(Event event) {
        Thread thread = new Thread(() -> {
            try {
                this.internalListener.onEvent(event);
            } catch (Exception e) {
                this.serverManager.getLogger().warning("Exception while sending ServerManager events to internal listener: " + e + ";" + e.getMessage() + ";" + Arrays.toString(e.getStackTrace()));
            }

            try {
                this.getServerManager().getPluginManager().redirectEvent(event);
            } catch (Exception e) {
                this.serverManager.getLogger().warning("Exception while sending ServerManager events to plugin manager: " + e + ";" + e.getMessage() + ";" + Arrays.toString(e.getStackTrace()));
            }
        });
        thread.setName("servermanager-event-" + event.toString());
        thread.start();
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
