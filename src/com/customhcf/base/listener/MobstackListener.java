package com.customhcf.base.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.util.cuboid.CoordinatePair;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.Village;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.gnu.trove.iterator.TObjectIntIterator;
import net.minecraft.util.gnu.trove.map.TObjectIntMap;
import net.minecraft.util.gnu.trove.map.hash.TObjectIntHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerBreedEntityEvent;
import org.bukkit.event.player.PlayerTemptEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MobstackListener
        extends BukkitRunnable
        implements Listener
{
    private static final int NATURAL_STACK_RADIUS = 81;
    private static final int MAX_STACKED_QUANTITY = 200;
    private static final List<LivingEntity> markedMobs = new ArrayList();
    private static final String STACKED_PREFIX = ChatColor.GREEN.toString() + "x";
    private final Table<CoordinatePair, EntityType, Integer> naturalSpawnStacks;
    private final TObjectIntMap<Location> spawnerStacks;
    private final BasePlugin plugin;

    public MobstackListener(BasePlugin plugin)
    {
        this.naturalSpawnStacks = HashBasedTable.create();
        this.spawnerStacks = new TObjectIntHashMap();
        this.plugin = plugin;
    }

    private CoordinatePair fromLocation(Location location)
    {
        return new CoordinatePair(location.getWorld(), 81 * Math.round(location.getBlockX() / 81), 81 * Math.round(location.getBlockZ() / 81));
    }

    public void run()
    {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getEnvironment() != World.Environment.THE_END) {
                for (Iterator<LivingEntity> localIterator2 = world.getLivingEntities().iterator(); localIterator2.hasNext();)
                {
                    LivingEntity entity = localIterator2.next();
                    if(entity.getType().equals(EntityType.ENDERMAN) || entity.getType().equals(EntityType.SLIME)) return;
                    if ((entity.isValid()) && (!(entity instanceof Player))) {
                        for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(8.0D, 8.0D, 8.0D)) {
                            if (((nearby instanceof LivingEntity)) && (nearby.isValid()) && (!(nearby instanceof Player))) {
                                stack(entity, (LivingEntity)nearby);
                            }
                        }
                    }
                }
            }
        }
        Iterator localIterator2;
        LivingEntity entity;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onSpawnerSpawn(SpawnerSpawnEvent event){
        if(event.getEntityType().equals(EntityType.ENDERMAN) || event.getEntityType().equals(EntityType.SLIME)) return;

        CreatureSpawner spawner = event.getSpawner();
        World world = spawner.getWorld();
        if ((world != null) && (world.getEnvironment().equals(World.Environment.THE_END))) {
            return;
        }
        Location location = spawner.getLocation();
        Optional<Integer> entityIdOptional = Optional.fromNullable(Integer.valueOf(this.spawnerStacks.get(location)));
        if (entityIdOptional.isPresent()){
            int entityId = entityIdOptional.get().intValue();
            net.minecraft.server.v1_7_R4.Entity nmsTarget = ((CraftWorld)location.getWorld()).getHandle().getEntity(entityId);
            org.bukkit.entity.Entity target = nmsTarget != null ? nmsTarget.getBukkitEntity() : null;
            if ((target != null) && ((target instanceof LivingEntity))){
                LivingEntity targetLiving = (LivingEntity)target;
                int stackedQuantity = getStackedQuantity(targetLiving);
                if (stackedQuantity == -1) {
                    stackedQuantity = 1;
                }
                if (stackedQuantity <= 200){
                    setStackedQuantity(targetLiving, ++stackedQuantity);
                    event.setCancelled(true);
                    return;
                }
            }
        }
        this.spawnerStacks.put(location, event.getEntity().getEntityId());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        EntityType entityType = event.getEntityType();

        if(entityType.equals(EntityType.ENDERMAN) || entityType.equals(EntityType.SLIME)) return;

        switch (event.getSpawnReason()){
            case CHUNK_GEN:
            case NATURAL:
            case DEFAULT:
                Location location = event.getLocation();
                CoordinatePair coordinatePair = fromLocation(location);
                Optional<Integer> entityIdOptional = Optional.fromNullable(this.naturalSpawnStacks.get(coordinatePair, entityType));
                if (entityIdOptional.isPresent()){
                    int entityId = entityIdOptional.get().intValue();
                    net.minecraft.server.v1_7_R4.Entity nmsTarget = ((CraftWorld)location.getWorld()).getHandle().getEntity(entityId);
                    org.bukkit.entity.Entity target = nmsTarget == null ? null : nmsTarget.getBukkitEntity();
                    if ((target != null) && ((target instanceof LivingEntity)))
                    {
                        LivingEntity targetLiving = (LivingEntity)target;
                        boolean canSpawn;
                        if ((targetLiving instanceof Ageable)) {
                            canSpawn = ((Ageable)targetLiving).isAdult();
                        } else {
                            canSpawn = (!(targetLiving instanceof Zombie)) || (!((Zombie)targetLiving).isBaby());
                        }
                        if (canSpawn)
                        {
                            int stackedQuantity = getStackedQuantity(targetLiving);
                            if (stackedQuantity == -1) {
                                stackedQuantity = 1;
                            }
                            if (stackedQuantity < 200)
                            {
                                setStackedQuantity(targetLiving, ++stackedQuantity);
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
                this.naturalSpawnStacks.put(coordinatePair, entityType, Integer.valueOf(event.getEntity().getEntityId()));
                break;
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event)
    {
        LivingEntity livingEntity = event.getEntity();
        int stackedQuantity = getStackedQuantity(livingEntity);
        if (stackedQuantity > 1)
        {
            LivingEntity respawned = (LivingEntity)livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), event.getEntityType());
            setStackedQuantity(respawned, Math.min(200, --stackedQuantity));
            if ((respawned instanceof Ageable)) {
                ((Ageable)respawned).setAdult();
            }
            if ((respawned instanceof Zombie)) {
                ((Zombie)respawned).setBaby(false);
            }
            if (this.spawnerStacks.containsValue(livingEntity.getEntityId()))
            {
                TObjectIntIterator<Location> iterator = this.spawnerStacks.iterator();
                while (iterator.hasNext())
                {
                    iterator.advance();
                    if (iterator.value() == livingEntity.getEntityId()) {
                        iterator.setValue(respawned.getEntityId());
                    }
                }
            }
        }
    }

    private int getStackedQuantity(LivingEntity livingEntity)
    {
        String customName = livingEntity.getCustomName();
        if ((customName != null) &&
                (customName.contains(STACKED_PREFIX)))
        {
            customName = customName.replace(STACKED_PREFIX, "");
            if (customName == null) {
                return -1;
            }
            customName = ChatColor.stripColor(customName);
            return Integer.parseInt(customName);
        }
        return -1;
    }

    private boolean stack(LivingEntity tostack, LivingEntity toremove)
    {
        Integer newStack = Integer.valueOf(1);
        Integer removeStack = Integer.valueOf(1);
        if (hasStack(tostack)) {
            newStack = Integer.valueOf(getStackedQuantity(tostack));
        }
        if (hasStack(toremove)) {
            removeStack = Integer.valueOf(getStackedQuantity(toremove));
        } else if ((getStackedQuantity(toremove) == -1) &&
                (toremove.getCustomName() != null) && (toremove.getCustomName().contains(ChatColor.WHITE.toString()))) {
            return false;
        }
        if (toremove.getType() != tostack.getType()) {
            return false;
        }
        if ((toremove.getType() == EntityType.SLIME) || (toremove.getType() == EntityType.MAGMA_CUBE) || (tostack.getType() == EntityType.SLIME) || (tostack.getType() == EntityType.MAGMA_CUBE) || (tostack.getType() == EntityType.VILLAGER)) {
            return false;
        }
        toremove.remove();
        setStackedQuantity(tostack, Math.min(newStack.intValue() + removeStack.intValue(), 200));
        return true;
    }

    private boolean hasStack(LivingEntity livingEntity)
    {
        return getStackedQuantity(livingEntity) != -1;
    }

    private void setStackedQuantity(LivingEntity livingEntity, int quantity)
    {
        Preconditions.checkArgument(quantity >= 0, "Stacked quantity cannot be negative");
        Preconditions.checkArgument(quantity <= 200, "Stacked quantity cannot be more than 200");
        if (quantity <= 1)
        {
            livingEntity.setCustomName(null);
        }
        else
        {
            livingEntity.setCustomName(STACKED_PREFIX + quantity);
            livingEntity.setCustomNameVisible(false);
        }
    }
}