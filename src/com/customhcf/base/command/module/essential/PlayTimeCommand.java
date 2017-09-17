
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.BasePlugin;
import com.customhcf.base.PlayTimeManager;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTimeCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public PlayTimeCommand(BasePlugin plugin) {
        super("playtime", "Check the playtime of another player.");
        this.setAliases(new String[]{"pt"});
        this.setUsage("/(command) [playerName]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        OfflinePlayer target;
        if (args.length >= 1) {
            target = BukkitUtils.offlinePlayerWithNameOrUUID(args[0]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (OfflinePlayer)sender;
        }
        if (!target.hasPlayedBefore() && !target.isOnline()) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
       // sender.sendMessage((Object)ChatColor.YELLOW + target.getName() + " has been playing for " + (Object)ChatColor.LIGHT_PURPLE + DurationFormatUtils.formatDurationWords((long)this.plugin.getPlayTimeManager().getTotalPlayTime(target.getUniqueId()), (boolean)true, (boolean)true) + (Object)ChatColor.YELLOW + " this map.");
        if(sender.hasPermission("rank.staff") && target.isOnline()) {
            sender.sendMessage(ChatColor.YELLOW + target.getName() + " has been playing for " + ChatColor.LIGHT_PURPLE + DurationFormatUtils.formatDurationWords(this.plugin.getPlayTimeManager().getTotalPlayTime(target.getUniqueId()), true, true) + ChatColor.YELLOW + " this map and has been " + ChatColor.RED + "AFK" + ChatColor.YELLOW + " for " + ChatColor.RED + DurationFormatUtils.formatDurationWords(BukkitUtils.getIdleTime(target.getPlayer()), true, true));
        } else {
            sender.sendMessage(ChatColor.YELLOW + target.getName() + " has been playing for " + ChatColor.LIGHT_PURPLE + DurationFormatUtils.formatDurationWords(this.plugin.getPlayTimeManager().getTotalPlayTime(target.getUniqueId()), true, true) + ChatColor.YELLOW + " this map.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

