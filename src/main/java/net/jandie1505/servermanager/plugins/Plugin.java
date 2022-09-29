package net.jandie1505.servermanager.plugins;

public abstract class Plugin {
    private final PluginManager pluginManager;

    public Plugin(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void onEnable() {}

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }
}
