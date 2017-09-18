package com.customhcf.base.command.module.chat;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FamousCommand
        extends BaseCommand {

    public FamousCommand(BasePlugin plugin) {
        super("famous", "Check requirements");
        this.setUsage("/(command)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Famous Requirments");
        sender.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "5000 Subscribers");
        sender.sendMessage(ChatColor.WHITE + " * " + ChatColor.YELLOW + "1 Video on VeilMC");
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }
}