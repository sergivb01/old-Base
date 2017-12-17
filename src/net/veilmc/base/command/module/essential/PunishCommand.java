package net.veilmc.base.command.module.essential;

// Created by iDaniel84
// Created by iDaniel84
// Created by iDaniel84
// Created by iDaniel84
// Created by iDaniel84
// Created by iDaniel84
// Created by iDaniel84

import java.util.Arrays;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;

public class PunishCommand
        extends BaseCommand implements Listener {

    static String tn;
    static OfflinePlayer target;
    static String silent;
    Inventory inv = Bukkit.createInventory(null, 54, "Veil Punishment");

    public PunishCommand(BasePlugin plugin) {
        super("punish", "Punish a player.");
        this.setUsage("/(command) <playerName> [-s]");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return false;
        }


        Player player = (Player) sender;
        if (args.length == 0 || args.length > 2) {
            //player.sendMessage(ChatColor.RED + "Try using /punish <player> [-s]");
        } else if (args.length == 2 && args[1].equals("-s")){
            target = Bukkit.getServer().getOfflinePlayer(args[0]);
            tn = target.getName();
            silent = " -s";
            PunishmentGUI(player);
            return true;
        } else if (args.length == 1) {
            target = Bukkit.getServer().getOfflinePlayer(args[0]);
            tn = target.getName();
            silent = "";
            PunishmentGUI(player);
            return true;
        } else {
            //player.sendMessage(ChatColor.RED + "Try using /punish <player> [-s]");
            return true;
        }
        return true;
    }


    public void PunishmentGUI(Player player) {
        if(player == null) {
            return;
        }

        //dyes

        ItemStack globalrules = new ItemStack(Material.INK_SACK, 1, (short) 1);
        ItemMeta globalrulesMeta = globalrules.getItemMeta();
        globalrulesMeta.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "Global Rules");
        globalrules.setItemMeta(globalrulesMeta);
        inv.setItem(0, globalrules);

        ItemStack chatrules = new ItemStack(Material.INK_SACK, 1, (short) 11);
        ItemMeta chatrulesMeta = chatrules.getItemMeta();
        chatrulesMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "Chat Rules");
        chatrules.setItemMeta(chatrulesMeta);
        inv.setItem(8, chatrules);

        ItemStack hcfrules = new ItemStack(Material.INK_SACK, 1, (short) 14);
        ItemMeta hcfrulesMeta = hcfrules.getItemMeta();
        hcfrulesMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "HCF Rules");
        hcfrules.setItemMeta(hcfrulesMeta);
        inv.setItem(22, hcfrules);

        //globalrules

        ItemStack hackedclient = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta hackedclientMeta = hackedclient.getItemMeta();
        hackedclientMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Hacked client");
        hackedclientMeta.setLore(Arrays.asList(ChatColor.GOLD + "Permanent BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Killaura, bhop, phase, etc."));
        hackedclient.setItemMeta(hackedclientMeta);
        inv.setItem(9, hackedclient);

        ItemStack hackedclientadmit = new ItemStack(Material.CARPET, 1, (short) 14);
        ItemMeta hackedclientadmitMeta = hackedclientadmit.getItemMeta();
        hackedclientadmitMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Hacked client Admit.");
        hackedclientadmitMeta.setLore(Arrays.asList(ChatColor.GOLD + "30 days BAN"));
        hackedclientadmit.setItemMeta(hackedclientadmitMeta);
        inv.setItem(10, hackedclientadmit);

        ItemStack xray = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta xrayMeta = xray.getItemMeta();
        xrayMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Xray");
        xrayMeta.setLore(Arrays.asList(ChatColor.GOLD + "15 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Includes X-Ray resourcepacks."));
        xray.setItemMeta(xrayMeta);
        inv.setItem(18, xray);

        ItemStack xrayadmit = new ItemStack(Material.CARPET, 1, (short) 14);
        ItemMeta xrayadmitMeta = xrayadmit.getItemMeta();
        xrayadmitMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Xray Admit");
        xrayadmitMeta.setLore(Arrays.asList(ChatColor.GOLD + "7 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Includes X-Ray resourcepacks."));
        xrayadmit.setItemMeta(xrayadmitMeta);
        inv.setItem(19, xrayadmit);

        ItemStack autoclick = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta autoclickMeta = autoclick.getItemMeta();
        autoclickMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Autoclick");
        autoclickMeta.setLore(Arrays.asList(ChatColor.GOLD + "30 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "More than 21cps constantly."));
        autoclick.setItemMeta(autoclickMeta);
        inv.setItem(27, autoclick);

        ItemStack autoclickadmit = new ItemStack(Material.CARPET, 1, (short) 14);
        ItemMeta autoclickadmitMeta = autoclickadmit.getItemMeta();
        autoclickadmitMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Autoclick Admit");
        autoclickadmitMeta.setLore(Arrays.asList(ChatColor.GOLD + "15 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "More than 21cps constantly."));
        autoclickadmit.setItemMeta(autoclickadmitMeta);
        inv.setItem(28, autoclickadmit);

        ItemStack glitchs = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta glitchsMeta = glitchs.getItemMeta();
        glitchsMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Abusing Glitches");
        glitchsMeta.setLore(Arrays.asList(ChatColor.GOLD + "3 days BAN"));
        glitchs.setItemMeta(glitchsMeta);
        inv.setItem(36, glitchs);

        ItemStack mods = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta modsMeta = mods.getItemMeta();
        modsMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Unapproved Mods");
        modsMeta.setLore(Arrays.asList(ChatColor.GOLD + "15 days BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Click links to see the", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "allowed mods list."));
        mods.setItemMeta(modsMeta);
        inv.setItem(37, mods);

        ItemStack advertising = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta advertisingMeta = advertising.getItemMeta();
        advertisingMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Advertising");
        advertisingMeta.setLore(Arrays.asList(ChatColor.GOLD + "Permanent BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Sending ips of other", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "servers (Not ts3 ips)."));
        advertising.setItemMeta(advertisingMeta);
        inv.setItem(45, advertising);

        ItemStack ddos = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta ddosMeta = ddos.getItemMeta();
        ddosMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Ddos");
        ddosMeta.setLore(Arrays.asList(ChatColor.GOLD + "Blacklist", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Ddosing // Doxing other users."));
        ddos.setItemMeta(ddosMeta);
        inv.setItem(46, ddos);

        //HCF RULES

        ItemStack suffocation = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta suffocationMeta = suffocation.getItemMeta();
        suffocationMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Suffocation Traps");
        suffocationMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Allowed on KITMAP."));
        suffocation.setItemMeta(suffocationMeta);
        inv.setItem(30, suffocation);

        ItemStack pvp = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta pvpMeta = pvp.getItemMeta();
        pvpMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "PvP Protection Abuse");
        pvpMeta.setLore(Arrays.asList(ChatColor.GOLD + "1 day BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Abusing of pvp timer."));
        pvp.setItemMeta(pvpMeta);
        inv.setItem(31, pvp);

        ItemStack kick = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta kickMeta = kick.getItemMeta();
        kickMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Kick & Kill");
        kickMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Allowed on KITMAP."));
        kick.setItemMeta(kickMeta);
        inv.setItem(32, kick);

        ItemStack dtr = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta dtrMeta = dtr.getItemMeta();
        dtrMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "DTR Evading");
        dtrMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Reduce DTR of the faction."));
        dtr.setItemMeta(dtrMeta);
        inv.setItem(39, dtr);

        ItemStack alting = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta altingMeta = alting.getItemMeta();
        altingMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Alting");
        altingMeta.setLore(Arrays.asList(ChatColor.GOLD + "1 day BAN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Alting for DTR or", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "while deathbanned."));
        alting.setItemMeta(altingMeta);
        inv.setItem(40, alting);

        ItemStack griefing = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta griefingMeta = griefing.getItemMeta();
        griefingMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Griefing outside faction claims.");
        griefingMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN"));
        griefing.setItemMeta(griefingMeta);
        inv.setItem(41, griefing);

        ItemStack inside = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta insideMeta = inside.getItemMeta();
        insideMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Inside");
        insideMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Ask a manager for Insider rank."));
        inside.setItemMeta(insideMeta);
        inv.setItem(48, inside);

        ItemStack blockglitch = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta blockglitchMeta = blockglitch.getItemMeta();
        blockglitchMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "BlockGlitch");
        blockglitchMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Includes pearl blockglitch."));
        blockglitch.setItemMeta(blockglitchMeta);
        inv.setItem(49, blockglitch);

        ItemStack allying = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta allyingMeta = allying.getItemMeta();
        allyingMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Allying");
        allyingMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Teaming with non allies."));
        allying.setItemMeta(allyingMeta);
        inv.setItem(50, allying);

        //CHAT RULES

        ItemStack Toxicity = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta ToxicityMeta = Toxicity.getItemMeta();
        ToxicityMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Toxicity");
        ToxicityMeta.setLore(Arrays.asList(ChatColor.GOLD + "1 hour MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Racism, suicidal encouragement", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "or excessive toxicity."));
        Toxicity.setItemMeta(ToxicityMeta);
        inv.setItem(16, Toxicity);

        ItemStack usersindirectly = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta usersindirectlyMeta = usersindirectly.getItemMeta();
        usersindirectlyMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Users disrespect indirectly");
        usersindirectlyMeta.setLore(Arrays.asList(ChatColor.GOLD + "15 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "~You are a ***~"));
        usersindirectly.setItemMeta(usersindirectlyMeta);
        inv.setItem(17, usersindirectly);

        ItemStack usersdirectly = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta usersdirectlyMeta = usersdirectly.getItemMeta();
        usersdirectlyMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Users disrespect directly");
        usersdirectlyMeta.setLore(Arrays.asList(ChatColor.GOLD + "30 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "~Pepito you are a ***~"));
        usersdirectly.setItemMeta(usersdirectlyMeta);
        inv.setItem(25, usersdirectly);

        ItemStack staff = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta staffMeta = staff.getItemMeta();
        staffMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Staff disrespect");
        staffMeta.setLore(Arrays.asList(ChatColor.GOLD + "1 hour MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "~Sh** staff~"));
        staff.setItemMeta(staffMeta);
        inv.setItem(26, staff);

        ItemStack chatspam = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta chatspamMeta = chatspam.getItemMeta();
        chatspamMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Chat spam");
        chatspamMeta.setLore(Arrays.asList(ChatColor.GOLD + "5 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Sending same message", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "a lot of times."));
        chatspam.setItemMeta(chatspamMeta);
        inv.setItem(34, chatspam);

        ItemStack chatflood = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta chatfloodMeta = chatflood.getItemMeta();
        chatfloodMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Chat flood");
        chatfloodMeta.setLore(Arrays.asList(ChatColor.GOLD + "WARN", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Sending 1 word per line."));
        chatflood.setItemMeta(chatfloodMeta);
        inv.setItem(35, chatflood);

        ItemStack links = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta linksMeta = links.getItemMeta();
        linksMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "External links");
        linksMeta.setLore(Arrays.asList(ChatColor.GOLD + "30 min MUTE", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Any link non-related to", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "the Veil Network."));
        links.setItemMeta(linksMeta);
        inv.setItem(43, links);

        ItemStack facspam = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta facspamMeta = facspam.getItemMeta();
        facspamMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Spamming faction commands");
        facspamMeta.setLore(Arrays.asList(ChatColor.GOLD + "KICK", ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Spamming /f create, disband, etc."));
        facspam.setItemMeta(facspamMeta);
        inv.setItem(44, facspam);

        //Miscellaneous

        ItemStack insiderrank = new ItemStack(Material.WOOL, 1, (short) 8);
        ItemMeta insiderrankMeta = insiderrank.getItemMeta();
        insiderrankMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Insider rank");
        insiderrankMeta.setLore(Arrays.asList(ChatColor.GOLD + "Click to set Insider rank", ChatColor.GOLD + "to " + ChatColor.YELLOW.toString() + ChatColor.ITALIC + target.getName()));
        insiderrank.setItemMeta(insiderrankMeta);
        inv.setItem(52, insiderrank);

        ItemStack unban = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta unbanMeta = unban.getItemMeta();
        unbanMeta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Unban");
        unbanMeta.setLore(Arrays.asList(ChatColor.GOLD + "Click to unban " + ChatColor.YELLOW.toString() + ChatColor.ITALIC + target.getName()));
        unban.setItemMeta(unbanMeta);
        inv.setItem(53, unban);

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta skullmeta = (SkullMeta) skull.getItemMeta();
        skullmeta.setOwner(target.getName());
        skullmeta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.BOLD + target.getName());
        skull.setItemMeta(skullmeta);
        inv.setItem(4, skull);

        ItemStack rules = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemMeta rulesMeta = rules.getItemMeta();
        rulesMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Rules document");
        rulesMeta.setLore(Arrays.asList(ChatColor.GOLD + "Click to get the rules", ChatColor.GOLD + "document in a chat message."));
        rules.setItemMeta(rulesMeta);
        inv.setItem(5, rules);

        ItemStack linksdoc = new ItemStack(Material.INK_SACK, 1, (short) 12);
        ItemMeta linksdocMeta = linksdoc.getItemMeta();
        linksdocMeta.setDisplayName(ChatColor.BLUE.toString() + ChatColor.BOLD + "Links document");
        linksdocMeta.setLore(Arrays.asList(ChatColor.GOLD + "Click to get the links", ChatColor.GOLD + "document in a chat message."));
        linksdoc.setItemMeta(linksdocMeta);
        inv.setItem(3, linksdoc);

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(!event.getInventory().getName().equalsIgnoreCase("Veil Punishment")) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == null || !event.getCurrentItem().hasItemMeta()) {
            player.closeInventory();
            return;
        }
        switch (event.getRawSlot()) {

            // RULES

            case 0:
                break;

            case 8:
                break;

            case 22:
                break;

            // GLOBAL RULES

            case 9:
                if (player.hasPermission("base.command.punish.ban")) {
                    Bukkit.dispatchCommand(player, "ipban " + target.getName() + " Hacked client" + silent);
                    player.closeInventory();
                } else {
                    Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 365d" + " Hacked client" + silent);
                    player.closeInventory();
                }
                break;

            case 10:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 30d" + " Hacked client [Admit]" + silent);
                player.closeInventory();
                break;

            case 18:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 15d" + " Xray" + silent);
                player.closeInventory();
                break;

            case 19:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 7d" + " Xray [Admit]" + silent);
                player.closeInventory();
                break;

            case 27:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 30d" + " Autoclick" + silent);
                player.closeInventory();
                break;

            case 28:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 15d" + " Autoclick [Admit]" + silent);
                player.closeInventory();
                break;

            case 36:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 3d" + " Abusing of glitches" + silent);
                player.closeInventory();
                break;

            case 37:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 15d" + " Unapproved Mods" + silent);
                player.closeInventory();
                break;

            case 45:
                if (player.hasPermission("base.command.punish.ban")) {
                    Bukkit.dispatchCommand(player, "ipban " + target.getName() + " Advertising" + silent);
                    player.closeInventory();
                } else {
                    Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 365d" + " Advertising" + silent);
                    player.closeInventory();
                }
                break;

            case 46:
                if (player.hasPermission("base.command.punish.blacklist")) {
                    Bukkit.dispatchCommand(player, "blacklist " + target.getName() + " Ddos");
                    player.closeInventory();
                } else if (player.hasPermission("base.command.punish.ban")){
                    Bukkit.dispatchCommand(player, "ipban " + target.getName() + " DDos" + silent);
                    player.closeInventory();
                    player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.RED + "Ask a Manager - Owner to blacklist this player.");
                } else {
                    Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 365d" + " DDos" + silent);
                    player.closeInventory();
                    player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.RED + "Ask a Manager - Owner to blacklist this player.");
                }
                break;

            // HCF RULES

            case 30:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " Suffocation traps" + silent);
                player.closeInventory();
                break;

            case 31:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 1d" + " Abusing of pvp protection" + silent);
                player.closeInventory();
                break;

            case 32:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " Kick & Kill" + silent);
                player.closeInventory();
                break;

            case 39:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " DTR Evading" + silent);
                player.closeInventory();
                break;

            case 40:
                Bukkit.dispatchCommand(player, "tempban " + target.getName() + " 1d" + " Alting" + silent);
                player.closeInventory();
                break;

            case 41:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " Griefing outside faction claims" + silent);
                player.closeInventory();
                break;

            case 48:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " Inside" + silent);
                player.closeInventory();
                break;

            case 49:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " Block glitching" + silent);
                player.closeInventory();
                break;

            case 50:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " Allying" + silent);
                player.closeInventory();
                break;

            // CHAT RULES

            case 16:
                Bukkit.dispatchCommand(player, "tempmute " + target.getName() + " 1h" + " Toxicity" + silent);
                player.closeInventory();
                break;

            case 17:
                Bukkit.dispatchCommand(player, "tempmute " + target.getName() + " 15m" + " Users disrespect" + silent);
                player.closeInventory();
                break;

            case 25:
                Bukkit.dispatchCommand(player, "tempmute " + target.getName() + " 30m" + " Users disrespect" + silent);
                player.closeInventory();
                break;

            case 26:
                Bukkit.dispatchCommand(player, "tempmute " + target.getName() + " 1h" + " Staff disrespect" + silent);
                player.closeInventory();
                break;

            case 34:
                Bukkit.dispatchCommand(player, "tempmute " + target.getName() + " 5m" + " Chat spam" + silent);
                player.closeInventory();
                break;

            case 35:
                Bukkit.dispatchCommand(player, "warn " + target.getName() + " Chat flood" + silent);
                player.closeInventory();
                break;

            case 43:
                Bukkit.dispatchCommand(player, "tempmute " + target.getName() + " 30m" + " External links" + silent);
                player.closeInventory();
                break;

            case 44:
                Bukkit.dispatchCommand(player, "kick " + target.getName() + " Spamming faction commands" + silent);
                player.closeInventory();
                break;

            // Miscellaneous
            case 52:
                if (player.hasPermission("base.command.punish.insider")) {
                    Bukkit.dispatchCommand(player, "pex user " + target.getName() + " group set Insider");
                    player.closeInventory();
                } else {
                    player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.RED + "You don't have pex access, ask a manager.");
                    player.closeInventory();
                }
                break;

            case 53:
                if (player.hasPermission("base.command.punish.ban")) {
                    Bukkit.dispatchCommand(player, "unban " + target.getName() + silent);
                    player.closeInventory();
                } else {
                    player.closeInventory();
                    player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.RED + "You can't unban players, ask a moderator.");
                }
                break;

            case 5:
                player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.YELLOW + "Rules document " + ChatColor.RED + ChatColor.UNDERLINE + "https://goo.gl/ous7pE");
                player.closeInventory();
                break;

            case 3:
                player.sendMessage(ChatColor.BOLD + " * " + ChatColor.GOLD + ChatColor.BOLD + "ALERT " + ChatColor.YELLOW + "Links document " + ChatColor.RED + ChatColor.UNDERLINE + "https://goo.gl/3xc79b");
                player.closeInventory();
                break;

            default:
                break;
        }
    }

}