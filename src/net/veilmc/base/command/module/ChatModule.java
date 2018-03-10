
package net.veilmc.base.command.module;

import net.veilmc.base.BasePlugin;
import net.veilmc.base.command.BaseCommandModule;
import net.veilmc.base.command.module.chat.*;
import net.veilmc.base.command.module.chat.*;

public class ChatModule
extends BaseCommandModule {
    public ChatModule(BasePlugin plugin) {
        this.commands.add(new ToggleStaffChatCommand(plugin));
        this.commands.add(new AnnouncementCommand(plugin));
        this.commands.add(new BroadcastCommand(plugin));
        this.commands.add(new ClearChatCommand());
        this.commands.add(new DisableChatCommand(plugin));
        this.commands.add(new MuteWordCommand(plugin));
        this.commands.add(new SlowChatCommand(plugin));
        this.commands.add(new StaffChatCommand(plugin));
        this.commands.add(new FamousCommand(plugin));
        this.commands.add(new LegalModsCommand(plugin));
        this.commands.add(new YoutubeCommand(plugin));
        this.commands.add(new IgnoreCommand(plugin));
        this.commands.add(new MessageCommand(plugin));
        this.commands.add(new MessageSpyCommand(plugin));
        this.commands.add(new ReplyCommand(plugin));
        this.commands.add(new ToggleMessagesCommand(plugin));
    }
}

