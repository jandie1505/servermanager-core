package net.jandie1505.servermanager.plugins;

import net.jandie1505.servermanager.ServerManager;

import java.util.*;

public class PluginManager {
    private final ServerManager serverManager;
    private final Map<UUID, Plugin> enabledPlugins;

    public PluginManager(ServerManager serverManager) {
        this.serverManager = serverManager;
        this.enabledPlugins = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Get a list of all loaded plugins
     * @return plugin
     */
    public List<Plugin> getPlugins() {
        return List.copyOf(this.enabledPlugins.values());
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
