
package com.customhcf.base.command;

import com.customhcf.base.command.BaseCommand;
import com.customhcf.base.command.BaseCommandModule;

public interface CommandManager {
    boolean containsCommand(BaseCommand var1);

    void registerAll(BaseCommandModule var1);

    void registerCommand(BaseCommand var1);

    void registerCommands(BaseCommand[] var1);

    void unregisterCommand(BaseCommand var1);

    BaseCommand getCommand(String var1);
}

