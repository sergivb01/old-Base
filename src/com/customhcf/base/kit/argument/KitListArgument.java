
package com.customhcf.base.kit.argument;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.PlayTimeManager;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitManager;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.UserManager;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.command.CommandArgument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitListArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitListArgument(BasePlugin plugin) {
        super("list", "Lists all current kits");
        this.plugin = plugin;
        this.permission = "command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        List<Kit> kits = this.plugin.getKitManager().getKits();
        if (kits.isEmpty()) {
            sender.sendMessage((Object)ChatColor.RED + "No kits have been defined.");
            return true;
        }
        ArrayList<String> kitNames = new ArrayList<String>();
        for (Kit kit : kits) {
            String permission = kit.getPermissionNode();
            if (permission != null && !sender.hasPermission(permission)) continue;
            BaseUser user = this.plugin.getUserManager().getUser(((Player)sender).getUniqueId());
            ChatColor color = user.getKitUses(kit) >= kit.getMaximumUses() || user.getRemainingKitCooldown(kit) >= (long)kit.getMaximumUses() || this.plugin.getPlayTimeManager().getTotalPlayTime(((Player)sender).getUniqueId()) <= kit.getMinPlaytimeMillis() ? ChatColor.RED : ChatColor.GREEN;
            kitNames.add((Object)color + kit.getDisplayName());
        }
        String kitList = StringUtils.join(kitNames, (String)((Object)ChatColor.GRAY + ", "));
        sender.sendMessage((Object)ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage((Object)ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Kit List" + (Object)ChatColor.GREEN + "[" + kitNames.size() + '/' + kits.size() + "]");
        sender.sendMessage((Object)ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage((Object)ChatColor.GRAY + "[" + (Object)ChatColor.RED + kitList + (Object)ChatColor.GRAY + ']');
        sender.sendMessage((Object)ChatColor.DARK_GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

