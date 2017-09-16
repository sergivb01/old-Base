
package com.customhcf.base.kit.argument;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitManager;
import com.customhcf.util.command.CommandArgument;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitApplyArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitApplyArgument(BasePlugin plugin) {
        super("apply", "Applies a kit to player");
        this.plugin = plugin;
        this.permission = "command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <kitName> <playerName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        Player target = Bukkit.getPlayer((String)args[2]);
        if (target == null || sender instanceof Player && !((Player)sender).canSee(target)) {
            sender.sendMessage((Object)ChatColor.RED + "Player '" + (Object)ChatColor.GRAY + args[2] + (Object)ChatColor.RED + "' not found.");
            return true;
        }
        if (kit.applyTo(target, true, true)) {
            sender.sendMessage((Object)ChatColor.GRAY + "Applied kit '" + kit.getDisplayName() + "' to '" + target.getName() + "'.");
            return true;
        }
        sender.sendMessage((Object)ChatColor.RED + "Failed to apply kit " + kit.getDisplayName() + " to " + target.getName() + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            List<Kit> kits = this.plugin.getKitManager().getKits();
            ArrayList<String> results = new ArrayList<String>(kits.size());
            for (Kit kit : kits) {
                results.add(kit.getName());
            }
            return results;
        }
        if (args.length == 3) {
            return null;
        }
        return Collections.emptyList();
    }
}

