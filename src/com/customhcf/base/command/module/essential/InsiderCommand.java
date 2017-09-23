package com.customhcf.base.command.module.essential;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InsiderCommand
        extends BaseCommand {
    public InsiderCommand() {
        super("insider", "Set insider rank of a player");
        this.setUsage("/(command) <add/remove> <playerName>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        if(!args[0].equalsIgnoreCase("add") || (!args[0].equalsIgnoreCase("remove"))) {
            sender.sendMessage(ChatColor.RED + "Usage: /insider " + ChatColor.AQUA + "<add/remove> " + ChatColor.RED + "<playerName>");
            return true;
        }

        Player target = BukkitUtils.playerWithNameOrUUID(args[1]);
        if (target == null) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[1]));
            return true;
        }

        switch (args[0]){
            case "add":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + target.getName() + " group add Insider");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have added &a" + target.getName() + " &eas Insider."));
                break;
            case "remove":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + target.getName() + " group remove Insider");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have been removed &a"+ target.getName() + "&e's Insider Rank."));
                break;
            default:
                sender.sendMessage(this.getUsage(label));
                break;
        }


        return true;
    }
}