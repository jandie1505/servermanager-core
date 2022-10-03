package net.jandie1505.servermanager.bot.listeners;

import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.jandie1505.servermanager.bot.BotManager;

public class SystemEvents extends ListenerAdapter {
    private final BotManager botManager;

    public SystemEvents(BotManager botManager) {
        this.botManager = botManager;
    }

    public BotManager getJdaManager() {
        return this.botManager;
    }
}
