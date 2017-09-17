
package com.customhcf.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.*;
import java.util.stream.Collectors;

public final class BukkitUtils {
    public static final String STRAIGHT_LINE_DEFAULT;
    private static final ImmutableMap<ChatColor, DyeColor> CHAT_DYE_COLOUR_MAP;
    private static final ImmutableSet<Object> DEBUFF_TYPES;
    private static final int DEFAULT_COMPLETION_LIMIT = 80;
    private static final String STRAIGHT_LINE_TEMPLATE;
    private static final String SPACED_TEMPLATE;

    public static int countColoursUsed(String id, boolean ignoreDuplicates) {
        ChatColor[] values = ChatColor.values();
        ArrayList<Character> charList = new ArrayList<Character>(values.length);
        for (ChatColor colour2 : values) {
            charList.add(Character.valueOf(colour2.getChar()));
        }
        int count = 0;
        HashSet<ChatColor> found = new HashSet<ChatColor>();
        for (int i = 1; i < id.length(); ++i) {
            ChatColor colour2;
            if (!charList.contains(Character.valueOf(id.charAt(i))) || id.charAt(i - 1) != '&' || !found.add(colour2 = ChatColor.getByChar(id.charAt(i))) && !ignoreDuplicates) continue;
            ++count;
        }
        return count;
    }

    public static List<String> getCompletions(String[] args, List<String> input) {
        return BukkitUtils.getCompletions(args, input, 80);
    }

    public static List<String> getCompletions(String[] args, List<String> input, int limit) {
        Preconditions.checkNotNull((Object)args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
    }

    public static String getDisplayName(CommandSender sender) {
        Preconditions.checkNotNull((Object)sender);
        return sender instanceof Player ? ((Player)sender).getDisplayName() : sender.getName();
    }

    public static long getIdleTime(Player player) {
        Preconditions.checkNotNull((Object)player);
        long idleTime = ((CraftPlayer)player).getHandle().x();
        return idleTime > 0 ? MinecraftServer.ar() - idleTime : 0;
    }

    public static DyeColor toDyeColor(ChatColor colour) {
        return CHAT_DYE_COLOUR_MAP.get(colour);
    }

    public static boolean hasMetaData(Metadatable metadatable, String input, Plugin plugin) {
        return BukkitUtils.getMetaData(metadatable, input, plugin) != null;
    }

    public static MetadataValue getMetaData(final Metadatable metadatable, final String input, final Plugin plugin) {
        return (MetadataValue) metadatable.getMetadata(input);
    }

    public static Player getFinalAttacker(EntityDamageEvent ede, boolean ignoreSelf) {
        Player attacker = null;
        if (ede instanceof EntityDamageByEntityEvent) {
            Projectile projectile;
            ProjectileSource shooter;
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)ede;
            Entity damager = event.getDamager();
            if (event.getDamager() instanceof Player) {
                attacker = (Player)damager;
            } else if (event.getDamager() instanceof Projectile && (shooter = (projectile = (Projectile)damager).getShooter()) instanceof Player) {
                attacker = (Player)shooter;
            }
            if (attacker != null && ignoreSelf && event.getEntity().equals(attacker)) {
                attacker = null;
            }
        }
        return attacker;
    }

    public static Player playerWithNameOrUUID(String string) {
        if (string == null) {
            return null;
        }
        return JavaUtils.isUUID(string) ? Bukkit.getPlayer(UUID.fromString(string)) : Bukkit.getPlayer(string);
    }

    @Deprecated
    public static OfflinePlayer offlinePlayerWithNameOrUUID(String string) {
        if (string == null) {
            return null;
        }
        return JavaUtils.isUUID(string) ? Bukkit.getOfflinePlayer(UUID.fromString(string)) : Bukkit.getOfflinePlayer(string);
    }

    public static boolean isWithinX(Location location, Location other, double distance) {
        return location.getWorld().equals(other.getWorld()) && Math.abs(other.getX() - location.getX()) <= distance && Math.abs(other.getZ() - location.getZ()) <= distance;
    }

    public static Location getHighestLocation(Location origin) {
        return BukkitUtils.getHighestLocation(origin, null);
    }

    public static Location getHighestLocation(Location origin, Location def) {
        Preconditions.checkNotNull((Object)origin, "The location cannot be null");
        Location cloned = origin.clone();
        World world = cloned.getWorld();
        int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            Block block;
            if ((block = world.getBlockAt(x, --y, z)).isEmpty()) continue;
            Location next = block.getLocation();
            next.setPitch(origin.getPitch());
            next.setYaw(origin.getYaw());
            return next;
        }
        return def;
    }

    public static boolean isDebuff(PotionEffectType type) {
        return DEBUFF_TYPES.contains(type);
    }

    public static boolean isDebuff(PotionEffect potionEffect) {
        return BukkitUtils.isDebuff(potionEffect.getType());
    }

    public static boolean isDebuff(ThrownPotion thrownPotion) {
        for (PotionEffect effect : thrownPotion.getEffects()) {
            if (!BukkitUtils.isDebuff(effect)) continue;
            return true;
        }
        return false;
    }

    static {
        STRAIGHT_LINE_TEMPLATE = ChatColor.STRIKETHROUGH.toString() + Strings.repeat("-", 256);
        SPACED_TEMPLATE = ChatColor.AQUA.toString() + " ";
        STRAIGHT_LINE_DEFAULT = STRAIGHT_LINE_TEMPLATE.substring(0, 55);

        CHAT_DYE_COLOUR_MAP = Maps.immutableEnumMap((Map)ImmutableMap.builder().put(ChatColor.AQUA, DyeColor.LIGHT_BLUE).put(ChatColor.BLACK, DyeColor.BLACK).put(ChatColor.BLUE, DyeColor.LIGHT_BLUE).put(ChatColor.DARK_AQUA, DyeColor.CYAN).put(ChatColor.DARK_BLUE, DyeColor.BLUE).put(ChatColor.DARK_GRAY, DyeColor.GRAY).put(ChatColor.DARK_GREEN, DyeColor.GREEN).put(ChatColor.DARK_PURPLE, DyeColor.PURPLE).put(ChatColor.DARK_RED, DyeColor.RED).put(ChatColor.GOLD, DyeColor.ORANGE).put(ChatColor.GRAY, DyeColor.SILVER).put(ChatColor.GREEN, DyeColor.LIME).put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA).put(ChatColor.RED, DyeColor.RED).put(ChatColor.WHITE, DyeColor.WHITE).put(ChatColor.YELLOW, DyeColor.YELLOW).build());
        DEBUFF_TYPES = ImmutableSet.builder().add(PotionEffectType.BLINDNESS).add(PotionEffectType.CONFUSION).add(PotionEffectType.HARM).add(PotionEffectType.HUNGER).add(PotionEffectType.POISON).add(PotionEffectType.SATURATION).add(PotionEffectType.SLOW).add(PotionEffectType.SLOW_DIGGING).add(PotionEffectType.WEAKNESS).add(PotionEffectType.WITHER).build();
    }
}

