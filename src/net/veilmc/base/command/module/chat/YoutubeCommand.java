package net.veilmc.base.command.module.chat;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class YoutubeCommand
		extends BaseCommand{

	public YoutubeCommand(BasePlugin plugin){
		super("youtube", "Check requirements");
		this.setUsage("/(command)");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "YouTuber Requirments");
		sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.WHITE + "1000 Subscribers");
		sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.WHITE + "1 Video");
		sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		return true;
	}
}