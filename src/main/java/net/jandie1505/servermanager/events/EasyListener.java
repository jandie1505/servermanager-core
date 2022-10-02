package net.jandie1505.servermanager.events;

import net.jandie1505.servermanager.events.events.LogEvent;
import net.jandie1505.servermanager.events.events.StartCompleteEvent;

public abstract class EasyListener implements Listener {

    public void onStartComplete(StartCompleteEvent event) {}

    public void onLog(LogEvent event) {}

    public void unknownEvent(Event event) {}

    @Override
    public void onEvent(Event event) {
        if (event instanceof StartCompleteEvent) {
            this.onStartComplete((StartCompleteEvent) event);
        } else if (event instanceof LogEvent) {
            this.onLog((LogEvent) event);
        } else {
            this.unknownEvent(event);
        }
    }
}
