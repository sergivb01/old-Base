
package com.customhcf.base.command.module.chat;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.JavaUtils;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SlowChatCommand
extends BaseCommand {
    private static final long DEFAULT_DELAY = TimeUnit.MINUTES.toMillis(5);
    private final BasePlugin plugin;

    public SlowChatCommand(BasePlugin plugin) {
        super("slowchat", "Slows the chat down for non-staff.");
        this.setAliases(new String[]{"slow"});
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Long newTicks;
        long oldTicks = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
        if (oldTicks > 0) {
            newTicks = (long) 0;
        } else if (args.length < 1) {
            newTicks = DEFAULT_DELAY;
        } else {
            newTicks = JavaUtils.parse(args[0]);
            if (newTicks == -1) {
                sender.sendMessage((Object)ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
                return true;
            }
        }
        this.plugin.getServerHandler().setChatSlowedMillis(newTicks);
        Bukkit.broadcastMessage((String)((Object)ChatColor.YELLOW + "Global chat is " + (newTicks > 0 ? new StringBuilder().append("has slowed down for ").append(DurationFormatUtils.formatDurationWords((long)newTicks, (boolean)true, (boolean)true)).toString() : new StringBuilder().append((Object)ChatColor.YELLOW).append("no longer slowed").toString()) + (Object)ChatColor.YELLOW + '.'));
        return true;
    }
}

