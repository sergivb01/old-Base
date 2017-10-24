package net.veilmc.base.command.module.essential;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BanwaveCommand extends BaseCommand {
    private BasePlugin plugin;

    public BanwaveCommand(BasePlugin plugin) {
        super("banwave", "Starts global ban wave.");
        this.plugin = plugin;
        this.setUsage("/(command)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.getName().equalsIgnoreCase("ltd") || (sender.getName().equalsIgnoreCase("imask3r"))) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Starting global blacklist wave...");
                for (String players : BasePlugin.getPlugin().getConfig().getStringList("banwave")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "blacklist " + players + " [AUTO] Blacklist [3.0]");
                }
            });
            return true;
        }
        sender.sendMessage(ChatColor.RED + "https://www.youtube.com/watch?v=9alXo1OXTec");
        return true;
    }
}