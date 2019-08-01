package com.by77er.discordbridge.utils;

import org.bukkit.command.*;

public class BitTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("It works!");
        return true;
    }
}