
package net.veilmc.base.command.module;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommandModule;
import net.veilmc.base.command.module.teleport.*;
import net.veilmc.base.command.module.warp.WarpExecutor;
import net.veilmc.base.command.module.teleport.*;

public class TeleportModule
extends BaseCommandModule {
    public TeleportModule(BasePlugin plugin) {
        this.commands.add(new LobbyCommand(plugin));
        this.commands.add(new BackCommand(plugin));
        this.commands.add(new TeleportCommand());
        this.commands.add(new TeleportAllCommand());
        this.commands.add(new TeleportHereCommand());
        this.commands.add(new TopCommand());
        this.commands.add(new WorldCommand());
        this.commands.add(new WarpExecutor(plugin));
    }
}

