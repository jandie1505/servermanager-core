package net.jandie1505.servermanager.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PluginHandler {
    private final PluginManager pluginManager;
    private final String name;
    private final Plugin plugin;
    private final List<Runnable> services;
    private boolean enabled;

    public PluginHandler(PluginManager pluginManager, String name, Plugin plugin) {
        this.pluginManager = pluginManager;
        this.name = name;
        this.plugin = plugin;
        this.plugin.init(this);
        this.services = Collections.synchronizedList(new ArrayList<>());
        this.enabled = false;
    }

    /**
     * Enables the plugin
     */
    public void enablePlugin() {
        if (!this.enabled) {
            this.enabled = true;
            this.pluginManager.getServerManager().getLogger().info("Enabled plugin " + this.name);
        }
    }

    /**
     * Disables the plugin
     */
    public void disablePlugin() {
        if (this.enabled) {
            this.enabled = false;
            this.pluginManager.getServerManager().getLogger().info("Disabled plugin " + this.name);
        }
    }

    /**
     * Check if the plugin is enabled
     * @return enabled=true
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Create a new repeating service.
     * Services are running their code in a specified time interval if the plugin and the service is enabled.
     * Don't use your own repeating threads, use these services feature instead.
     * @param runnable PluginServiceRunnable (The same as a java runnable, but with the PluginService object as parameter
     * @param time Time interval the service should be executed. Can be changed during runtime.
     * @return PluginService
     */
    public PluginService createService(PluginServiceRunnable runnable, int time) {
        PluginService pluginService = new PluginService(this, runnable, time);
        this.services.add(pluginService);
        return pluginService;
    }

    /**
     * Remove a repeating service by id.
     * The id is the list index of the service.
     * If a service is running while it's being deleted, it will stop.
     * @param serviceId list index of the service
     */
    public void deleteService(int serviceId) {
        if (serviceId < this.services.size()) {
            this.services.remove(serviceId);
        }
    }

    /**
     * Remove a repeating service by PluginService object.
     * If a service is running while it's being deleted, it will stop.
     * @param service The PluginService object of the service
     */
    public void deleteService(PluginService service) {
        this.services.remove(service);
    }

    /**
     * Get the plugin manager
     * @return PluginManager
     */
    public PluginManager getPluginManager()  {
        return this.pluginManager;
    }

    /**
     * Get the plugin's name
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the plugin
     * @return Plugin
     */
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Get all services of this plugin
     * @return List of services
     */
    public List<Runnable> getServices() {
        return List.copyOf(this.services);
    }
}
