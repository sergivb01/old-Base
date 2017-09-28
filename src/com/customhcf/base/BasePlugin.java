
package com.customhcf.base;

import com.customhcf.base.command.CommandManager;
import com.customhcf.base.command.SimpleCommandManager;
import com.customhcf.base.command.module.ChatModule;
import com.customhcf.base.command.module.EssentialModule;
import com.customhcf.base.command.module.InventoryModule;
import com.customhcf.base.command.module.TeleportModule;
import com.customhcf.base.command.module.essential.ReportCommand;
import com.customhcf.base.command.module.teleport.WorldCommand;
import com.customhcf.base.kit.*;
import com.customhcf.base.listener.*;
import com.customhcf.base.task.AnnouncementHandler;
import com.customhcf.base.task.AutoRestartHandler;
import com.customhcf.base.task.ClearEntityHandler;
import com.customhcf.base.user.*;
import com.customhcf.base.warp.FlatFileWarpManager;
import com.customhcf.base.warp.Warp;
import com.customhcf.base.warp.WarpManager;
import com.customhcf.util.PersistableLocation;
import com.customhcf.util.RandomUtils;
import com.customhcf.util.SignHandler;
import com.customhcf.util.bossbar.BossBarManager;
import com.customhcf.util.chat.Lang;
import com.customhcf.util.cuboid.Cuboid;
import com.customhcf.util.cuboid.NamedCuboid;
import com.customhcf.util.itemdb.ItemDb;
import com.customhcf.util.itemdb.SimpleItemDb;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Random;

public class BasePlugin
extends JavaPlugin {
    private static BasePlugin instance;
    @Getter private static BasePlugin plugin;
    @Getter private ItemDb itemDb;
    @Getter private Random random = new Random();
    @Getter private WarpManager warpManager;
    @Getter private RandomUtils randomUtils;
    @Getter private AutoRestartHandler autoRestartHandler;
    @Getter private BukkitRunnable clearEntityHandler;
    @Getter public BukkitRunnable announcementTask;
    @Getter private CommandManager commandManager;
    @Getter private KitManager kitManager;
    @Getter private PlayTimeManager playTimeManager;
    @Getter private ServerHandler serverHandler;
    @Getter private SignHandler signHandler;
    @Getter private UserManager userManager;
    @Getter private KitExecutor kitExecutor;


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


        Plugin plugin = this.getServer().getPluginManager().getPlugin("ProtocolLib");
        if (plugin != null && plugin.isEnabled()) {
            try {
                ProtocolHook.hook(this);
            }
            catch (Exception ex) {
                this.getLogger().severe("Error hooking into ProtocolLib from Base.");
                ex.printStackTrace();
            }
        }
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
        }
        catch (IOException ex) {
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
        manager.registerEvents(new MotdListener(this), this);
        manager.registerEvents(new WorldCommand(), this);
        manager.registerEvents(new ChatListener(this), this);
        manager.registerEvents(new ColouredSignListener(), this);
        manager.registerEvents(new DecreasedLagListener(this), this);
        manager.registerEvents(new JoinListener(this), this);
        manager.registerEvents(new ReportCommand(), this);
        manager.registerEvents(new KitListener(this), this);
        manager.registerEvents(new MoveByBlockEvent(), this);
        manager.registerEvents(new MobDamageListener(), this);
        manager.registerEvents(new MobstackListener(this), this);
        manager.registerEvents(new StaffListener(), this);
        manager.registerEvents(new NameVerifyListener(this), this);
        this.playTimeManager = new PlayTimeManager(this);
        manager.registerEvents(this.playTimeManager, this);
        manager.registerEvents(new PlayerLimitListener(), this);
        manager.registerEvents(new VanishListener(this), this);
    }

    public void reloadSchedulers() {
        ClearEntityHandler clearEntityHandler;
        AnnouncementHandler announcementTask;

        if (this.clearEntityHandler != null) this.clearEntityHandler.cancel();
        if (this.announcementTask != null) this.announcementTask.cancel();

        long announcementDelay = (long)this.serverHandler.getAnnouncementDelay() * 20;
        long claggdelay = (long)this.serverHandler.getClearlagdelay() * 20;

        this.announcementTask = announcementTask = new AnnouncementHandler(this);
        MobstackListener mobstackListener = new MobstackListener(this);
        this.clearEntityHandler = clearEntityHandler = new ClearEntityHandler();

        mobstackListener.runTaskTimerAsynchronously(this, 20, 20);
        clearEntityHandler.runTaskTimer(this, claggdelay, claggdelay);
        announcementTask.runTaskTimer(this, announcementDelay, announcementDelay);
    }

    //Code from No3-NYC615-Q616 ~ Nord1615 - 51571 (Credits: @sergivb01)
    private String getPublicAdress() throws IOException{return new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream())).readLine();}

    //Code from No3-NYC615-Q618 ~ Nord1651 - 17914 (Credits: @sergivb01)
    private boolean checkPrivacy(){ //TODO: Check if this works.
        try {
            final URLConnection openConnection = new URL("http://website.com/whitelist.txt").openConnection();
            openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            openConnection.connect();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream(), Charset.forName("UTF-8")));
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                Bukkit.getConsoleSender().sendMessage("Reading IP: " + line);
            }
            if(!sb.toString().contains(getPublicAdress())) {
                return false;
            }
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }


}

