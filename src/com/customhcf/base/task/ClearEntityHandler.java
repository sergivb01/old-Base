
package com.customhcf.base.task;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Tameable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ClearEntityHandler
extends BukkitRunnable {
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (Entity entity : chunk.getEntities()) {
                    ItemStack itemStack;
                    Material material;
                    Item item;
                    if (entity.getType() != EntityType.DROPPED_ITEM && entity.getType() != EntityType.SKELETON && entity.getType() != EntityType.ZOMBIE && entity.getType() != EntityType.ARROW && entity.getType() != EntityType.SPIDER && entity.getType() != EntityType.CREEPER && entity.getType() != EntityType.COW && entity.getType() != EntityType.ENDERMAN && entity.getType() != EntityType.SILVERFISH && entity.getType() != EntityType.MAGMA_CUBE && entity.getType() != EntityType.IRON_GOLEM && entity.getType() != EntityType.GHAST || entity instanceof Tameable && ((Tameable)entity).isTamed() || entity.getType() == EntityType.DROPPED_ITEM && entity instanceof Item && ((material = (itemStack = (item = (Item)entity).getItemStack()).getType()) == Material.SAND || material == Material.TNT || material == Material.DIAMOND || material == Material.EMERALD || material == Material.DIAMOND_ORE || material == Material.GOLD_ORE || material == Material.GOLD_INGOT || material == Material.IRON_INGOT || material == Material.IRON_ORE || material == Material.MOB_SPAWNER || material == Material.HOPPER || material == Material.BEACON)) continue;
                    entity.remove();
                }
            }
        }
    }
}

