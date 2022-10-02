package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;

import java.util.Arrays;

public class RedirectEvents implements EventListener {
    private final JDAManager jdaManager;

    public RedirectEvents(JDAManager jdaManager) {
        this.jdaManager = jdaManager;
    }

    @Override
    public void onEvent(GenericEvent event) {
        try {
            this.jdaManager.getServerManager().getPluginManager().redirectEvent(event);
        } catch (Exception e) {
            this.jdaManager.getServerManager().getLogger().warning("Exception while sending JDA events to plugin manager: " + e + ";" + e.getMessage() + ";" + Arrays.toString(e.getStackTrace()));
        }
    }

    public JDAManager getJdaManager() {
        return this.jdaManager;
    }
}
