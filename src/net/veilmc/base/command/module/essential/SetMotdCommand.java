
package net.veilmc.base.command.module.essential;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SetMotdCommand
        extends BaseCommand {
    private final BasePlugin plugin;

    public SetMotdCommand(BasePlugin plugin) {
        super("vanish", "Hide from other players.");
        this.setAliases(new String[]{"v", "vis", "vanish", "invis"});
        this.setUsage("/(command) [playerName]");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.RED + "wip"); //TODO: More stuff
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 1 ? null : Collections.emptyList();
    }
}

