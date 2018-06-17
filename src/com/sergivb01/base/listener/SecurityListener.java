package com.sergivb01.base.listener;

import com.sergivb01.base.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class SecurityListener implements Listener{


	@EventHandler
	public void preProcessCommand(PlayerCommandPreprocessEvent event){

		if(event.getPlayer().isOp() && BasePlugin.getChat().playerInGroup(event.getPlayer(), "default")){
			event.setCancelled(true);
			Command.broadcastCommandMessage(Bukkit.getConsoleSender(), ChatColor.YELLOW + event.getPlayer().getName() + " has been detected as a security risk", false);
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + event.getPlayer().getName() + " Detected: Security Risk -s");
		}
	}


}
