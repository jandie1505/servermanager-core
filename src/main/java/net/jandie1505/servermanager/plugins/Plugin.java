package net.jandie1505.servermanager.plugins;

public abstract class Plugin {
    private PluginHandler pluginHandler;

    public Plugin() {}

    void init(PluginHandler pluginHandler) {
        this.pluginHandler = pluginHandler;
    }

    public void onEnable() {}

    public void onDisable() {}

    public PluginHandler getPluginManager() {
        return this.pluginHandler;
    }
}
