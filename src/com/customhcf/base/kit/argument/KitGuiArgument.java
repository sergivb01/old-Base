
package com.customhcf.base.kit.argument;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.kit.Kit;
import com.customhcf.base.kit.KitManager;
import com.customhcf.util.command.CommandArgument;
import java.util.Collections;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class KitGuiArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public KitGuiArgument(BasePlugin plugin) {
        super("gui", "Opens the kit gui");
        this.plugin = plugin;
        this.aliases = new String[]{"menu"};
        this.permission = "command.kit.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage((Object)ChatColor.RED + "Only players may open kit GUI's.");
            return true;
        }
        List<Kit> kits = this.plugin.getKitManager().getKits();
        if (kits.isEmpty()) {
            sender.sendMessage((Object)ChatColor.RED + "No kits have been defined.");
            return true;
        }
        Player player = (Player)sender;
        player.openInventory(this.plugin.getKitManager().getGui(player));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

