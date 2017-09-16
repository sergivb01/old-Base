
package com.customhcf.base.command.module.teleport;

import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TeleportHereCommand
extends BaseCommand {
    public TeleportHereCommand() {
        super("teleporthere", "Teleport to a player to your position.");
        this.setAliases(new String[]{"tphere"});
        this.setUsage("/(command) <playerName>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        if (TeleportHereCommand.checkNull(sender, args[0])) {
            return true;
        }
        Player player = (Player)sender;
        BukkitUtils.playerWithNameOrUUID(args[0]).teleport((Entity)player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

