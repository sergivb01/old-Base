package net.veilmc.base.command.module.chat;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MuteWordCommand
		extends BaseCommand{
	private final BasePlugin plugin;
	private final Config config;

	public MuteWordCommand(BasePlugin plugin){
		super("muteword", "Stops people saying certain words.");
		this.setAliases(new String[]{"mw"});
		this.setUsage("/(command) <add/list/remove> [word]");
		this.plugin = plugin;

		this.config = new Config(plugin, "mute-word");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(this.getUsage());
			return true;
		}
		if(args[0].equalsIgnoreCase("add")){
			if(!(args[1] == null)){
				this.config.set("mute-word." + args[1], 0);
				sender.sendMessage("DONE");
				return true;
			}
			sender.sendMessage(args[0]);
			return true;
		}else if(args[0].equalsIgnoreCase("remove")){
			sender.sendMessage(args[0]);
			return true;
		}else if(args[0].equalsIgnoreCase("list")){
			sender.sendMessage(args[0]);
			return true;


		}else{
			sender.sendMessage(this.getUsage());
			return true;
		}

	}
}