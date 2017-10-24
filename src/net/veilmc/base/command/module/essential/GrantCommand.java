package net.veilmc.base.command.module.essential;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.JavaUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Arrays;

public class GrantCommand extends BaseCommand implements Listener{
    private BasePlugin plugin;
    public Inventory main = Bukkit.createInventory(null, 27, "Permission Manager");

    public GrantCommand(BasePlugin plugin) {
        super("grant", "Assign player a rank.");
        this.plugin  = plugin;
        this.setUsage("/(command) <playerName>");
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "Player only.");
            return false;
        }

        Player player = (Player)sender;

        if (args.length < 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cIncorrect usage! &eUse like this: &b/grant <player>"));
            return true;
        }

       /* OfflinePlayer tg = Bukkit.getOfflinePlayer(args[0]);

        if (tg.getPlayer() == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError: Player not found!"));
            return true;
        }

        final long duration = JavaUtils.parse(args[1]);
        if (duration == -1L && (!(args[1].equalsIgnoreCase("permanent")))) {
            player.sendMessage(ChatColor.RED + "'" + args[0] + "' is an invalid duration.");
            return true;
        }

        if (duration < 3600000L && (!(args[1].equalsIgnoreCase("permanent")))) {
            player.sendMessage(ChatColor.RED + "Rank duration must last for at least 1 hour.");
            return true;
        }*/

        int i = 0;
        PermissionGroup currentGroup  = PermissionsEx.getUser(tg.getPlayer()).getGroups()[0];
        for(PermissionGroup permissionGroup : PermissionsEx.getPermissionManager().getGroupList()){
            ItemStack is = new ItemStack(351, 1, (short) (currentGroup  == permissionGroup ? 10 : 8));
            ItemMeta meta = is.getItemMeta();
            meta.setLore(Arrays.asList((currentGroup == permissionGroup ? ChatColor.translateAlternateColorCodes('&', "&eThis player already has " + permissionGroup.getPrefix().replace("[","").replace("]","").replace(" ", "") + " &erank") : ChatColor.translateAlternateColorCodes('&', "&eSet this player's group " + permissionGroup.getPrefix().replace("[","").replace("]",""))), (ChatColor.GRAY + tg.getName())));
            meta.setDisplayName(ChatColor.GOLD.toString() + permissionGroup.getName());
            is.setItemMeta(meta);
            main.setItem(i, is);
            i++;
        }
        player.openInventory(main);

        return true;
    }


    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(!event.getClickedInventory().getName().equalsIgnoreCase("permission manager")){
            return;
        }

        if(event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta() || event.getCurrentItem().getType().equals(Material.AIR)){
            return;
        }

        Player player = (Player)event.getWhoClicked();
        String rank = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals(rank)) {
                String target = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(1));
               // Long dur = Long.parseLong(duration);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + target + " group set " + rank);
                Command.broadcastCommandMessage(player, ChatColor.translateAlternateColorCodes('&',"&eYou have set &a" + target + "'s&e group to &a" + rank));
                player.closeInventory();
            }
        event.setCancelled(true);
    }
}