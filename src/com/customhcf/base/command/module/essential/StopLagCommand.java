
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class StopLagCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public StopLagCommand(BasePlugin plugin) {
        super("stoplag", "Decrease the server lag.");
        this.plugin = plugin;
        this.setUsage("/(command)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean newMode = !this.plugin.getServerHandler().isDecreasedLagMode();
        this.plugin.getServerHandler().setDecreasedLagMode(newMode);
        String newModeString = Boolean.toString(newMode);
        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", newModeString);
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Server is " + (newMode ? new StringBuilder().append(ChatColor.RED).append("not").toString() : new StringBuilder().append(ChatColor.GREEN).append("now").toString()) + ChatColor.YELLOW + " allowing intensive activity.");
        return true;
    }
}

