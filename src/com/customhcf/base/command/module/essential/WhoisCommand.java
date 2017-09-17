// 

// 

package com.customhcf.base.command.module.essential;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.BasePlugin;
import com.customhcf.base.StaffPriority;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.user.BaseUser;
import com.customhcf.util.BukkitUtils;
import com.customhcf.util.chat.ClickAction;
import com.customhcf.util.chat.Text;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WhoisCommand extends BaseCommand
{
    private static final Map<Integer, String> CLIENT_PROTOCOL_IDS;
    private final BasePlugin plugin;
    
    public WhoisCommand(final BasePlugin plugin) {
        super("whois", "Check information about a player.");
        this.plugin = plugin;
        this.setUsage("/(command) [player]");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        final Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        final Location location = target.getLocation();
        final World world = location.getWorld();
        final BaseUser baseUser = this.plugin.getUserManager().getUser(target.getUniqueId());
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        sender.sendMessage(ChatColor.GREEN + " [" + target.getDisplayName() + ChatColor.GREEN + ']');
        sender.sendMessage(ChatColor.YELLOW + "  Health: " + ChatColor.AQUA + target.getHealth() + '/' + target.getMaxHealth());
        sender.sendMessage(ChatColor.YELLOW + "  Hunger: " + ChatColor.AQUA + target.getFoodLevel() + '/' + 20 + " (" + target.getSaturation() + " saturation)");
        sender.sendMessage(ChatColor.YELLOW + "  Exp/Level: " + ChatColor.AQUA + target.getExp() + '/' + target.getLevel());
        sender.sendMessage(ChatColor.YELLOW + "  Location: " + ChatColor.AQUA + world.getName() + ' ' + ChatColor.GRAY + '[' + WordUtils.capitalizeFully(world.getEnvironment().name().replace('_', ' ')) + "] " + ChatColor.GOLD + '(' + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ')');
        sender.sendMessage(ChatColor.YELLOW + "  Vanished: " + ChatColor.AQUA + baseUser.isVanished() + " (priority=" + StaffPriority.of(target).getPriorityLevel() + ')');
        sender.sendMessage(ChatColor.YELLOW + "  Staff Chat: " + ChatColor.AQUA + baseUser.isInStaffChat());
        sender.sendMessage(ChatColor.YELLOW + "  Operator: " + ChatColor.AQUA + target.isOp());
        if (PermissionsEx.getPlugin() != null) {
            sender.sendMessage(ChatColor.YELLOW + "  * Permission: " + (PermissionsEx.getUser(target).has("*") ? (ChatColor.AQUA + "True") : (ChatColor.AQUA + "False")));
        }
        sender.sendMessage(ChatColor.YELLOW + "  Staff Mode: " + ChatColor.AQUA + baseUser.isStaffUtil());
        sender.sendMessage(ChatColor.YELLOW + "  GameMode: " + ChatColor.AQUA + WordUtils.capitalizeFully(target.getGameMode().name().replace('_', ' ')));
        sender.sendMessage(ChatColor.YELLOW + "  Idle Time: " + ChatColor.AQUA + DurationFormatUtils.formatDurationWords(BukkitUtils.getIdleTime(target), true, true));
        sender.sendMessage(ChatColor.YELLOW + "  IP4 Address: " + ChatColor.RED + "" + ChatColor.STRIKETHROUGH + "*********");
        if (baseUser.getNotes() != null) {
            if (!baseUser.getNotes().isEmpty()) {
                if (baseUser.getNotes().size() > 1) {
                    new Text(ChatColor.YELLOW + "  Note: " + ChatColor.AQUA + baseUser.getNotes().get(0) + ChatColor.GRAY + " [Click for more notes]").setHoverText(ChatColor.AQUA + "Click me to view " + target.getName() + "'s " + baseUser.getNotes().size() + " more notes.").setClick(ClickAction.RUN_COMMAND, "/note check " + target.getName()).send(sender);
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "  Notes: " + ChatColor.AQUA + baseUser.getNotes().get(0));
                }
            } else {
                sender.sendMessage(ChatColor.YELLOW + "  Notes: " + ChatColor.RED + "none");
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "  Notes: " + ChatColor.RED + "none");
        }
        final int version = ((CraftPlayer) target).getHandle().playerConnection.networkManager.getVersion();
        sender.sendMessage(ChatColor.YELLOW + "  Client Version: " + ChatColor.AQUA + version + ChatColor.GRAY + " [" + MoreObjects.firstNonNull(WhoisCommand.CLIENT_PROTOCOL_IDS.get(version), "Unknown (check at http://wiki.vg/Protocol_version_numbers)") + "]");
        sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        return true;
    }


    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    static {
        CLIENT_PROTOCOL_IDS = ImmutableMap.of(4, "1.7.5", 5, "1.7.10", 47, "1.8");
    }
}
