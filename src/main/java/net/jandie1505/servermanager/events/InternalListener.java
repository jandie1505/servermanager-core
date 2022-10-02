package net.jandie1505.servermanager.events;

public final class InternalListener extends EasyListener {
    private EventHandler eventHandler;

    public InternalListener(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public EventHandler getEventHandler() {
        return this.eventHandler;
    }
}
