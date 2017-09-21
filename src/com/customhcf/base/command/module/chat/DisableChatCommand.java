
package com.customhcf.base.command.module.chat;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.util.JavaUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.concurrent.TimeUnit;

public class DisableChatCommand
extends BaseCommand {
    private static final long DEFAULT_DELAY;
    private final BasePlugin plugin;

    public DisableChatCommand(BasePlugin plugin) {
        super("disablechat", "Disables the chat for non-staff.");
        this.setAliases(new String[]{"mutechat", "restrictchat", "mc", "rc"});
        this.setUsage("/(command)");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        long oldTicks = this.plugin.getServerHandler().getRemainingChatDisabledMillis();
        long newTicks;
        if (oldTicks > 0L) {
            newTicks = 0L;
        } else if (args.length < 1) {
            newTicks = DisableChatCommand.DEFAULT_DELAY;
        } else {
            newTicks = JavaUtils.parse(StringUtils.join(args, ' ', 0, args.length));
            if (newTicks == -1L) {
                sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m1s");
                return true;
            }
        }
        this.plugin.getServerHandler().setChatDisabledMillis(newTicks);
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Global chat is now " + ((newTicks > 0L) ? (ChatColor.RED + "disabled" + ChatColor.YELLOW + " for " + ChatColor.GOLD + DurationFormatUtils.formatDurationWords(newTicks, true, true)) : (ChatColor.GREEN + "enabled")));
        return true;
    }

    static {
        DEFAULT_DELAY = TimeUnit.MINUTES.toMillis(3L);
    }
}

