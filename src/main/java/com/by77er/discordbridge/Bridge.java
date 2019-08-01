package com.by77er.discordbridge;

import com.by77er.discordbridge.utils.*;

import org.bukkit.plugin.java.JavaPlugin;

public class Bridge extends JavaPlugin {
    @Override
    public void onEnable() {
        // fired when plugin is enabled / server starts
        this.getCommand("bittest").setExecutor(new BitTest());
        System.out.println("Hello, world!");

        String url = "";
        // Create a new http client for firing webhooks
        this.getServer()
            .getPluginManager()
            .registerEvents(new ChatListener(url), this);
    }
    @Override
    public void onDisable() {
        // fired when the plugin is disabled / server stops
    }

}