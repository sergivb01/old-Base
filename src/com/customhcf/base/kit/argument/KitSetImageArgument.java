
package com.customhcf.base.kit.argument;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitManager;
import com.customhcf.util.command.CommandArgument;
import com.customhcf.util.itemdb.ItemDb;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitSetImageArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitSetImageArgument(BasePlugin plugin) {
        super("setimage", "Sets the image of kit in GUI to held item");
        this.plugin = plugin;
        this.aliases = new String[]{"setitem", "setpic", "setpicture"};
        this.permission = "base.command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName() + " <kitName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This argument is only executable by players.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage((Object)ChatColor.RED + "Usage: " + this.getUsage(label));
            return true;
        }
        Player player = (Player)sender;
        ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            player.sendMessage((Object)ChatColor.RED + "You are not holding anything.");
            return true;
        }
        Kit kit = this.plugin.getKitManager().getKit(args[1]);
        if (kit == null) {
            sender.sendMessage((Object)ChatColor.RED + "There is not a kit named " + args[1] + '.');
            return true;
        }
        kit.setImage(stack.clone());
        sender.sendMessage((Object)ChatColor.AQUA + "Set image of kit " + (Object)ChatColor.YELLOW + kit.getDisplayName() + (Object)ChatColor.AQUA + " to " + (Object)ChatColor.YELLOW + this.plugin.getItemDb().getName(stack) + (Object)ChatColor.AQUA + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            return Collections.emptyList();
        }
        List<Kit> kits = this.plugin.getKitManager().getKits();
        ArrayList<String> results = new ArrayList<String>(kits.size());
        for (Kit kit : kits) {
            results.add(kit.getName());
        }
        return results;
    }
}

