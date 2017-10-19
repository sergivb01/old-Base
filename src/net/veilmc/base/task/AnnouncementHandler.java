
package net.veilmc.base.task;

import net.veilmc.base.BasePlugin;

import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AnnouncementHandler
extends BukkitRunnable {
    private final BasePlugin plugin;

    public AnnouncementHandler(BasePlugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        List<String> announcements = this.plugin.getServerHandler().getAnnouncements();
        if (!announcements.isEmpty()) {
            String next = announcements.get(0);
            Bukkit.broadcastMessage(next);
            Collections.rotate(announcements, -1);
        }
    }
}
