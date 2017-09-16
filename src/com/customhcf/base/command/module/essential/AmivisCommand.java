
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BaseConstants;
import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.user.BaseUser;
import com.customhcf.base.user.UserManager;
import com.customhcf.util.BukkitUtils;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AmivisCommand
extends BaseCommand {
    private final BasePlugin plugin;

    public AmivisCommand(BasePlugin plugin) {
        super("amivis", "Check if a player is visible.");
        this.setUsage("/(command) <playerName> [targetName]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean vanished;
        Player target;
        if (args.length > 0) {
            target = BukkitUtils.playerWithNameOrUUID(args[0]);
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.getUsage(label));
                return true;
            }
            target = (Player)sender;
        }
        if (target == null || !BaseCommand.canSee(sender, target)) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }
        sender.sendMessage((Object)ChatColor.YELLOW + target.getName() + " is " + ((vanished = this.plugin.getUserManager().getUser(target.getUniqueId()).isVanished()) ? "in vanish" : "not in vanish") + '.');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

