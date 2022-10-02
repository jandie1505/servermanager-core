package net.jandie1505.servermanager.config;

import net.jandie1505.servermanager.ServerManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public final class ConfigManager {
    private final ServerManager serverManager;
    private JSONObject config;

    public ConfigManager(ServerManager serverManager, String overrideConfig) {
        this.serverManager = serverManager;
        this.loadConfig(overrideConfig);
    }

    public void loadConfig(String overrideConfigString) {
        JSONObject overrideConfig = new JSONObject();
        JSONObject configFile = new JSONObject();

        if(overrideConfigString != null) {
            try {
                overrideConfig = new JSONObject(overrideConfigString);
            } catch (JSONException ignored) {
                // CANNOT OUTPUT AT THIS TIME
            }
        }

        try {
            File file = new File(System.getProperty("user.dir"), "config.json");

            if(!file.exists()) {
                file.createNewFile();

                FileWriter writer = new FileWriter(file);
                writer.write(this.getDefaultValues().toString(4));
                writer.flush();
                writer.close();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            String out = sb.toString();

            configFile = new JSONObject(out);
        } catch (IOException | JSONException ignored) {
            // CANNOT OUTPUT AT THIS TIME
        }

        JSONObject internalConfig = this.getDefaultValues();

        internalConfig = this.getDefaultValues();

        for(String key : configFile.keySet()) {
            internalConfig.put(key, configFile.get(key));
        }

        for(String key : overrideConfig.keySet()) {
            internalConfig.put(key, overrideConfig.get(key));
        }

        this.config = new JSONObject(internalConfig.toString());
    }

    /**
     * Get a copy of the config
     * @return JSONObject
     */
    public JSONObject getConfig() {
        return new JSONObject(this.config.toString());
    }

    /**
     * Get the config
     * @return JSONConfig
     */
    public JSONObject getConfigObject() {
        return this.config;
    }

    public JSONObject getDefaultValues() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("token", "");
        jsonObject.put("autoStartJDA", true);
        jsonObject.put("autoEnablePlugins", true);

        return jsonObject;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
