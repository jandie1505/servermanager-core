package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SystemEvents extends ListenerAdapter {
    private final BotManager botManager;

    public SystemEvents(BotManager botManager) {
        this.botManager = botManager;
    }

    public BotManager getJdaManager() {
        return this.botManager;
    }
}
