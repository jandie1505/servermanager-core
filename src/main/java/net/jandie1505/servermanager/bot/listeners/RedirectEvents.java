package net.jandie1505.servermanager.bot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.jandie1505.servermanager.bot.BotManager;

import java.util.Arrays;

public class RedirectEvents implements EventListener {
    private final BotManager botManager;

    public RedirectEvents(BotManager botManager) {
        this.botManager = botManager;
    }

    @Override
    public void onEvent(GenericEvent event) {
        try {
            this.botManager.getServerManager().getPluginManager().redirectEvent(event);
        } catch (Exception e) {
            this.botManager.getServerManager().getLogger().warning("Exception while sending JDA events to plugin manager: " + e + ";" + e.getMessage() + ";" + Arrays.toString(e.getStackTrace()));
        }
    }

    public BotManager getJdaManager() {
        return this.botManager;
    }
}
