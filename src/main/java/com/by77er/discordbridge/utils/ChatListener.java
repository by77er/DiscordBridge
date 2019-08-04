package com.by77er.discordbridge.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/*

This class sends the following events to the configured webhook:
    - Player chat events
    - Player join / leave events

*/
public class ChatListener implements Listener {
    HttpClient httpclient;
    String webhookURL;
    public ChatListener(String webhookURL) {
        this.webhookURL = webhookURL;
        this.httpclient = HttpClient.newHttpClient();
    }

    @EventHandler 
    public void onPlayerJoined(PlayerJoinEvent event) {
        String username = "Server";
        ChatColor.stripColor(event.getJoinMessage());
        String message = ChatColor.stripColor(event.getJoinMessage());
        this.sendHookMessage(username, message);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String username = "Server";
        String message = ChatColor.stripColor(event.getQuitMessage());
        this.sendHookMessage(username, message);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Fired for each player chat message
        // TODO: Player skin icons
        String username = ChatColor.stripColor(event.getPlayer().getDisplayName());
        String message = ChatColor.stripColor(event.getMessage());
        this.sendHookMessage(username, message);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String username = "Server";
        String message = ChatColor.stripColor(event.getDeathMessage());
        this.sendHookMessage(username, message);
    }

    // Created separate function
    private void sendHookMessage(String username, String message) {
        try {
            HttpRequest request = HttpRequest
                .newBuilder(new URI(this.webhookURL))
                .headers("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(""
                + "{"
                + " \"username\": \"" + username + "\","
                + " \"content\": \"" + message.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
                + "}"))
                .build();
            // send request
            this.httpclient.sendAsync(request, HttpResponse.BodyHandlers.discarding());   
        } catch (Exception e) {
            System.out.println("--- ERROR: Failed to send to webhook ---");
            System.out.println(e.getMessage());
        }
    }
}