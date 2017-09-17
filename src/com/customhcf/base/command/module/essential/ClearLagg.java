
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.task.ClearEntityHandler;
import com.google.common.primitives.Ints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClearLagg
extends BaseCommand {
    public ClearLagg() {
        super("clearlagg", "Clears the lag on the server");
        this.setAliases(new String[]{"cl", "laggclear", "clearlag", "clag"});
        this.setUsage("/(command) [Delay]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        if (args.length == 1) {
            ClearEntityHandler clearEntityHandler;
            if (Ints.tryParse(args[0]) == null) {
                sender.sendMessage(ChatColor.RED + "Must be a number");
                return true;
            }
            BasePlugin.getPlugin().getClearEntityHandler().cancel();
            BasePlugin.getPlugin().getClearEntityHandler().runTaskTimer(BasePlugin.getPlugin(), (long) Ints.tryParse(args[0]), (long) Ints.tryParse(args[0]));
            Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Changed the Clear Lag From " + BasePlugin.getPlugin().getServerHandler().getClearlagdelay() + " To " + Ints.tryParse(args[0]), true);
            BasePlugin.getPlugin().getServerHandler().setClearlagdelay(Ints.tryParse(args[0]));
            return true;
        }
        return true;
    }
}

