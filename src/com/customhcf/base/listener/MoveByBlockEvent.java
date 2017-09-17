
package com.customhcf.base.listener;

import com.customhcf.base.event.PlayerMoveByBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveByBlockEvent
implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY() && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
            return;
        }
        Bukkit.getPluginManager().callEvent(new PlayerMoveByBlockEvent(e.getPlayer(), e.getTo(), e.getFrom()));
    }
}

