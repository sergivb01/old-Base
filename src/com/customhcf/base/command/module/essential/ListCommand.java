
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class ListCommand
extends BaseCommand {
    private final String TO_BE_ON_LIST_PERMISSION;

    public ListCommand() {
        super("list", "Lists players online");
        this.setAliases(new String[]{"who"});
        this.setUsage("/(command)");
        this.TO_BE_ON_LIST_PERMISSION = "command.list.own";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (ListCommand.canSee(sender, player)) {
                if (BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isVanished()) {
                    list.add(ChatColor.GRAY + player.getName());
                } else if (player.hasPermission("command.list.own")) {
                    list.add(ChatColor.GREEN + player.getName());
                }
            }
        }
        sender.sendMessage((Object) ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage((Object) ChatColor.YELLOW + "There are currently " + (Object) ChatColor.GREEN + Bukkit.getOnlinePlayers().length + (Object) ChatColor.GREEN + "/" + (Object) ChatColor.GREEN + Bukkit.getMaxPlayers() + (Object) ChatColor.YELLOW + " players online.");
        sender.sendMessage(" ");
        if (list.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Staff:" + " " + ChatColor.RED + "Not avaliable");
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Staff: " + ChatColor.GRAY + "(" + list.size() + ")" + " " + list.toString().replace("[", "").replace("]", "").replace(",", ChatColor.GRAY + ","));
        }
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }

    public String getRealName(String group) {
        return PermissionsEx.getPermissionManager().getGroup(group).getPrefix().replace("[", "").replace("]", "").replace("_", "").replace(group, "");
    }
}

