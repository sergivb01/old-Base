
package net.veilmc.base.command.module.chat;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class BroadcastCommand
extends BaseCommand {
    private final BasePlugin plugin;
    private final List<String> COMPLETIONS_FIRST = ImmutableList.of("-raw");

    public BroadcastCommand(BasePlugin plugin) {
        super("broadcast", "Broadcasts a message to the server.");
        this.setAliases(new String[]{"bc"});
        this.setUsage("/(command) [-raw *sendRaw*] <text..>");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int position;
        boolean raw;
        if (args.length < 1) {
            sender.sendMessage(this.getUsage(label));
            return true;
        }
        if (args.length > 1 && args[0].startsWith("-raw")) {
            position = 1;
            raw = true;
        } else {
            position = 0;
            raw = false;
        }
        String message = StringUtils.join(args, ' ', position, args.length);
        if (raw) {
            if (message.length() < 3) {
                sender.sendMessage(ChatColor.RED + "Character limit not met, must have atleast 3 characters.");
                return true;
            }
        } else if (message.length() < 4) {
            sender.sendMessage(ChatColor.RED + "Character limit not met, must have atleast 4 characters.");
            return true;
        }
        message = !raw ? ChatColor.translateAlternateColorCodes('&', String.format(Locale.ENGLISH, this.plugin.getServerHandler().getBroadcastFormat(), message)) : ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Veil " + ChatColor.GRAY + "»" + ChatColor.YELLOW + message);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? BukkitUtils.getCompletions(args, this.COMPLETIONS_FIRST) : Collections.emptyList();
    }
}

