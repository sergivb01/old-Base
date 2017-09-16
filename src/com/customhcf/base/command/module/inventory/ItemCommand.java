
package com.customhcf.base.command.module.inventory;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.itemdb.ItemDb;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemCommand
extends BaseCommand {
    public ItemCommand() {
        super("item", "Spawns an item.");
        this.setAliases(new String[]{"i", "get"});
        this.setUsage("/(command) <itemName> [quantity]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "This command is only executable for players.");
            return true;
        }
        String amount = "";
        Player p = (Player)sender;
        if (args.length == 0) {
            p.sendMessage((Object)ChatColor.RED + this.getUsage());
            return true;
        }
        if (BasePlugin.getPlugin().getItemDb().getItem(args[0]) == null) {
            sender.sendMessage((Object)ChatColor.RED + "Item or ID not found.");
            return true;
        }
        if (args.length == 1) {
            if (!p.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[0], BasePlugin.getPlugin().getItemDb().getItem(args[0]).getMaxStackSize())}).isEmpty()) {
                p.sendMessage((Object)ChatColor.RED + "Your inventory is full.");
                return true;
            }
            amount = "" + BasePlugin.getPlugin().getItemDb().getItem(args[0]).getMaxStackSize() + "";
        }
        if (args.length == 2) {
            if (!p.getInventory().addItem(new ItemStack[]{BasePlugin.getPlugin().getItemDb().getItem(args[0], Integer.parseInt(args[1]))}).isEmpty()) {
                p.sendMessage((Object)ChatColor.RED + "Your inventory is full.");
                return true;
            }
            amount = args[1];
        }
        Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + p.getName() + " gave himself " + amount + ", " + BasePlugin.getPlugin().getItemDb().getName(BasePlugin.getPlugin().getItemDb().getItem(args[0]))), (boolean)true);
        return true;
    }
}

