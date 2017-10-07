package com.customhcf.base.listener;

import com.avaje.ebeaninternal.server.cluster.PacketTransactionEvent;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDamageListener implements Listener {

    @EventHandler
    public void onEndermanDamage(EntityDamageByEntityEvent event){
        if((event.getEntity() instanceof Player) && (event.getDamager() instanceof Enderman)) event.setCancelled(true);
    }

}
