package com.customhcf.base.listener;

import com.customhcf.base.BasePlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener{
    private BasePlugin plugin;

    public MotdListener(BasePlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerPing(final ServerListPingEvent event){ //TODO: More stuff
        event.setMotd(ChatColor.translateAlternateColorCodes('&', "Comming" + "\n" + "Soon"));
    }


}
