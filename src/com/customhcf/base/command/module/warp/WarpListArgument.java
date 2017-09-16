
package com.customhcf.base.command.module.warp;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.warp.Warp;
import com.customhcf.base.warp.WarpManager;
import com.customhcf.util.command.CommandArgument;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WarpListArgument
extends CommandArgument {
    private final BasePlugin plugin;

    public WarpListArgument(BasePlugin plugin) {
        super("list", "List all server warps");
        this.plugin = plugin;
        this.permission = "command.warp.argument." + this.getName();
    }

    @Override
    public String getUsage(String label) {
        return "" + '/' + label + ' ' + this.getName();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Collection<Warp> warps = this.plugin.getWarpManager().getWarps();
        ArrayList<String> warpNames = new ArrayList<String>(warps.size());
        for (Warp warp : warps) {
            if (!sender.hasPermission(warp.getPermission())) continue;
            warpNames.add(warp.getName());
        }
        sender.sendMessage((Object)ChatColor.DARK_AQUA + "Global Warps (" + warpNames.size() + ")");
        sender.sendMessage((Object)ChatColor.GRAY + "[" + StringUtils.join(warpNames, (String)", ") + ']');
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

