package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.internal.utils.config.sharding.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class ExtendedShardManager extends DefaultShardManager {
    private final BotManager botManager;
    private final Thread shutdownConditionThread;

    public ExtendedShardManager(@NotNull String token, @NotNull BotManager botManager) {
        this(token, null, null, null, null, null, null, null, null, botManager);
    }

    public ExtendedShardManager(@NotNull String token, @Nullable Collection<Integer> shardIds, @NotNull BotManager botManager) {
        this(token, shardIds, null, null, null, null, null, null, null, botManager);
    }

    public ExtendedShardManager(@NotNull String token, @Nullable Collection<Integer> shardIds, @Nullable ShardingConfig shardingConfig, @Nullable EventConfig eventConfig, @Nullable PresenceProviderConfig presenceConfig, @Nullable ThreadingProviderConfig threadingConfig, @Nullable ShardingSessionConfig sessionConfig, @Nullable ShardingMetaConfig metaConfig, @Nullable ChunkingFilter chunkingFilter, @NotNull BotManager botManager) {
        super(token, shardIds, shardingConfig, eventConfig, presenceConfig, threadingConfig, sessionConfig, metaConfig, chunkingFilter);
        this.botManager = botManager;
        this.shutdownConditionThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && !this.shutdown.get()) {
                if (this.botManager.getShardManager() != this) {
                    this.shutdown();
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    this.shutdown();
                }
            }
            if (!this.shutdown.get()) {
                this.shutdown();
            }
        });
        this.shutdownConditionThread.setName("servermanager-extendedshardmanager-shutdownthread");
        this.shutdownConditionThread.setDaemon(true);
        this.shutdownConditionThread.start();
    }

    public BotManager getJdaManager() {
        return this.botManager;
    }

    public boolean isShutdown() {
        return this.shutdown.get();
    }
}
