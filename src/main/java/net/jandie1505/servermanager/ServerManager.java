package net.jandie1505.servermanager;

import java.io.IOException;

public class ServerManager {
    private Thread managerThread;
    private final Console console;

    public ServerManager() throws IOException {
        this.console = new Console(this);
        this.console.start();
    }

    public static ServerManager instance;

    public static void main(String[] args) throws IOException {
        instance = new ServerManager();
    }
}
