package com.customhcf.base.command.module.essential;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.event.StaffModeEvent;
import com.customhcf.base.user.BaseUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class StaffUtilitiesCommand extends BaseCommand
{
	BasePlugin plugin;
	public static ConcurrentHashMap<Player, ItemStack[]>  staffitems = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Player, ItemStack[]> staffarmor = new ConcurrentHashMap<>();


	public static ItemStack getRandomTeleport()
	{
		ItemStack is = new ItemStack(397, 1, (byte) 3);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Random Teleport " + ChatColor.GRAY + "(Right Click)");
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack getFreezeTool()
	{
		ItemStack is = new ItemStack(Material.PACKED_ICE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Freeze Player " + ChatColor.GRAY + "(Right Click)");
		is.setItemMeta(im);
		return is;
	}
	public static ItemStack getCompassTool()
	{
		ItemStack is = new ItemStack(Material.COMPASS);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Teleport " + ChatColor.GRAY + "(Right Click)");
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack getMinerTeleport() {
        ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "Miner Teleport " + ChatColor.GRAY + "(Right Click)");
        is.setItemMeta(im);
        return is;
    }

	public static ItemStack getBookTool()
	{
		ItemStack is = new ItemStack(Material.BOOK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "Inspect " + ChatColor.GRAY + "(Right Click)");
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack getCarpetTool()
	{
		ItemStack is = new ItemStack(Material.CARPET, 1, (byte) 14);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GRAY + " ");
		is.setItemMeta(im);
		return is;
	}

    public static ItemStack getNoWorldEditTool()
    {
        ItemStack is = new ItemStack(Material.WOOD_AXE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.GOLD + "World Edit " + ChatColor.RED + "(Not Avaliable)");
        is.setItemMeta(im);
        return is;
    }

	public static ItemStack getWorldEditTool()
	{
		ItemStack is = new ItemStack(Material.WOOD_AXE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.GOLD + "World Edit ");
		is.setItemMeta(im);
		return is;
	}
	public static ItemStack getVanishTool(boolean vanish)
	{
		if(vanish)
		{
			ItemStack is = new ItemStack(351, 1, (byte) 8);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.RED + "Disable Vanish " + ChatColor.GRAY + "(Right Click)");
			is.setItemMeta(im);
			return is;
		}
		else
		{
			ItemStack is = new ItemStack(351, 1, (byte) 10);
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.GREEN + "Enable Vanish " + ChatColor.GRAY + "(Right Click)");
			is.setItemMeta(im);
			return is;
		}

	}

	public StaffUtilitiesCommand(final BasePlugin plugin) {
		super("staffmode", "Turns on/off staffmode");
		this.setAliases(new String[] { "mod", "h", "staff" });
		this.plugin = plugin;
		this.setUsage("/(command)");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Only players.");
			return false;
		}

		final Player player = (Player)sender;
		final BaseUser user = this.plugin.getUserManager().getUser(player.getUniqueId());
		final StaffModeEvent playerFreezeEvent = new StaffModeEvent(player, !user.isStaffUtil());
		Bukkit.getServer().getPluginManager().callEvent(playerFreezeEvent);
		player.sendMessage(ChatColor.YELLOW + "Staff Mode has been set to " + !user.isStaffUtil() + ChatColor.YELLOW + ".");
		if(!user.isStaffUtil())
		{
			staffitems.put((Player)sender, ((Player) sender).getInventory().getContents());
			staffarmor.put((Player)sender, ((Player) sender).getInventory().getArmorContents());
			Player p = (Player)sender;
			p.getInventory().clear();
			p.getInventory().setItem(0, getCompassTool());
			p.getInventory().setItem(1, getBookTool());
			if(p.hasPermission("worldedit.*")) {
                p.getInventory().setItem(2, getWorldEditTool());
            } else {
                p.getInventory().setItem(2, getNoWorldEditTool());
            }

            user.setVanished(true);
			p.getInventory().setItem(3, getCarpetTool());
			p.getInventory().setItem(6, getFreezeTool());
			p.getInventory().setItem(8, getVanishTool(true));
            p.getInventory().setItem(7, getRandomTeleport());
            //p.getInventory().setItem(5, getMinerTeleport());

			p.setGameMode(GameMode.CREATIVE);
		}
		else
		{
			Player p = (Player)sender;
			p.getInventory().clear();
			if(staffitems.contains(player)){
				p.getInventory().setContents(staffitems.remove(sender));
				p.getInventory().setArmorContents(staffarmor.remove(sender));
			}
			p.setGameMode(GameMode.SURVIVAL);
		}
		user.setStaffUtil(!user.isStaffUtil());
		return true;
	}
}