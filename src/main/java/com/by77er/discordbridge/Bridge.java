package com.by77er.discordbridge;

import com.by77er.discordbridge.utils.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Bridge extends JavaPlugin {
    private TcpChatListener tcpserver;
    @Override
    public void onEnable() {
        // fired when plugin is enabled / server starts
        // getCommand("bittest").setExecutor(new BitTest());
        System.out.println("Hello, Minecraft!");

        FileConfiguration config = this.getConfig();
        config.addDefault("webhook-url", "");
        config.addDefault("listen-port", 45376);
        config.options().copyDefaults(true);
        saveConfig();

        String url = config.getString("webhook-url");
        if (url.isEmpty()) {
            System.out.println("Failed to get webhook-url from config. This needs to be configured!");
            return;
        }

        Integer port = config.getInt("listen-port");
        System.out.println("Chat listener port set to " + port.toString());
        this.tcpserver = new TcpChatListener(port);

        // Create a new http client for firing webhooks
        getServer()
            .getPluginManager()
            .registerEvents(new ChatListener(url), this);
    }
    @Override
    public void onDisable() {
        // fired when the plugin is disabled / server stops
        System.out.println("Closing chat listener");
        this.tcpserver.closeListener();
    }

}