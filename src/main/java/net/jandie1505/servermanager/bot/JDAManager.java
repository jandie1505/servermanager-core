package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.jandie1505.servermanager.ServerManager;

public final class JDAManager {
    private final ServerManager serverManager;
    private ShardManager shardManager;

    public JDAManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public void startShardManager() {
        if (this.shardManager == null) {
            this.shardManager = DefaultShardManagerBuilder
                    .create(this.serverManager.getConfigManager().getConfig().getString("token"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .setShardsTotal(1)
                    .addEventListeners(new SystemEvents(this))
                    .setActivity(Activity.playing("Starting up..."))
                    .setStatus(OnlineStatus.IDLE)
                    .build();
            this.serverManager.getLogger().info("Created shard manager");
        }
    }

    public void stopShardManager() {
        if (this.shardManager != null) {
            this.shardManager.shutdown();
            this.shardManager = null;
            this.serverManager.getLogger().info("Shut down shard manager");
        }
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }
}
