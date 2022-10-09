package net.jandie1505.servermanager.plugins;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.jandie1505.servermanager.events.Event;
import net.jandie1505.servermanager.events.Listener;
import net.jandie1505.servermanager.utils.ShutdownCondition;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class PluginHandler implements ShutdownCondition {
    private final PluginManager pluginManager;
    private final String name;
    private final Plugin plugin;
    private final List<Runnable> services;
    private final List<Listener> listeners;
    private final List<EventListener> jdaEventListeners;
    private final File pluginDirectory;
    private boolean enabled;

    public PluginHandler(PluginManager pluginManager, String name, Plugin plugin) {
        this.pluginManager = pluginManager;
        this.name = name;
        this.plugin = plugin;
        this.plugin.init(this);
        this.services = Collections.synchronizedList(new ArrayList<>());
        this.listeners = Collections.synchronizedList(new ArrayList<>());
        this.jdaEventListeners = Collections.synchronizedList(new ArrayList<>());
        this.pluginDirectory = new File(System.getProperty("user.dir") + "/plugins", this.name);
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
     *
     * @return enabled=true
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Create a new repeating service.
     * Services are running their code in a specified time interval if the plugin and the service is enabled.
     * Don't use your own repeating threads, use these services feature instead.
     *
     * @param runnable PluginServiceRunnable (The same as a java runnable, but with the PluginService object as parameter
     * @param time     Time interval the service should be executed. Can be changed during runtime.
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
     *
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
     *
     * @param service The PluginService object of the service
     */
    public void deleteService(PluginService service) {
        this.services.remove(service);
    }

    /**
     * Add a ServerManager event listener.
     * ServerManager event listeners are NOT receiving JDA events. For this, use addJDAEventListener.
     *
     * @param listener ServerManager event listener
     */
    public void addSMEventListener(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Remove a ServerManager event listener by id.
     * The id is the list index.
     *
     * @param listenerId ServerManager event listener id
     */
    public void removeSMEventListener(int listenerId) {
        if (listenerId < this.listeners.size()) {
            this.listeners.remove(listenerId);
        }
    }

    /**
     * Remove a ServerManager event listener by Listener object.
     *
     * @param listener ServerManager event listener
     */
    public void removeSMEventListener(Listener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Add a JDA event listener.
     * JDA event listeners are not receiving ServerManager events. For this, use addSMEventListener.
     *
     * @param listener JDA event listener
     */
    public void addJDAEventListener(Listener listener) {
        this.listeners.add(listener);
    }

    /**
     * Remove a JDA event listener by id.
     * The id is the list index.
     *
     * @param listenerId JDA event listener id
     */
    public void removeJDAEventListener(int listenerId) {
        if (listenerId < this.listeners.size()) {
            this.listeners.remove(listenerId);
        }
    }

    /**
     * Remove a JDA event listener by Listener object.
     *
     * @param listener JDA event listener
     */
    public void removeJDAEventListener(Listener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Output info message with plugin name to console
     *
     * @param out message
     */
    public void logInfo(String out) {
        this.getPluginManager().getServerManager().getLogger().info("[" + this.name + "] " + out);
    }

    /**
     * Output warning message with plugin name to console
     *
     * @param out message
     */
    public void logWarning(String out) {
        this.getPluginManager().getServerManager().getLogger().warning("[" + this.name + "] " + out);
    }

    /**
     * Output error message with plugin name to console
     *
     * @param out message
     */
    public void logError(String out) {
        this.getPluginManager().getServerManager().getLogger().error("[" + this.name + "] " + out);
    }

    /**
     * Internal method for sending events to plugin event listeners
     *
     * @param event ServerManager event
     */
    void onEvent(Event event) {
        if (this.enabled) {
            for (Listener listener : this.listeners) {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    this.pluginManager.getServerManager().getLogger().warning("Exception while redirecting ServerManager events to plugin event listeners " + this.getName() + ": " + e + ";" + e.getMessage() + ";" + Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    /**
     * Internal method for sending events to plugin event listeners
     *
     * @param event JDA event
     */
    void onEvent(GenericEvent event) {
        if (this.enabled) {
            for (EventListener eventListener : this.jdaEventListeners) {
                try {
                    eventListener.onEvent(event);
                } catch (Exception e) {
                    this.pluginManager.getServerManager().getLogger().warning("Exception while redirecting JDA events to plugin event listeners " + this.getName() + ": " + e + ";" + e.getMessage() + ";" + Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    /**
     * Get the plugin manager
     *
     * @return PluginManager
     */
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    /**
     * Get the plugin's name
     *
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the plugin
     *
     * @return Plugin
     */
    public Plugin getPlugin() {
        return this.plugin;
    }

    /**
     * Get all services of this plugin
     *
     * @return List of services
     */
    public List<Runnable> getServices() {
        return List.copyOf(this.services);
    }

    /**
     * Get all ServerManager event listeners
     *
     * @return list of ServerManager event listeners
     */
    public List<Listener> getListeners() {
        return List.copyOf(this.listeners);
    }

    public List<EventListener> getJdaEventListeners() {
        return List.copyOf(this.jdaEventListeners);
    }

    /**
     * Get the plugin's directory
     * @return Plugin directory
     */
    public File getPluginDirectory() {
        return this.pluginDirectory;
    }

    @Override
    public boolean getShutdownCondition() {
        return this.pluginManager.getShutdownCondition();
    }
}
