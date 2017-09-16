
package com.customhcf.base.command.module.essential;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.task.ClearEntityHandler;
import com.google.common.primitives.Ints;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
            if (Ints.tryParse((String)args[0]) == null) {
                sender.sendMessage((Object)ChatColor.RED + "Must be a number");
                return true;
            }
            BasePlugin.getPlugin().clearEntityHandler.cancel();
            BasePlugin.getPlugin().clearEntityHandler = clearEntityHandler = new ClearEntityHandler();
            clearEntityHandler.runTaskTimer((Plugin)BasePlugin.getPlugin(), (long)Ints.tryParse((String)args[0]).intValue(), (long)Ints.tryParse((String)args[0]).intValue());
            Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Changed the Clear Lag From " + BasePlugin.getPlugin().getServerHandler().getClaggDelay() + " To " + Ints.tryParse((String)args[0])), (boolean)true);
            BasePlugin.getPlugin().getServerHandler().setClearlagdelay(Ints.tryParse((String)args[0]));
            return true;
        }
        return true;
    }
}

