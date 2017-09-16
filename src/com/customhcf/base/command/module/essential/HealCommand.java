// 

// 

package com.customhcf.base.command.module.essential;

import com.customhcf.base.command.BaseCommand;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import com.customhcf.base.BaseConstants;
import com.customhcf.util.BukkitUtils;
import com.customhcf.base.ServerHandler;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;
import java.util.Set;

import javax.security.auth.login.Configuration;

public class HealCommand extends BaseCommand
{
    private static final Set<PotionEffectType> HEALING_REMOVEABLE_POTION_EFFECTS;
    
    public HealCommand() {
        super("heal", "Heals a player.");
        this.setUsage("/(command) <playerName>");
    }
    
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player onlyTarget = null;
        Collection<Player> targets;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            if (args[0].equalsIgnoreCase("all") && sender.hasPermission(command.getPermission() + ".all")) {
                targets = ImmutableSet.copyOf(Bukkit.getOnlinePlayers());
            }
            else {
                if ((onlyTarget = BukkitUtils.playerWithNameOrUUID(args[0])) == null || !BaseCommand.canSee(sender, onlyTarget)) {
                    sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
                    return true;
                }
                targets = ImmutableSet.of(onlyTarget);
            }
        }
        else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            targets = ImmutableSet.of((onlyTarget = (Player)sender));
        }
        final double maxHealth;
        if (onlyTarget != null && (maxHealth = ((Damageable)onlyTarget).getHealth()) == ((Damageable)onlyTarget).getMaxHealth()) {
            sender.sendMessage(ChatColor.RED + onlyTarget.getName() + " already has full health (" + maxHealth + ").");
            return true;
        }
        for (final Player target : targets) {
            target.setHealth(((Damageable)target).getMaxHealth());
            for (final PotionEffectType type : HealCommand.HEALING_REMOVEABLE_POTION_EFFECTS) {
                target.removePotionEffect(type);
            }
            target.setFireTicks(0);
        }
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Healed " + ((onlyTarget == null) ? "all online players" : onlyTarget.getName()) + '.');
        return true;
    }
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        return (args.length == 1) ? null : Collections.emptyList();
    }
    
    static {
        HEALING_REMOVEABLE_POTION_EFFECTS = (Set)ImmutableSet.of(PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.POISON, PotionEffectType.WEAKNESS);
    }
}
