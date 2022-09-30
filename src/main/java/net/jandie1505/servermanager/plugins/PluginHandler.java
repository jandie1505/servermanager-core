package net.jandie1505.servermanager.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginHandler {
    private final PluginManager pluginManager;
    private final Plugin plugin;
    private final List<Runnable> services;
    private boolean enabled;

    public PluginHandler(PluginManager pluginManager, Plugin plugin) {
        this.pluginManager = pluginManager;
        this.plugin = plugin;
        this.services = Collections.synchronizedList(new ArrayList<>());
        this.enabled = false;
    }

    /**
     * Enables the plugin
     */
    public void enablePlugin() {
        this.enabled = true;
    }

    /**
     * Disables the plugin
     */
    public void disablePlugin() {
        this.enabled = false;
    }

    /**
     * Check if the plugin is enabled
     * @return enabled=true
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Get the plugin manager
     * @return PluginManager
     */
    public PluginManager getPluginManager()  {
        return this.pluginManager;
    }

    /**
     * Get the plugin
     * @return Plugin
     */
    public Plugin getPlugin() {
        return this.plugin;
    }

    public List<Runnable> getServices() {
        return List.copyOf(this.services);
    }
}
