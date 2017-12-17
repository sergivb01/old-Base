
package net.veilmc.base.command.module.essential;

import net.veilmc.base.BaseConstants;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.base.user.BaseUser;
import net.veilmc.util.API;
import net.veilmc.util.BukkitUtils;
import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public VanishCommand(BasePlugin plugin) {
        super("vanish", "Hide from other players.");
        this.setAliases(new String[]{"v", "vis", "vanish", "invis"});
        this.setUsage("/(command) [playerName]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (args.length > 0 && sender.hasPermission(command.getPermission() + ".others")) {
            target = BukkitUtils.playerWithNameOrUUID(args[0]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        if (target == null || sender instanceof Player && !((Player)sender).canSee(target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        BaseUser baseUser = this.plugin.getUserManager().getUser(target.getUniqueId());
        Player p = (Player)sender;
        boolean newVanished = !baseUser.isVanished() || args.length > 1 && Boolean.parseBoolean(args[1]);
        baseUser.setVanished(target, newVanished, true);
        if(baseUser.isStaffUtil()) {
            p.getInventory().setItem(8, StaffUtilitiesCommand.getVanishTool(newVanished));
            p.sendMessage("changed");
        }
        sender.sendMessage(API.Prefix_staff + ChatColor.YELLOW + "Vanish mode of " + target.getName() + " set to " + newVanished + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

