package net.veilmc.base.command.module.essential;

import net.veilmc.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LockdownCommand
		extends BaseCommand

{
	private final String TO_BE_ON_LIST_PERMISSION;

	public ListCommand(){
		super("lockdown", "Puts the server on high alert");
		this.setUsage("/(command)");
		this.TO_BE_ON_LIST_PERMISSION = "command.lockdown";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){


	}
}
