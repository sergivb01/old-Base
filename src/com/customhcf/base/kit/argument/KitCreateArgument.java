
package com.customhcf.base.kit.argument;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitManager;
import com.customhcf.base.kit.event.KitCreateEvent;
import com.customhcf.util.JavaUtils;
import com.customhcf.util.command.CommandArgument;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class KitCreateArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitCreateArgument(BasePlugin plugin) {
        super("create", "Creates a kit");
        this.plugin = plugin;
        this.permission = "command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <kitName> [kitDescription]";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "Only players may create kits.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        if (!JavaUtils.isAlphanumeric(args[1])) {
            sender.sendMessage((Object)ChatColor.GRAY + "Kit names may only be alphanumeric.");
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit != null) {
            sender.sendMessage((Object)ChatColor.RED + "There is already a kit named " + args[1] + '.');
            return true;
        }
        Player player = (Player)sender;
        kit = new Kit(args[1], args.length >= 3 ? args[2] : null, player.getInventory(), player.getActivePotionEffects());
        KitCreateEvent event = new KitCreateEvent(kit);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return true;
        }
        this.plugin.getKitManager().createKit(kit);
        sender.sendMessage((Object)ChatColor.GRAY + "Created kit '" + kit.getDisplayName() + "'.");
        return true;
    }
}

