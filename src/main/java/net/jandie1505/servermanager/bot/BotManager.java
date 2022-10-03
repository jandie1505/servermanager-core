package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.utils.config.sharding.ShardingConfig;
import net.jandie1505.servermanager.ServerManager;

public final class BotManager {
    private final ServerManager serverManager;
    private ExtendedShardManager shardManager;

    public BotManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

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

    public void stopShardManager() {
        if (!this.shardManager.isShutdown()) {
            this.shardManager.shutdown();
            this.shardManager = null;
        }
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    public ExtendedShardManager getShardManager() {
        return this.shardManager;
    }

    public boolean getShutdownCondition() {
        return this.serverManager.isShutdown();
    }
}
