package net.jandie1505.servermanager.plugins;

import net.jandie1505.servermanager.ServerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public class PluginManager {
    private final ServerManager serverManager;
    private final List<PluginHandler> plugins;

    public PluginManager(ServerManager serverManager) {
        this.serverManager = serverManager;
        this.plugins = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Get a list of all loaded plugins
     * @return plugin
     */
    public List<PluginHandler> getPlugins() {
        return this.plugins;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
