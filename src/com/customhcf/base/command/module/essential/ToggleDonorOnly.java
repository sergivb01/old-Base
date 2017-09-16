
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ToggleDonorOnly
extends BaseCommand {
    BasePlugin plugin;

    public ToggleDonorOnly(BasePlugin plugin) {
        super("toggledonoronly", "Turns the server into Donor only mode.");
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.plugin.getServerHandler().setDonorOnly(!this.plugin.getServerHandler().isDonorOnly());
        Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Server is " + (!this.plugin.getServerHandler().isDonorOnly() ? new StringBuilder().append((Object)ChatColor.RED).append("not").toString() : new StringBuilder().append((Object)ChatColor.GREEN).append("now").toString()) + (Object)ChatColor.YELLOW + " in donor only mode."));
        return true;
    }
}

