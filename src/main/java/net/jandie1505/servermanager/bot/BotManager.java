package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.utils.config.sharding.ShardingConfig;
import net.jandie1505.servermanager.ServerManager;
import net.jandie1505.servermanager.bot.listeners.RedirectEvents;
import net.jandie1505.servermanager.bot.listeners.SystemEvents;
import net.jandie1505.servermanager.utils.ShutdownCondition;

public final class BotManager implements ShutdownCondition {
    private final ServerManager serverManager;
    private ExtendedShardManager shardManager;

    public BotManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    /**
     * Crate a new and start a shard manager (starts the bot).
     * Only works if there is no shard manager running.
     */
    public void startShardManager() {
        if (this.shardManager == null || this.shardManager.isShutdown()) {
            this.shardManager = new ExtendedShardManager(
                    this.serverManager.getConfigManager().getConfig().getString("token"),
                    null,
                    new ShardingConfig(
                            this.serverManager.getConfigManager().getConfig().getInt("shardsTotal"),
                            true,
                            GatewayIntent.ALL_INTENTS,
                            MemberCachePolicy.ALL
                    ),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    this
            );
            this.shardManager.addEventListener(new SystemEvents(this));
            this.shardManager.addEventListener(new RedirectEvents(this));
            this.shardManager.login();
        }
    }

    /**
     * Shutdown the shard manager (stops the bot).
     * This will also set the shard manager null.
     */
    public void stopShardManager() {
        if (!this.shardManager.isShutdown()) {
            this.shardManager.shutdown();
            this.shardManager = null;
        }
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    /**
     * Get the ShardManager object.
     * Returns null if not available.
     * @return ShardManager
     */
    public ExtendedShardManager getShardManager() {
        return this.shardManager;
    }

    /**
     * Returns true if the ShardManager is not null and not shutdown
     * @return boolean bot running
     */
    public boolean isShardManagerRunning() {
        return this.shardManager != null && !this.shardManager.isShutdown();
    }

    @Override
    public boolean getShutdownCondition() {
        return this.serverManager.isShutdown();
    }
}