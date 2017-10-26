package net.veilmc.base.command.module.chat;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LegalModsCommand
        extends BaseCommand {

    public LegalModsCommand(BasePlugin plugin) {
        super("legalmods", "Check legal mods.");
        this.setUsage("/(command)");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &f&l* &6&lVeilMC Legal Mods &f&l*"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e  veilhcf.us/legalmods"));
        return true;
    }
}