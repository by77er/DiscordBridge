package com.by77er.discordbridge.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.net.InetSocketAddress;

public class TcpChatListener {
    private AsynchronousServerSocketChannel listener;
    public TcpChatListener(Integer port) {
        // constructor
        // starts the listener and the accept loop
        try {
            System.out.println("Starting chat listener...");
            this.listener = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress("127.0.0.1", port));
            this.listener.accept(null, new CompletionHandler<AsynchronousSocketChannel,Void>() {
                public void completed(AsynchronousSocketChannel ch, Void att) {
                    // accept the next connection
                    listener.accept(null, this);
          
                    // handle this connection
                    handleChat(ch);
                }
                public void failed(Throwable exc, Void att) {
                    System.out.println("A connection to the chat server failed");
                    // Handle failed connections if we want to
                    // This just needs to be here to make the anonymous class valid
                }
            });
            System.out.println("Started listener");
        } catch (Exception e) {
            System.out.println("Error: Failed to start TCP listener");
            System.out.println(e.getMessage());
        }
    }

    // handles all successful connections to the server
    private void handleChat(AsynchronousSocketChannel channel) {
        try {
            System.out.println("Handling connection from " + channel.getRemoteAddress().toString());
            // Need to read more to understand whether this is a memory leak
            ByteBuffer readbuf = ByteBuffer.allocate(2000); // 2000B byffer, duh
            channel.read(readbuf, null, new CompletionHandler<Integer,Void>() {
                public void completed(Integer stat, Void att) {
                    // Convert to string
                    String raw_read = new String(readbuf.array(), StandardCharsets.UTF_8);
                    if (!raw_read.isEmpty() && stat != -1) {
                        // Parse message
                        raw_read = raw_read.substring(0, stat);
                        try {
                            // name before first newline
                            String name = raw_read.split("\n", 2)[0];
                            // message after first newline
                            String message = raw_read.split("\n", 2)[1];
                            // truncate
                            if (message.length() > 256) {
                                message = message.substring(0, 256);
                            }
                            // remove nulls
                            message = message.replace("\0", "");
                            // Send to everyone
                            Bukkit.broadcastMessage("<" + ChatColor.AQUA + name + ChatColor.RESET + "> " + message);
                        } catch (Exception e) {}
                    }
                    // get ready to read again
                    readbuf.clear(); // point to beginning of buffer
                    readbuf.put(new byte[2000]); // write 2000 zeros to clear
                    readbuf.clear();
                    if (channel.isOpen()) {
                        channel.read(readbuf, null, this);
                    } else {
                        System.out.println("Chat connection closed");
                    }
                }
                public void failed(Throwable exc, Void att) {
                    System.out.println("Error: Read failed, closing connection");
                    try {
                        channel.close();
                    } catch (Exception e) {
                        System.out.println("Couldn't even close the connection");
                        System.out.println(e);
                    }
                }
            });

        } catch (Exception e) {
            return;
        }
        
    }

    public void closeListener() {
        // Try to close / free the socket + port combo
        try {
            this.listener.close();
        } catch (Exception e) {
            System.out.println("Error: Failed to close chat listener");
            System.out.println(e.getMessage());
        }

    }
}