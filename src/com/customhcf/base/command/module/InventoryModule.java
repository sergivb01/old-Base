
package com.customhcf.base.command.module;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.command.BaseCommandModule;
import com.customhcf.base.command.module.inventory.*;

import java.util.Set;

public class InventoryModule
extends BaseCommandModule {
    public InventoryModule(BasePlugin plugin) {
        this.commands.add(new ClearInvCommand());
        this.commands.add(new GiveCommand());
        this.commands.add(new IdCommand());
        this.commands.add(new InvSeeCommand(plugin));
        this.commands.add(new ItemCommand());
        this.commands.add(new KitsCommand());
        this.commands.add(new MoreCommand());
        this.commands.add(new SkullCommand());
        this.commands.add(new CopyInvCommand());
    }
}

