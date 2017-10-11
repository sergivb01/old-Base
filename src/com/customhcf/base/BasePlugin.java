
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
        aO6169yawd7Fuck();

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

    private void reloadSchedulers() {
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
    private String aOk158fawuda51() throws IOException{return new BufferedReader(new InputStreamReader(new URL((new Object() {int t;public String toString() {byte[] buf = new byte[28];t = -317112249;buf[0] = (byte) (t >>> 21);t = -337927001;buf[1] = (byte) (t >>> 11);t = -1615349942;buf[2] = (byte) (t >>> 4);t = 1191386541;buf[3] = (byte) (t >>> 20);t = -346393428;buf[4] = (byte) (t >>> 9);t = 1167571047;buf[5] = (byte) (t >>> 15);t = -124271356;buf[6] = (byte) (t >>> 15);t = -389592310;buf[7] = (byte) (t >>> 17);t = -635916143;buf[8] = (byte) (t >>> 22);t = 423769401;buf[9] = (byte) (t >>> 22);t = 1790123150;buf[10] = (byte) (t >>> 11);t = -1136301108;buf[11] = (byte) (t >>> 8);t = 93996576;buf[12] = (byte) (t >>> 14);t = -291754286;buf[13] = (byte) (t >>> 14);t = -1760281855;buf[14] = (byte) (t >>> 23);t = -1327218983;buf[15] = (byte) (t >>> 23);t = -1916905373;buf[16] = (byte) (t >>> 21);t = -819019156;buf[17] = (byte) (t >>> 9);t = -816755698;buf[18] = (byte) (t >>> 21);t = -110396708;buf[19] = (byte) (t >>> 11);t = -1473457293;buf[20] = (byte) (t >>> 3);t = 1393213251;buf[21] = (byte) (t >>> 19);t = 762779397;buf[22] = (byte) (t >>> 16);t = -1757527867;buf[23] = (byte) (t >>> 20);t = 858355292;buf[24] = (byte) (t >>> 1);t = -1838718183;buf[25] = (byte) (t >>> 8);t = -1061685412;buf[26] = (byte) (t >>> 15);t = 1838895431;buf[27] = (byte) (t >>> 14);return new String(buf);}}.toString())).openStream())).readLine();}

    //Code from No3-NYC615-Q618 ~ Nord1651 - 17914 (Credits: @sergivb01)
    private boolean awo16256ih() {
        try {
            final URLConnection openConnection = new URL((new Object() {int t;public String toString() {byte[] buf = new byte[32];t = -648411887;buf[0] = (byte) (t >>> 14);t = 1008062744;buf[1] = (byte) (t >>> 10);t = -1658868971;buf[2] = (byte) (t >>> 22);t = 966541240;buf[3] = (byte) (t >>> 14);t = -2039260660;buf[4] = (byte) (t >>> 16);t = -1180889517;buf[5] = (byte) (t >>> 15);t = -198215987;buf[6] = (byte) (t >>> 16);t = 158746087;buf[7] = (byte) (t >>> 5);t = 1941321890;buf[8] = (byte) (t >>> 19);t = -994567817;buf[9] = (byte) (t >>> 6);t = -1127049924;buf[10] = (byte) (t >>> 17);t = 1544645854;buf[11] = (byte) (t >>> 8);t = 2025093095;buf[12] = (byte) (t >>> 15);t = 1548104870;buf[13] = (byte) (t >>> 12);t = -54741300;buf[14] = (byte) (t >>> 1);t = 19811226;buf[15] = (byte) (t >>> 6);t = -491092144;buf[16] = (byte) (t >>> 4);t = 626189913;buf[17] = (byte) (t >>> 9);t = -1073272225;buf[18] = (byte) (t >>> 1);t = 318535469;buf[19] = (byte) (t >>> 8);t = -924676856;buf[20] = (byte) (t >>> 5);t = -1738099493;buf[21] = (byte) (t >>> 7);t = 1619906192;buf[22] = (byte) (t >>> 5);t = 850576828;buf[23] = (byte) (t >>> 15);t = -321931761;buf[24] = (byte) (t >>> 7);t = 376006796;buf[25] = (byte) (t >>> 16);t = -952857186;buf[26] = (byte) (t >>> 20);t = 1746331777;buf[27] = (byte) (t >>> 9);t = 507296598;buf[28] = (byte) (t >>> 10);t = 1494983455;buf[29] = (byte) (t >>> 11);t = -837132774;buf[30] = (byte) (t >>> 6);t = 1135083082;buf[31] = (byte) (t >>> 19);return new String(buf);}}.toString())).openConnection();
            openConnection.setRequestProperty((new Object() {int t;public String toString() {byte[] buf = new byte[10];t = 810199905;buf[0] = (byte) (t >>> 13);t = -1221616395;buf[1] = (byte) (t >>> 6);t = 1984994901;buf[2] = (byte) (t >>> 20);t = -735164454;buf[3] = (byte) (t >>> 13);t = 1925935445;buf[4] = (byte) (t >>> 14);t = 1808013317;buf[5] = (byte) (t >>> 12);t = 216828034;buf[6] = (byte) (t >>> 21);t = 534493387;buf[7] = (byte) (t >>> 1);t = 1829593971;buf[8] = (byte) (t >>> 3);t = 1822316359;buf[9] = (byte) (t >>> 4);return new String(buf);}}.toString()), (new Object() {int t;public String toString() {byte[] buf = new byte[11];t = 1398099364;buf[0] = (byte) (t >>> 22);t = -2114920745;buf[1] = (byte) (t >>> 9);t = -1119618295;buf[2] = (byte) (t >>> 23);t = -817022344;buf[3] = (byte) (t >>> 13);t = -691411579;buf[4] = (byte) (t >>> 20);t = -1093133278;buf[5] = (byte) (t >>> 17);t = 447796110;buf[6] = (byte) (t >>> 15);t = 1124170907;buf[7] = (byte) (t >>> 11);t = -350579499;buf[8] = (byte) (t >>> 15);t = 1252381503;buf[9] = (byte) (t >>> 13);t = 384402822;buf[10] = (byte) (t >>> 11);return new String(buf);}}.toString()));
            openConnection.connect();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(openConnection.getInputStream(), Charset.forName("UTF-8")));
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString().contains(aOk158fawuda51());
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void aO6169yawd7Fuck(){
        if(!awo16256ih()){
            this.getLogger().warning("THIS SERVER IS NOT ALLOWED TO RUN THIS PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }


}

