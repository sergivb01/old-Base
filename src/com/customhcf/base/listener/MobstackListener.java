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
        implements Listener {
    private static final int NATURAL_STACK_RADIUS = 8;
    private static final int MAX_STACKED_QUANTITY = 200;
    private static final List<LivingEntity> markedMobs = new ArrayList<LivingEntity>();
    private static final String STACKED_PREFIX = ChatColor.GREEN.toString() + "x";
    private final Table<CoordinatePair, EntityType, Integer> naturalSpawnStacks = HashBasedTable.create();
    private final TObjectIntMap<Location> spawnerStacks = new TObjectIntHashMap();
    private final BasePlugin plugin;

    public MobstackListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    private CoordinatePair fromLocation(Location location) {
        return new CoordinatePair(location.getWorld(), NATURAL_STACK_RADIUS * Math.round(location.getBlockX() / NATURAL_STACK_RADIUS), NATURAL_STACK_RADIUS * Math.round(location.getBlockZ() / NATURAL_STACK_RADIUS));
    }

    public void run() {
        for (World world : Bukkit.getServer().getWorlds()) {
            if (world.getEnvironment() == World.Environment.THE_END) continue;
            for (LivingEntity entity : world.getLivingEntities()) {
                if (!entity.isValid() || entity instanceof Player) continue;
                for (org.bukkit.entity.Entity nearby : entity.getNearbyEntities(8.0, 8.0, 8.0)) {
                    if (!(nearby instanceof LivingEntity) || !nearby.isValid() || nearby instanceof Player) continue;
                    this.stack(entity, (LivingEntity)nearby);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerTemptEntity(PlayerTemptEntityEvent event) {
        int stackedQuantity = this.getStackedQuantity((LivingEntity)event.getEntity());
        if (stackedQuantity >= MAX_STACKED_QUANTITY) {
            event.setCancelled(true);
            event.getPlayer().sendMessage((Object)ChatColor.RED + "This entity is already max stacked.");
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerBreedEntity(PlayerBreedEntityEvent event) {
        if (event.getEntity() instanceof Horse) {
            return;
        }
        LivingEntity chosen = this.plugin.getRandom().nextBoolean() ? event.getFirstParent() : event.getSecondParent();
        int stackedQuantity = this.getStackedQuantity(chosen);
        if (stackedQuantity == -1) {
            stackedQuantity = 1;
        }
        this.setStackedQuantity(chosen, ++stackedQuantity);
        event.getPlayer().sendMessage((Object)ChatColor.GREEN + "One of the adults bred has been increased a stack.");
        event.setCancelled(true);
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
                if (stackedQuantity <= 199){
                    setStackedQuantity(targetLiving, ++stackedQuantity);
                    event.setCancelled(true);
                    return;
                }
            }
        }
        this.spawnerStacks.put(location, event.getEntity().getEntityId());
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        EntityType entityType = event.getEntityType();
        switch (entityType) {
            default:
        }
        switch (event.getSpawnReason()) {
            case CHUNK_GEN:
            case NATURAL:
            case DEFAULT: {
                Location location = event.getLocation();
                CoordinatePair coordinatePair = this.fromLocation(location);
                Optional entityIdOptional = Optional.fromNullable((Object)this.naturalSpawnStacks.get((Object)coordinatePair, (Object)entityType));
                if (entityIdOptional.isPresent()) {
                    CraftEntity target;
                    int entityId = (Integer)entityIdOptional.get();
                    Entity nmsTarget = ((CraftWorld)location.getWorld()).getHandle().getEntity(entityId);
                    CraftEntity craftEntity = target = nmsTarget == null ? null : nmsTarget.getBukkitEntity();
                    if (target != null && target instanceof LivingEntity) {
                        boolean canSpawn;
                        LivingEntity targetLiving = (LivingEntity)target;
                        if (targetLiving instanceof Ageable) {
                            canSpawn = ((Ageable)targetLiving).isAdult();
                        } else {
                            boolean bl = canSpawn = !(targetLiving instanceof Zombie) || !((Zombie)targetLiving).isBaby();
                        }
                        if (canSpawn) {
                            int stackedQuantity = this.getStackedQuantity(targetLiving);
                            if (stackedQuantity == -1) {
                                stackedQuantity = 1;
                            }
                            if (stackedQuantity < MAX_STACKED_QUANTITY) {
                                this.setStackedQuantity(targetLiving, ++stackedQuantity);
                                event.setCancelled(true);
                                return;
                            }
                            if(targetLiving instanceof Villager) {
                                return;
                            }
                        }
                    }
                }
                this.naturalSpawnStacks.put(coordinatePair, entityType, event.getEntity().getEntityId());
                break;
            }
        }
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        int stackedQuantity = this.getStackedQuantity(livingEntity);
        if (stackedQuantity > 1) {
            LivingEntity respawned = (LivingEntity)livingEntity.getWorld().spawnEntity(livingEntity.getLocation(), event.getEntityType());
            this.setStackedQuantity(respawned, Math.min(MAX_STACKED_QUANTITY, --stackedQuantity));
            if (respawned instanceof Ageable) {
                ((Ageable)respawned).setAdult();
            }
            if (respawned instanceof Zombie) {
                ((Zombie)respawned).setBaby(false);
            }
            if (this.spawnerStacks.containsValue(livingEntity.getEntityId())) {
                TObjectIntIterator iterator = this.spawnerStacks.iterator();
                while (iterator.hasNext()) {
                    iterator.advance();
                    if (iterator.value() != livingEntity.getEntityId()) continue;
                    iterator.setValue(respawned.getEntityId());
                    break;
                }
            }
        }
    }

    private int getStackedQuantity(LivingEntity livingEntity) {
        String customName = livingEntity.getCustomName();
        if (customName != null && customName.contains(STACKED_PREFIX)) {
            if ((customName = customName.replace(STACKED_PREFIX, "")) == null) {
                return -1;
            }
            customName = ChatColor.stripColor((String)customName);
            return Ints.tryParse((String)customName);
        }
        return -1;
    }

    private boolean stack(LivingEntity tostack, LivingEntity toremove) {
        Integer newStack = 1;
        Integer removeStack = 1;
        if (this.hasStack(tostack)) {
            newStack = this.getStackedQuantity(tostack);
        }
        if (this.hasStack(toremove)) {
            removeStack = this.getStackedQuantity(toremove);
        } else if (this.getStackedQuantity(toremove) == -1 && toremove.getCustomName() != null && toremove.getCustomName().contains(ChatColor.WHITE.toString())) {
            return false;
        }
        if (toremove.getType() != tostack.getType()) {
            return false;
        }
        if (toremove.getType() == EntityType.SLIME || toremove.getType() == EntityType.MAGMA_CUBE || tostack.getType() == EntityType.SLIME || tostack.getType() == EntityType.MAGMA_CUBE || tostack.getType() == EntityType.VILLAGER) {
            return false;
        }
        toremove.remove();
        this.setStackedQuantity(tostack, Math.min(newStack + removeStack, MAX_STACKED_QUANTITY));
        return true;
    }

    private boolean hasStack(LivingEntity livingEntity) {
        if (this.getStackedQuantity(livingEntity) != -1) {
            return true;
        }
        return false;
    }

    private void setStackedQuantity(LivingEntity livingEntity, int quantity) {
        Preconditions.checkArgument((boolean)(quantity >= 0), (Object)"Stacked quantity cannot be negative");
        Preconditions.checkArgument((boolean)(quantity <= MAX_STACKED_QUANTITY), "Stacked quantity cannot be more than " + MAX_STACKED_QUANTITY);
        if (quantity <= 1) {
            livingEntity.setCustomName(null);
        } else {
            livingEntity.setCustomName(STACKED_PREFIX + quantity);
            livingEntity.setCustomNameVisible(false);
        }
    }

}