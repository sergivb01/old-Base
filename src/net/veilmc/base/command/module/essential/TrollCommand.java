package net.veilmc.base.command.module.essential;

import net.veilmc.base.BaseConstants;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TrollCommand extends BaseCommand {
    private BasePlugin plugin;

    public TrollCommand(BasePlugin plugin) {
        super("troll", "Blah");
        this.setUsage("/(command) <player>");
        this.plugin = plugin;
    }

    public void Troll(Player player) {
        final String path = Bukkit.getServer().getClass().getPackage().getName();
        final String version = path.substring(path.lastIndexOf(".") + 1, path.length());
        try {
            final Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            final Class<?> PacketPlayOutGameStateChange = Class.forName("net.minecraft.server." + version + ".PacketPlayOutGameStateChange");
            final Class<?> Packet = Class.forName("net.minecraft.server." + version + ".Packet");
            final Constructor<?> playOutConstructor = PacketPlayOutGameStateChange.getConstructor(Integer.TYPE, Float.TYPE);
            final Object packet = playOutConstructor.newInstance(5, 0);
            final Object craftPlayerObject = craftPlayer.cast(player);
            final Method getHandleMethod = craftPlayer.getMethod("getHandle", (Class<?>[])new Class[0]);
            final Object handle = getHandleMethod.invoke(craftPlayerObject, new Object[0]);
            final Object pc = handle.getClass().getField("playerConnection").get(handle);
            final Method sendPacketMethod = pc.getClass().getMethod("sendPacket", Packet);
            sendPacketMethod.invoke(pc, packet);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        if(args[0].equalsIgnoreCase("all")) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Troll(player);
                }
                sender.sendMessage(ChatColor.GREEN + "You have sent the message to every player!");
            });
            return true;
        }
        final Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
        if (target == null) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }

        Troll(target);
        sender.sendMessage(ChatColor.YELLOW + "You are a fucking troller lolololoololo packets g0d");
        return true;
    }
}