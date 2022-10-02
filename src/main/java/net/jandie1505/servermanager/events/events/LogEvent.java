package net.jandie1505.servermanager.events.events;

import net.jandie1505.servermanager.events.Event;
import net.jandie1505.servermanager.logger.Logger;

import java.time.LocalDateTime;

public class LogEvent extends Event {
    private final Logger logger;
    private final LocalDateTime timestamp;
    private final String content;

    public LogEvent(Logger logger, LocalDateTime timestamp, String content) {
        this.logger = logger;
        this.timestamp = timestamp;
        this.content = content;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getContent() {
        return this.content;
    }
}
