
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand
extends BaseCommand {
    public PingCommand() {
        super("ping", "Checks the ping of a player.");
        this.setUsage("/(command) <playerName>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[0]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        sender.sendMessage((target.equals((Object)sender) ? new StringBuilder().append((Object)ChatColor.YELLOW).append("Your ping is").toString() : new StringBuilder().append((Object)ChatColor.YELLOW).append("Ping of ").append(target.getName()).toString()) + (Object)ChatColor.GRAY + ": " + (Object)ChatColor.WHITE + target.getPing());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 && sender.hasPermission(command.getPermission() + ".others") ? null : Collections.emptyList();
    }
}

