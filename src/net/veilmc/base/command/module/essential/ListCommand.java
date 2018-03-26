package net.veilmc.base.command.module.essential;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ListCommand
		extends BaseCommand{
	private final String TO_BE_ON_LIST_PERMISSION;

	public ListCommand(){
		super("list", "Lists players online");
		this.setAliases(new String[]{"who"});
		this.setUsage("/(command)");
		this.TO_BE_ON_LIST_PERMISSION = "command.list.own";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		ArrayList<String> list = new ArrayList<String>();
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(ListCommand.canSee(sender, player)){
				if(BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isVanished()){
					list.add(ChatColor.GRAY + player.getName());
				}else if(player.hasPermission("command.list.own")){
					list.add(ChatColor.BLUE + player.getName());
				}
			}
		}
		sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		sender.sendMessage(ChatColor.AQUA + "There are currently " + ChatColor.BLUE.toString() + ChatColor.BOLD + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ChatColor.AQUA + " players online.");
		sender.sendMessage(" ");
		if(list.isEmpty()){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &f&l* &bStaff:" + " " + ChatColor.RED + "Not avaliable"));
		}else{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &f&l* &bStaff: &7(" + list.size() + ")" + " " + list.toString().replace("[", "").replace("]", "").replace(",", ChatColor.GRAY + ",")));
		}
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f&l * &bSupport: &fts.veilhcf.us"));
		sender.sendMessage(ChatColor.GRAY + BukkitUtils.STRAIGHT_LINE_DEFAULT);
		return true;
	}
}

