package net.veilmc.base.command.module.essential;

//import me.sergivb01.giraffe.Giraffe;

import net.veilmc.base.BaseConstants;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class BoomCommand
		extends BaseCommand{
	final BasePlugin plugin;

	public BoomCommand(BasePlugin plugin){
		super("boom", "Hackers goes boom.");
		this.plugin = plugin;
		this.setUsage("/(command) <playerName>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(sender instanceof Player){
			sender.sendMessage(ChatColor.RED + "This command is only executable by console.");
			return true;
		}
		if(args.length < 1){
			sender.sendMessage(this.getUsage(label));
			return true;
		}
		final Player p = BukkitUtils.playerWithNameOrUUID(args[0]);
		if(p == null || !BaseCommand.canSee(sender, p)){
			sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
			return true;
		}
		Location loc = p.getLocation();
		p.setVelocity(new Vector(0.0D, 10.0D, 0.0D));
		Firework firework = p.getWorld().spawn(loc, Firework.class);
		FireworkMeta data = firework.getFireworkMeta();
		data.addEffects(FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BALL_LARGE).trail(false).build());
		data.setPower(2);
		firework.setFireworkMeta(data);

		for(int i = 1; i < 11; i++){
			loc.getWorld().playEffect(loc, Effect.LARGE_SMOKE, 50);
			loc.getWorld().playEffect(loc, Effect.EXPLOSION, 50);
			loc.getWorld().playEffect(loc, Effect.SMALL_SMOKE, 50);
		}
		Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(this.plugin, () -> Bukkit.dispatchCommand(sender, "ban " + p.getName() + " Cheating -s"), 20L);
		return true;
	}
}

