package net.veilmc.base.listener;

import net.veilmc.base.BasePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class AutoMuteListener implements Listener {

    private final BasePlugin plugin;
    public AutoMuteListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    private int cooldownToAdvert = 3;
    private int cooldownToRemoveAdvert = 600;
    private HashMap<String, Integer> messageCount = new HashMap<>();
    private HashMap<String, Integer> totalAdverts = new HashMap<>();

    @EventHandler
    private void autoMute(AsyncPlayerChatEvent event) {
        long remainingChatSlowed = this.plugin.getServerHandler().getRemainingChatSlowedMillis();
        long remainingChatDisabled = this.plugin.getServerHandler().getRemainingChatDisabledMillis();
        if (!(remainingChatDisabled > 0) || !(remainingChatSlowed > 0)) {
            Player player = event.getPlayer();
            String playername = player.getName();
            if (player.hasPermission("automute.bypass")) return;
            if (!messageCount.containsKey(playername)) toAdvert(playername);
            addMessage(playername);
            if (messageCount.get(playername) == 3) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Please slow down!");
            }
            if (messageCount.get(playername) == 4) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You have recieved an advert for spamming.");
                if (!messageCount.containsKey(playername)) toRemoveAdverts(playername);
                addAdvert(playername);
            }

        }
    }
    private void addMessage(final String playername) {
        int messageInt;
        if (messageCount.containsKey(playername)) {
            messageInt = messageCount.get(playername);
            messageCount.remove(playername);
        } else messageInt = 0;
        messageInt++;
        messageCount.put(playername, messageInt);
    }

    private void addAdvert(final String playername) {
        int advertsInt;
        if (totalAdverts.containsKey(playername)) {
            advertsInt = totalAdverts.get(playername);
            totalAdverts.remove(playername);
        } else advertsInt = 0;
        advertsInt++;
        totalAdverts.put(playername, advertsInt);
        if (totalAdverts.get(playername) == 2) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mute " + playername + " 5m [AutoMute] Spam -s");
            totalAdverts.remove(playername);
        }
    }

    private void toAdvert(final String playername) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(BasePlugin.getPlugin(), () -> messageCount.remove(playername), cooldownToAdvert * 20);
    }

    private void toRemoveAdverts(final String playername) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(BasePlugin.getPlugin(), () -> totalAdverts.remove(playername), cooldownToRemoveAdvert * 20);
    }
}