
package com.customhcf.base.command.module.chat;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.JavaUtils;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DisableChatCommand
extends BaseCommand {
    private static final long DEFAULT_DELAY = TimeUnit.MINUTES.toMillis(5);
    private final BasePlugin plugin;

    public DisableChatCommand(BasePlugin plugin) {
        super("disablechat", "Disables the chat for non-staff.");
        this.setAliases(new String[]{"mutechat", "restrictchat", "mc", "rc"});
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        long newTicks;
        long oldTicks = this.plugin.getServerHandler().getRemainingChatDisabledMillis();
        if (oldTicks > 0) {
            newTicks = 0;
        } else if (args.length < 1) {
            newTicks = DEFAULT_DELAY;
        } else {
            newTicks = JavaUtils.parse(StringUtils.join((Object[])args, (char)' ', (int)0, (int)args.length));
            if (newTicks == -1) {
                sender.sendMessage((Object)ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
                return true;
            }
        }
        this.plugin.getServerHandler().setChatDisabledMillis(newTicks);
        Bukkit.broadcastMessage((String)((Object)ChatColor.YELLOW + "Global chat is " + (newTicks > 0 ? new StringBuilder().append("disabled for ").append(DurationFormatUtils.formatDurationWords((long)newTicks, (boolean)true, (boolean)true)).toString() : "no longer disabled") + (Object)ChatColor.YELLOW + '.'));
        return true;
    }
}

