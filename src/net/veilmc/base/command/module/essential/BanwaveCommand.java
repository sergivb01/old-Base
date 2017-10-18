package net.veilmc.base.command.module.essential;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BanwaveCommand
        extends BaseCommand {
    public BanwaveCommand() {
        super("banwave", "Starts global ban wave.");
        this.setUsage("/(command)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Starting global ban wave...");
        for (String players : BasePlugin.getPlugin().getConfig().getStringList("banwave")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + players + " [AUTO] Blacklist [3.0]");
        }
        Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Global ban wave COMPLETE.");
        return true;
    }
}