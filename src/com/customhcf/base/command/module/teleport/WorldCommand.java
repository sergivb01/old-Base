
package com.customhcf.base.command.module.teleport;

import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.BukkitUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.customhcf.util.chat.Text;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.omg.CORBA.Environment;

public class WorldCommand
extends BaseCommand implements Listener {
    public WorldCommand() {
        super("world", "Change current world.");
        this.setAliases(new String[]{"changeworld", "switchworld"});
        this.setUsage("/(command) <worldName>");

    }

    public Inventory inv;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable for players.");
            return true;
        }

        this.inv = Bukkit.createInventory(null, 9, "World");

        ItemStack overworld = new ItemStack(Material.GRASS, 1, (short) 3);
        ItemMeta overworldm = overworld.getItemMeta();
        overworldm.setLore((Arrays.asList((ChatColor.GRAY + " Click to teleport to the " + ChatColor.YELLOW + "Overworld"))));
        overworldm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Overworld");
        overworld.setItemMeta(overworldm);
        inv.setItem(2, overworld);

        ItemStack nether = new ItemStack(Material.NETHERRACK, 1, (short) 3);
        ItemMeta netherm = nether.getItemMeta();
        netherm.setLore((Arrays.asList((ChatColor.GRAY + " Click to teleport to the " + ChatColor.YELLOW + "Nether"))));
        netherm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Nether");
        nether.setItemMeta(netherm);
        inv.setItem(4, nether);

        ItemStack end = new ItemStack(Material.ENDER_STONE, 1, (short) 3);
        ItemMeta endm = end.getItemMeta();
        endm.setLore((Arrays.asList((ChatColor.GRAY + " Click to teleport to the " + ChatColor.YELLOW + "End"))));
        endm.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "End");
        end.setItemMeta(endm);
        inv.setItem(6, end);

        ((Player) sender).openInventory(inv);

//        if (args.length < 1) {
//            sender.sendMessage((Object)ChatColor.RED + this.getUsage());
//            return true;
//        }
//        World world = Bukkit.getWorld((String)args[0]);
//        if (world == null) {
//            sender.sendMessage((Object)ChatColor.RED + "World '" + args[0] + "' not found.");
//            return true;
//        }
//        Player player = (Player)sender;
//        if (player.getWorld().equals((Object)world)) {
//            sender.sendMessage((Object)ChatColor.RED + "You are already in that world.");
//            return true;
//        }
//        Location origin = player.getLocation();
//        Location location = new Location(world, origin.getX(), origin.getY(), origin.getZ(), origin.getYaw(), origin.getPitch());
//        player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
//        sender.sendMessage((Object)ChatColor.YELLOW + "Switched world to '" + world.getName() + (Object)ChatColor.AQUA + " [" + WordUtils.capitalizeFully((String)world.getEnvironment().name().replace('_', ' ')) + ']' + (Object)ChatColor.AQUA + "'.");
//        return true;
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        if (inventory.getName().equals("World")) {
            if (clicked.getType() == Material.GRASS) {
                Location origin = player.getLocation();
                event.setCancelled(true);
                player.closeInventory();
                Location location = new Location(Bukkit.getWorld("world"), origin.getX(), origin.getY(), origin.getZ(), origin.getYaw(), origin.getPitch());
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            if (clicked.getType() == Material.NETHERRACK) {
                Location origin = player.getLocation();
                event.setCancelled(true);
                player.closeInventory();
                Location location = new Location(Bukkit.getWorld("world_nether"), origin.getX(), origin.getY(), origin.getZ(), origin.getYaw(), origin.getPitch());
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);

            }
            if (clicked.getType() == Material.ENDER_STONE) {
                Location origin = player.getLocation();
                event.setCancelled(true);
                player.closeInventory();
                Location location = new Location(Bukkit.getWorld("world_the_end"), origin.getX(), origin.getY(), origin.getZ(), origin.getYaw(), origin.getPitch());
                player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
            }
            event.setCancelled(true);

        }
    }
}

