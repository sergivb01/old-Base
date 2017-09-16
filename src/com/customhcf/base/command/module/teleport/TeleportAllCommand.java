
package com.customhcf.base.command.module.teleport;

import com.customhcf.base.command.BaseCommand;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportAllCommand
extends BaseCommand {
    public TeleportAllCommand() {
        super("teleportall", "Teleport all players to yourself.");
        this.setAliases(new String[]{"tpall"});
        this.setUsage("/(command)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        Player player = (Player)sender;
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals((Object)player) || !player.canSee(target)) continue;
            target.teleport((Entity)player, PlayerTeleportEvent.TeleportCause.COMMAND);
        }
        Command.broadcastCommandMessage(sender, ChatColor.GREEN + "All players have been teleported to your location.");
//        sender.sendMessage(ChatColor.GREEN + "All players have been teleported to your location.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

