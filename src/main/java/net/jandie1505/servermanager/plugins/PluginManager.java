package net.jandie1505.servermanager.plugins;

import net.jandie1505.servermanager.ServerManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public final class PluginManager {
    private final ServerManager serverManager;
    private final List<PluginHandler> plugins;

    public PluginManager(ServerManager serverManager) {
        this.serverManager = serverManager;
        this.plugins = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * This method loads plugins.
     * It is only for ServerManager.
     * Don't call it as a plugin author.
     */
    @Deprecated
    public void loadPlugins() {
        try {
            File pluginDir = new File(System.getProperty("user.dir"), "plugins");

            if (!pluginDir.exists() || !pluginDir.isDirectory()) {
                pluginDir.mkdirs();
            }

            File[] pluginsFiles = pluginDir.listFiles((file1, s) -> s.toLowerCase().endsWith(".jar"));

            if (pluginsFiles != null) {
                for (File pluginFile : pluginsFiles) {
                    try {
                        JarFile pluginJar = new JarFile(pluginFile);

                        ZipEntry pluginJsonFile = pluginJar.getEntry("plugin.json");

                        InputStream pluginJsonFileInputStream = pluginJar.getInputStream(pluginJsonFile);

                        BufferedReader br = new BufferedReader(new InputStreamReader(pluginJsonFileInputStream));

                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append(System.lineSeparator());
                            line = br.readLine();
                        }

                        String out = sb.toString();

                        JSONObject pluginJSON = new JSONObject(out);

                        String pluginMain = pluginJSON.getString("main");
                        String pluginName;

                        if (pluginJSON.has("name")) {
                            pluginName = pluginJSON.getString("name");
                        } else {
                            pluginName = pluginMain;
                        }

                        URL[] urls = new URL[] {pluginFile.toURI().toURL()};
                        ClassLoader loader = new URLClassLoader(urls);
                        Class c = loader.loadClass(pluginMain);
                        Object pluginObject = c.newInstance();

                        if (pluginObject instanceof Plugin) {
                            Plugin plugin = (Plugin) pluginObject;
                            this.plugins.add(new PluginHandler(this, pluginName, plugin));
                            this.serverManager.getLogger().info("Loaded plugin " + pluginName);
                        }
                    } catch (IOException e) {
                        this.serverManager.getLogger().warning("Failed to load " + pluginFile.getName() + ": IO Error");
                    } catch (JSONException e) {
                        this.serverManager.getLogger().warning("Failed to load " + pluginFile.getName() + ": plugin.json file broken");
                    } catch (NullPointerException e) {
                        this.serverManager.getLogger().warning("Failed to load " + pluginFile.getName() + ": plugin.json file missing or ClassLoader error");
                    } catch (ClassNotFoundException e) {
                        this.serverManager.getLogger().warning("Failed to load " + pluginFile.getName() + ": main class not found");
                    } catch (IllegalAccessException | InstantiationException e) {
                        this.serverManager.getLogger().warning("Failed to load " + pluginFile.getName() + ": failed to create instance of main class");
                    }
                }
            }
        } catch (Exception e) {
            this.serverManager.getLogger().error(e + ";" + Arrays.toString(e.getStackTrace()) + ";" + e.getMessage());
            e.printStackTrace();
        }
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
