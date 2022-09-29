package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SystemEvents extends ListenerAdapter {
    private final JDAManager jdaManager;

    public SystemEvents(JDAManager jdaManager) {
        this.jdaManager = jdaManager;
    }

    public JDAManager getJdaManager() {
        return this.jdaManager;
    }
}
