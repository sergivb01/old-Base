package com.customhcf.base.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.module.essential.StaffUtilitiesCommand;
import com.customhcf.base.user.BaseUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static com.customhcf.base.command.module.essential.StaffUtilitiesCommand.getBookTool;
import static com.customhcf.base.command.module.essential.StaffUtilitiesCommand.getVanishTool;

public class StaffListener implements Listener {

    private Inventory inv;

    @EventHandler
    public void staff(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack hand = event.getPlayer().getItemInHand();
            Player p = event.getPlayer();
            if (hand.getType() == Material.DIAMOND_PICKAXE) {
                int Counter = 0;
                this.inv = Bukkit.createInventory(null, 54, "Xrayer Gui");

                for (Player players : Bukkit.getOnlinePlayers()) {
                    Counter++;
                    if (Counter < 54) {
                        if (players.getLocation().getBlockY() < 20) {
                            ItemStack xSkull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                            ItemMeta xSkullMeta = xSkull.getItemMeta();
                            xSkullMeta.setDisplayName(ChatColor.GOLD + players.getName());
                            xSkullMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "This player is mining on level " + players.getLocation().getBlockY()));
                            xSkullMeta.setLore(Collections.singletonList(ChatColor.AQUA + "Diamonds: " + players.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
                            xSkull.setItemMeta(xSkullMeta);
                            inv.addItem(xSkull);
                        }
                    } else {
                        event.getPlayer()
                                .sendMessage(ChatColor.RED + "There are too many players mining right now.");
                    }
                }
                p.openInventory(inv);
            }
            if (hand.equals(StaffUtilitiesCommand.getRandomTeleport())) {
                if (!p.hasPermission("command.random")) {
                    p.sendMessage(ChatColor.RED + "No permission!");
                    return;
                }

                final ArrayList<Player> players = new ArrayList<>();
                for (final Player p2 : Bukkit.getOnlinePlayers()) {
                    if((p2 != p) && (!p2.hasPermission("command.random"))){
                        players.add(p2);
                    }
                }

                if (players.size() == 0) {
                    p.sendMessage(ChatColor.RED + "There are no players to teleport to.");
                    return;
                }


                final Player target = players.get(new Random().nextInt(players.size()));

                p.teleport(target);
                p.sendMessage(ChatColor.YELLOW + "You have been teleported to " + ChatColor.GREEN + target.getName());
                event.setCancelled(true);
            }
            if (hand.equals(getVanishTool(true))) {
                if (!p.hasPermission("command.vanish")) {
                    p.sendMessage(ChatColor.RED + "No permission!");
                    return;
                }

                p.getInventory().setItemInHand(getVanishTool(false));
                final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(p.getUniqueId());
                baseUser.setVanished(false);
                p.sendMessage(ChatColor.YELLOW + "Vanish mode of " + p.getName() + " set to " + "false" + '.');
            }
            if (hand.equals(getVanishTool(false))) {
                if (!p.hasPermission("command.vanish")) {
                    p.sendMessage(ChatColor.RED + "No permission!");
                    return;
                }

                p.getInventory().setItemInHand(getVanishTool(true));
                final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(p.getUniqueId());
                baseUser.setVanished(true);
                p.sendMessage(ChatColor.YELLOW + "Vanish mode of " + p.getName() + " set to " + "true" + '.');
            }
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            final ItemStack hand = event.getPlayer().getItemInHand();
            if (hand.equals(StaffUtilitiesCommand.getRandomTeleport())) {
                if (!event.getPlayer().hasPermission("command.staffmode")) {
                    event.getPlayer().sendMessage(ChatColor.RED + "No permission!");
                    return;
                }

                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getWhoClicked().getUniqueId());
        Inventory inventory = event.getInventory();
        ItemStack clicked = event.getCurrentItem();
        if (inventory.getName().equals("Xrayer Gui")) {
            if(clicked == null){
                return;
            }
            if (clicked.getType() == Material.SKULL_ITEM) {
                Bukkit.dispatchCommand(player, "tp " + ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
            }
        }
        if (baseUser.isStaffUtil()) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void touchytouchy(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player) {
            Player rightclick = (Player) event.getRightClicked();
            final ItemStack hand = event.getPlayer().getItemInHand();
            Player p = event.getPlayer();
            if (hand.equals(getBookTool())) {
                if (!p.hasPermission("command.staffmode")) {
                    p.sendMessage(ChatColor.RED + "No permission!");
                    return;
                }

                p.openInventory(rightclick.getInventory());
                p.sendMessage(ChatColor.YELLOW + "Opening the inventory of " + ChatColor.BOLD + rightclick.getName() + ChatColor.YELLOW + ".");
            } else if (hand.equals(StaffUtilitiesCommand.getFreezeTool())) {
                if (!p.hasPermission("command.staffmode")) {
                    p.sendMessage(ChatColor.RED + "No permission!");
                    return;
                }

                Bukkit.dispatchCommand(p, "freeze " + rightclick.getName());
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
        if (baseUser.isStaffUtil()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
        if (baseUser.isStaffUtil()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
        if (baseUser.isStaffUtil()) {
            Player p = event.getPlayer();
            p.sendMessage(ChatColor.RED + "You can not do this while in staff mode.");
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
        if (baseUser.isStaffUtil()) {
            Player p = event.getPlayer();
            p.sendMessage(ChatColor.RED + "You can not do this while in staff mode.");
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("command.staff")) {
            final BaseUser baseUser = BasePlugin.getPlugin().getUserManager().getUser(event.getPlayer().getUniqueId());
            baseUser.setVanished(true);
            event.getPlayer().sendMessage(ChatColor.GREEN + "Your vanish has been enabled.");

        }
    }
}