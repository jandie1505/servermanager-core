package net.jandie1505.servermanager.bot;

import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.internal.utils.config.sharding.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class ExtendedShardManager extends DefaultShardManager {
    private final JDAManager jdaManager;
    private final Thread shutdownConditionThread;

    public ExtendedShardManager(@NotNull String token, @NotNull JDAManager jdaManager) {
        this(token, null, null, null, null, null, null, null, null, jdaManager);
    }

    public ExtendedShardManager(@NotNull String token, @Nullable Collection<Integer> shardIds, @NotNull JDAManager jdaManager) {
        this(token, shardIds, null, null, null, null, null, null, null, jdaManager);
    }

    public ExtendedShardManager(@NotNull String token, @Nullable Collection<Integer> shardIds, @Nullable ShardingConfig shardingConfig, @Nullable EventConfig eventConfig, @Nullable PresenceProviderConfig presenceConfig, @Nullable ThreadingProviderConfig threadingConfig, @Nullable ShardingSessionConfig sessionConfig, @Nullable ShardingMetaConfig metaConfig, @Nullable ChunkingFilter chunkingFilter, @NotNull JDAManager jdaManager) {
        super(token, shardIds, shardingConfig, eventConfig, presenceConfig, threadingConfig, sessionConfig, metaConfig, chunkingFilter);
        this.jdaManager = jdaManager;
        this.shutdownConditionThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted() && !this.shutdown.get()) {
                if (this.jdaManager.getShardManager() != this) {
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

    public JDAManager getJdaManager() {
        return this.jdaManager;
    }

    public boolean getShutdownStatus() {
        return this.shutdown.get();
    }
}
