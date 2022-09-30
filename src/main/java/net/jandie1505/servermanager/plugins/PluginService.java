package net.jandie1505.servermanager.plugins;

import java.util.concurrent.TimeUnit;

public final class PluginService implements Runnable {
    private final PluginHandler pluginHandler;
    private final PluginServiceRunnable runnable;
    private int time;
    private Thread thread;

    public PluginService(PluginHandler pluginHandler, PluginServiceRunnable runnable, int time) {
        this.pluginHandler = pluginHandler;
        this.runnable = runnable;
        if(time <= 0) {
            time = 1;
        }
        this.time = time;
    }

    @Override
    public void run() {
        this.pluginHandler.getPluginManager().getServerManager().getLogger().info("Service " + thread.getName() + " started");
        while (!Thread.currentThread().isInterrupted() && this.thread == Thread.currentThread() && this.pluginHandler.isEnabled()) {
            try {
                this.runnable.run(this);
                TimeUnit.MILLISECONDS.sleep(this.time);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        this.pluginHandler.getPluginManager().getServerManager().getLogger().info("Service " + thread.getName() + " stopped");
    }

    public void start() {
        if (this.thread == null || !this.thread.isAlive()) {
            this.thread = new Thread(this);
            this.thread.setName("servermanager-service-xxxx");
            this.thread.start();
        }
    }

    public void stop() {
        if (this.thread != null) {
            this.thread.interrupt();
            new Thread(() -> {
                int time = 60;
                while (thread.isAlive()) {
                    if(time <= 0) {
                        thread.stop();
                        time = 10;
                        this.pluginHandler.getPluginManager().getServerManager().getLogger().warning("Killed service " + thread.getName());
                    }
                    time--;
                }
            }).start();
        }
    }

    /**
     * Set the time in milliseconds
     * @param time time in milliseconds
     */
    public void setTime(int time) {
        if (time <= 0) {
            time = 1;
        }
        this.time = time;
    }

    public PluginHandler getPluginHandler() {
        return this.pluginHandler;
    }

    public PluginServiceRunnable getRunnable() {
        return this.runnable;
    }

    public int getTime() {
        return this.time;
    }

    public boolean isRunning() {
        return (this.thread != null && this.thread.isAlive());
    }
}
