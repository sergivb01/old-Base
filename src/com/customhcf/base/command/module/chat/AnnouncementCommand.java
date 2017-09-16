
package com.customhcf.base.command.module.chat;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.ServerHandler;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.task.AnnouncementHandler;
import com.customhcf.util.BukkitUtils;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AnnouncementCommand
extends BaseCommand {
    private final BasePlugin plugin;
    private List<String> COMPLETIONS;
    private int MAX_ANNOUNCEMENT_PER_PAGE = 10;

    public AnnouncementCommand(BasePlugin plugin) {
        super("announcement", "Broadcasts a message to the server.");
        this.setAliases(new String[]{"announce", "ann"});
        this.COMPLETIONS = ImmutableList.of("add", "remove", "list", "delay");
        this.setUsage("/(command) <add|remove|list|delay> <text..|delay>");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                Integer number = 0;
                sender.sendMessage((Object)ChatColor.AQUA + "Announcement Lists: ");
                if (number <= this.MAX_ANNOUNCEMENT_PER_PAGE) {
                    for (String announce : this.plugin.getServerHandler().getAnnouncements()) {
                        sender.sendMessage(ChatColor.GRAY.toString() + " [" + (Object)ChatColor.YELLOW + number + (Object)ChatColor.GRAY + ']' + (Object)ChatColor.YELLOW + announce);
                        Integer n = number;
                        Integer n2 = number = Integer.valueOf(number + 1);
                    }
                } else {
                    sender.sendMessage((Object)ChatColor.YELLOW + "Too many to display!");
                    return true;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                String messagge = StringUtils.join((Object[])args, (char)' ', (int)1, (int)args.length);
                this.plugin.getServerHandler().getAnnouncements().add(ChatColor.translateAlternateColorCodes((char)'&', (String)messagge));
                Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Added " + messagge + " to the announcements."), (boolean)true);
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                int integer;
                try {
                    integer = Integer.parseInt(args[1]);
                }
                catch (NumberFormatException ex) {
                    sender.sendMessage((Object)ChatColor.RED + "Use /" + label + " list to get the number.");
                    return true;
                }
                Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Announcement number " + integer + " has been removed." + '\n' + (Object)ChatColor.GRAY + "(" + this.plugin.getServerHandler().getAnnouncements().get(integer) + ")"));
                this.plugin.getServerHandler().getAnnouncements().remove(integer);
                return true;
            }
            if (args[0].equalsIgnoreCase("delay")) {
                int integer;
                AnnouncementHandler announcementTask;
                try {
                    integer = Integer.parseInt(args[1]);
                }
                catch (NumberFormatException ex) {
                    sender.sendMessage((Object)ChatColor.RED + "Must be a number to set the delay.");
                    return true;
                }
                if (this.plugin.getServerHandler().getAnnouncementDelay() == integer) {
                    return true;
                }
                this.plugin.getServerHandler().setAnnouncementDelay(integer);
                BasePlugin.getPlugin().announcementTask.cancel();
                BasePlugin.getPlugin().announcementTask = announcementTask = new AnnouncementHandler(this.plugin);
                announcementTask.runTaskTimerAsynchronously((Plugin)BasePlugin.getPlugin(), (long)this.plugin.getServerHandler().getAnnouncementDelay(), (long)this.plugin.getServerHandler().getAnnouncementDelay());
                Command.broadcastCommandMessage((CommandSender)sender, (String)((Object)ChatColor.YELLOW + "Announcement Delay has been modified to " + integer));
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? BukkitUtils.getCompletions(args, this.COMPLETIONS) : Collections.emptyList();
    }
}

