package com.customhcf.base.command.module.essential;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AuthCommand
        extends BaseCommand {

    public String authpin;

    public AuthCommand(BasePlugin plugin) {
        super("auth", "Account security command.");
        this.setUsage("/(command) <pin>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args[1] == null) {
            sender.sendMessage(ChatColor.RED + "/auth <pin>");
            return false;
        }
        // TODO: CONNECT TO DATABASE HERE
        // TODO: SET VARIABLE "authpin" TO COLUMN IN SQL DATABASE
        if(args[1] != authpin) {
            sender.sendMessage(ChatColor.RED + "Your pin does not match our database.");
            return false;
        }
        if(args[1] == authpin) {
            sender.sendMessage(ChatColor.GREEN + "You have been authenticated.");
            return true;
        }
        return false;
    }
}