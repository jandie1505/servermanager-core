package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.ShardCacheView;
import net.jandie1505.servermanager.ServerManager;
import org.jetbrains.annotations.NotNull;

public final class JDAManager implements ShardManager {
    private final ServerManager serverManager;

    public JDAManager(ServerManager serverManager) {
        this.serverManager = serverManager;
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public int getShardsQueued() {
        return 0;
    }

    @NotNull
    @Override
    public ShardCacheView getShardCache() {
        return null;
    }

    @Override
    public void restart() {

    }

    @Override
    public void restart(int i) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdown(int i) {

    }

    @Override
    public void start(int i) {

    }

    @Override
    public void login() {

    }
}
