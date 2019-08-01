package com.by77er.discordbridge.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    HttpClient httpclient;
    String webhookURL;
    public ChatListener(String webhookURL) {
        this.webhookURL = webhookURL;
        this.httpclient = HttpClient.newHttpClient();
    }
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Fired for each player chat message
        // Just sends the message to the webhook
        // TODO: Player skin icons
        String username = event.getPlayer().getDisplayName();
        String message = event.getMessage();
        try {
            HttpRequest request = HttpRequest
                .newBuilder(new URI(this.webhookURL))
                .headers("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(""
                + "{"
                + " \"username\": \"" + username + "\","
                + " \"content\": \"" + message + "\""
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