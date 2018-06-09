package net.veilmc.base.command.module;

import net.veilmc.base.command.module.inventory.*;
import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommandModule;
import net.veilmc.base.command.module.inventory.*;

public class InventoryModule
		extends BaseCommandModule{
	public InventoryModule(BasePlugin plugin){
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

