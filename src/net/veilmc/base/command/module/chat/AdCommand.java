package net.veilmc.base.command.module.chat;

import net.veilmc.base.command.BaseCommand;
import com.google.common.collect.Maps;
import net.minecraft.util.org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AdCommand
        extends BaseCommand
        implements Listener {
    private static final long AD_DELAY_MILLIS = TimeUnit.MINUTES.toMillis(5);
    private final Map<UUID, Long> AdMap;

    public AdCommand() {
        super("ad", "Advertise a youtube video");
        this.setUsage("/(command) <link>");
        this.AdMap = Maps.newHashMap();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        UUID uuid = player.getUniqueId();
        if (args.length == 0) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        long millis = System.currentTimeMillis();
        Long lastAd = this.AdMap.get(uuid);
        if (lastAd != null && lastAd - millis > 0) {
            sender.sendMessage(ChatColor.RED + "You have already advertised in the last " + DurationFormatUtils.formatDurationWords(AD_DELAY_MILLIS, true, true) + '.');
            return true;
        }
        this.AdMap.put(uuid, millis + AD_DELAY_MILLIS);
        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + sender.getName() + ChatColor.GREEN.toString() + ChatColor.BOLD + " " + ChatColor.GREEN
                .toString() + ChatColor.BOLD + args[0]);
        return true;
    }
}