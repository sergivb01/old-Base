package net.veilmc.base.listener;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.user.BaseUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener
		implements Listener{
	private final BasePlugin plugin;

	public JoinListener(BasePlugin plugin){
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
		if(this.plugin.getServerManager().Lockdo)
			for(Player player1 : Bukkit.getServer().getOnlinePlayers()){
				if(!player1.hasPermission("command.staffchat")) continue;
				if(!baseUser.getNotes().isEmpty()){
					player1.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.BOLD + " has the following notes" + ChatColor.RED + '\u2193');
				}
				for(String notes : baseUser.getNotes()){
					player1.sendMessage(notes);
				}
			}

		baseUser.tryLoggingName(player);
		baseUser.tryLoggingAddress(player.getAddress().getAddress().getHostAddress());
	}

}

