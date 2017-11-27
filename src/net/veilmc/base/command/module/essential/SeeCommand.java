package net.veilmc.base.command.module.essential;

import net.veilmc.base.BaseConstants;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import net.veilmc.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class SeeCommand extends BaseCommand {

    private final BasePlugin plugin;
    public Inventory page1 = Bukkit.createInventory(null, 36, "Punishment Manager");

    public SeeCommand(BasePlugin plugin) {
        super("see", "Blah");
        this.setUsage("/(command) <player>");
        this.plugin = plugin;
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

        new Thread(()->{
            /*Bukkit.getScheduler().runTaskAsynchronously(this.plugin, ()->{


                boolean banned = Database.get().isPlayerBanned(uuid, "null");
                boolean muted = Database.get().isPlayerMuted(uuid, "null");
                p.sendMessage("start");
                if(banned) {
                    p.sendMessage("banned");
                } else {
                    p.sendMessage("NOT banned");
                }
                if(muted) {
                    p.sendMessage("muted");
                } else {
                    p.sendMessage("NOT muted");
                }
                p.sendMessage("end");
                try (PreparedStatement st = Database.get().prepareStatement("SELECT * FROM {bans}")) {
                    try (ResultSet rs = st.executeQuery()) {

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });*/
        }).start();

        return true;
    }
}