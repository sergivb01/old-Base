package net.veilmc.base.command.module.essential;

import litebans.api.Database;
import net.veilmc.base.BaseConstants;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import net.veilmc.util.menu.Menu;
import net.veilmc.util.menu.mask.Mask;
import net.veilmc.util.menu.mask.Mask2D;
import net.veilmc.util.menu.type.ChestMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.awt.SystemColor.menu;

public class SeeCommand extends BaseCommand {

    private final BasePlugin plugin;
    public SeeCommand(BasePlugin plugin) {
        super("see", "Blah");
        this.setUsage("/(command) <player>");
        this.plugin = plugin;
    }


    public Menu createMenu() {
        return ChestMenu.builder(4)
                .title(ChatColor.translateAlternateColorCodes('&', "&bPunishment GUI"))
                .build();
    }
    public void displayMenu(Player player) {
        Menu menu = createMenu();
        menu.open(player);
    }
    public void addWhiteBorder(Menu menu) {
        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
        for (int i = 0 ; i < 9 ; i++) {
            menu.getSlot(i).setItem(glass);
        }
        menu.getSlot(17).setItem(glass);
        menu.getSlot(18).setItem(glass);
        for (int i = 26 ; i < 36 ; i++) {
            menu.getSlot(i).setItem(glass);
        }
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        Player p = (Player)sender;

        if (args.length < 1) {
            sender.sendMessage(this.getUsage());
            return true;
        }
        final OfflinePlayer target = BukkitUtils.offlinePlayerWithNameOrUUID(args[0]);
        if (target == null) {
            sender.sendMessage(String.format(BaseConstants.PLAYER_WITH_NAME_OR_UUID_NOT_FOUND, args[0]));
            return true;
        }

        UUID uuid = target.getUniqueId();

        createMenu();
        addWhiteBorder(createMenu());
        displayMenu(p);
        p.sendMessage("OPEN");
//        new Thread(()->{
//            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, ()->{
//
//
//                boolean banned = Database.get().isPlayerBanned(uuid, "null");
//                boolean muted = Database.get().isPlayerMuted(uuid, "null");
//                p.sendMessage("start");
//                if(banned) {
//                    p.sendMessage("banned");
//                } else {
//                    p.sendMessage("NOT banned");
//                }
//                if(muted) {
//                    p.sendMessage("muted");
//                } else {
//                    p.sendMessage("NOT muted");
//                }
//                p.sendMessage("end");
//               // createMenu();
//                addWhiteBorder(createMenu());
//                displayMenu(p);
//
//            });
//        }).start();

        return true;
    }
}