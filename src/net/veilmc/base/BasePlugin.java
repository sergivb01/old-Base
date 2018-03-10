
package net.veilmc.base;

import lombok.Getter;
import net.veilmc.base.command.CommandManager;
import net.veilmc.base.command.SimpleCommandManager;
import net.veilmc.base.command.module.ChatModule;
import net.veilmc.base.command.module.EssentialModule;
import net.veilmc.base.command.module.InventoryModule;
import net.veilmc.base.command.module.TeleportModule;
import net.veilmc.base.command.module.chat.ChatCommands;
import net.veilmc.base.command.module.essential.PunishCommand;
import net.veilmc.base.command.module.essential.ReportCommand;
import net.veilmc.base.command.module.teleport.WorldCommand;
import net.veilmc.base.kit.*;
import net.veilmc.base.listener.*;
import net.veilmc.base.task.AnnouncementHandler;
import net.veilmc.base.task.AutoRestartHandler;
import net.veilmc.base.task.ClearEntityHandler;
import net.veilmc.base.user.*;
import net.veilmc.base.warp.FlatFileWarpManager;
import net.veilmc.base.warp.Warp;
import net.veilmc.base.warp.WarpManager;
import net.veilmc.util.PersistableLocation;
import net.veilmc.util.RandomUtils;
import net.veilmc.util.SignHandler;
import net.veilmc.util.bossbar.BossBarManager;
import net.veilmc.util.chat.Lang;
import net.veilmc.util.cuboid.Cuboid;
import net.veilmc.util.cuboid.NamedCuboid;
import net.veilmc.util.itemdb.ItemDb;
import net.veilmc.util.itemdb.SimpleItemDb;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Random;

public class BasePlugin extends JavaPlugin {
    @Getter
    private static BasePlugin plugin;
    @Getter
    private ItemDb itemDb;
    @Getter
    private Random random = new Random();
    @Getter
    private WarpManager warpManager;
    @Getter
    private RandomUtils randomUtils;
    @Getter
    private AutoRestartHandler autoRestartHandler;
    @Getter
    private BukkitRunnable clearEntityHandler;
    @Getter
    public BukkitRunnable announcementTask;
    @Getter
    private CommandManager commandManager;
    @Getter
    private KitManager kitManager;
    @Getter
    private PlayTimeManager playTimeManager;
    @Getter
    private ServerHandler serverHandler;
    @Getter
    private SignHandler signHandler;
    @Getter
    private UserManager userManager;
    @Getter
    private KitExecutor kitExecutor;
    //@Getter private ConfigFile langFile;

    public void onEnable() {
        plugin = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        ConfigurationSerialization.registerClass(Warp.class);
        ConfigurationSerialization.registerClass(ServerParticipator.class);
        ConfigurationSerialization.registerClass(BaseUser.class);
        ConfigurationSerialization.registerClass(ConsoleUser.class);
        ConfigurationSerialization.registerClass(NameHistory.class);
        ConfigurationSerialization.registerClass(PersistableLocation.class);
        ConfigurationSerialization.registerClass(Cuboid.class);
        ConfigurationSerialization.registerClass(NamedCuboid.class);
        ConfigurationSerialization.registerClass(Kit.class);

        this.registerManagers();
        this.registerCommands();
        this.registerListeners();
        this.reloadSchedulers();

        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "clearlag 100000");

    }


    public void onDisable() {
        super.onDisable();

        BossBarManager.unhook();

        this.kitManager.saveKitData();
        this.playTimeManager.savePlaytimeData();
        this.serverHandler.saveServerData();
        this.signHandler.cancelTasks(null);
        this.userManager.saveParticipatorData();
        this.warpManager.saveWarpData();

        plugin = null;
    }


    private void registerManagers() {
        BossBarManager.hook();

        this.randomUtils = new RandomUtils();
        this.autoRestartHandler = new AutoRestartHandler(this);
        this.kitManager = new FlatFileKitManager(this);
        this.serverHandler = new ServerHandler(this);
        this.signHandler = new SignHandler(this);
        this.userManager = new UserManager(this);
        this.itemDb = new SimpleItemDb(this);
        this.warpManager = new FlatFileWarpManager(this);

        try {
            Lang.initialize("en_US");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void registerCommands() {
        this.commandManager = new SimpleCommandManager(this);
        this.commandManager.registerAll(new ChatModule(this));
        this.commandManager.registerAll(new EssentialModule(this));
        this.commandManager.registerAll(new InventoryModule(this));
        this.commandManager.registerAll(new TeleportModule(this));
        this.kitExecutor = new KitExecutor(this);
        this.getCommand("kit").setExecutor(this.kitExecutor);
    }

    public KitExecutor getKitExecutor() {
        return this.kitExecutor;
    }

    private void registerListeners() {
        PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new WorldCommand(), this);
        manager.registerEvents(new ChatListener(this), this);
        manager.registerEvents(new PunishCommand(), this);
        manager.registerEvents(new ColouredSignListener(), this);
        manager.registerEvents(new DecreasedLagListener(this), this);
        manager.registerEvents(new JoinListener(this), this);
        manager.registerEvents(new ReportCommand(), this);
        manager.registerEvents(new KitListener(this), this);
        manager.registerEvents(new MoveByBlockEvent(), this);
        manager.registerEvents(new MobstackListener(), this);
        manager.registerEvents(new StaffListener(), this);
        manager.registerEvents(new NameVerifyListener(this), this);
        this.playTimeManager = new PlayTimeManager(this);
        manager.registerEvents(this.playTimeManager, this);
        manager.registerEvents(new PlayerLimitListener(), this);
        manager.registerEvents(new VanishListener(this), this);
        manager.registerEvents(new ChatCommands(), this);
        //manager.registerEvents(new AutoMuteListener(this), this);
        manager.registerEvents(new StaffUtilsRemoveListener(), this);
    }


    private void reloadSchedulers() {
        ClearEntityHandler clearEntityHandler;
        AnnouncementHandler announcementTask;

        if (this.clearEntityHandler != null) this.clearEntityHandler.cancel();
        if (this.announcementTask != null) this.announcementTask.cancel();

        long announcementDelay = (long) this.serverHandler.getAnnouncementDelay() * 20;
        long claggdelay = (long) this.serverHandler.getClearlagdelay() * 20;

        this.announcementTask = announcementTask = new AnnouncementHandler(this);
        MobstackListener mobstackListener = new MobstackListener();
        this.clearEntityHandler = clearEntityHandler = new ClearEntityHandler();

        mobstackListener.runTaskTimerAsynchronously(this, 20, 20);
        clearEntityHandler.runTaskTimer(this, claggdelay, claggdelay);
        announcementTask.runTaskTimer(this, announcementDelay, announcementDelay);
    }


}

