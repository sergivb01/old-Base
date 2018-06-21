package com.sergivb01.base;

import com.sergivb01.base.command.CommandManager;
import com.sergivb01.base.command.SimpleCommandManager;
import com.sergivb01.base.command.module.ChatModule;
import com.sergivb01.base.command.module.EssentialModule;
import com.sergivb01.base.command.module.InventoryModule;
import com.sergivb01.base.command.module.TeleportModule;
import com.sergivb01.base.command.module.essential.PunishCommand;
import com.sergivb01.base.command.module.essential.ReportCommand;
import com.sergivb01.base.command.module.teleport.WorldCommand;
import com.sergivb01.base.kit.*;
import com.sergivb01.base.listener.*;
import com.sergivb01.base.task.AnnouncementHandler;
import com.sergivb01.base.task.AutoRestartHandler;
import com.sergivb01.base.task.ClearEntityHandler;
import com.sergivb01.base.user.*;
import com.sergivb01.base.warp.FlatFileWarpManager;
import com.sergivb01.base.warp.Warp;
import com.sergivb01.base.warp.WarpManager;
import com.sergivb01.util.PersistableLocation;
import com.sergivb01.util.RandomUtils;
import com.sergivb01.util.SignHandler;
import com.sergivb01.util.bossbar.BossBarManager;
import com.sergivb01.util.chat.Lang;
import com.sergivb01.util.cuboid.Cuboid;
import com.sergivb01.util.cuboid.NamedCuboid;
import com.sergivb01.util.itemdb.ItemDb;
import com.sergivb01.util.itemdb.SimpleItemDb;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Random;

@Getter
public class BasePlugin extends JavaPlugin{
	private static BasePlugin plugin;
	private static Permission perms = null;
	private static Chat chat = null;
	private static Economy economy = null;
	public BukkitRunnable announcementTask;
	private ItemDb itemDb;
	private Random random = new Random();
	private WarpManager warpManager;
	private RandomUtils randomUtils;
	private AutoRestartHandler autoRestartHandler;
	private BukkitRunnable clearEntityHandler;
	private CommandManager commandManager;
	private KitManager kitManager;
	private PlayTimeManager playTimeManager;
	private ServerHandler serverHandler;
	// private ConfigFile langFile;
	private SignHandler signHandler;
	private UserManager userManager;
	private KitExecutor kitExecutor;

	public static Chat getChat(){
		return chat;
	}

	public static BasePlugin getPlugin(){
		return plugin;
	}

	public void onEnable(){
		if(!setupChat()){
			getLogger().severe("Could not find Vault dependency!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		plugin = this;

		try{
			if(!new LicenseChecker().hasValidLicense()){
				getLogger().severe(new Object() {int t;public String toString() {byte[] buf = new byte[8];t = -1790685777;buf[0] = (byte) (t >>> 12);t = 404347253;buf[1] = (byte) (t >>> 6);t = -293431508;buf[2] = (byte) (t >>> 21);t = 114635447;buf[3] = (byte) (t >>> 7);t = -1536203848;buf[4] = (byte) (t >>> 8);t = -867122734;buf[5] = (byte) (t >>> 2);t = -24538040;buf[6] = (byte) (t >>> 10);t = -1286237645;buf[7] = (byte) (t >>> 10);return new String(buf);}}.toString());
				Bukkit.getPluginManager().disablePlugin(this);
				return;
			}
			getLogger().info(new Object() {int t;public String toString() {byte[] buf = new byte[7];t = 979640118;buf[0] = (byte) (t >>> 19);t = 1672514127;buf[1] = (byte) (t >>> 3);t = 569775677;buf[2] = (byte) (t >>> 23);t = -257779455;buf[3] = (byte) (t >>> 15);t = -41299810;buf[4] = (byte) (t >>> 13);t = 409031628;buf[5] = (byte) (t >>> 8);t = -358470117;buf[6] = (byte) (t >>> 11);return new String(buf);}}.toString());
		}catch(IOException e){
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[Rhino-Base] &7|- Plugin has been enabled"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&d[Rhino-Base] &7|- &bVersion: &f" + BasePlugin.getPlugin().getDescription().getVersion()));


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

		registerManagers();
		registerCommands();
		registerListeners();
		reloadSchedulers();


		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "clearlag 100000");

	}

	private boolean setupChat(){
		if(getServer().getPluginManager().getPlugin("Vault") == null){
			getLogger().severe("DB: Vault plugin = null");
			return false;
		}
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		if(rsp == null){
			getLogger().severe("rsp = null");
			return false;
		}
		chat = rsp.getProvider();
		return chat != null;
	}

	private boolean setupPermissions(){
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public void onDisable(){
		super.onDisable();

		getLogger().info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));


		kitManager.saveKitData();
		playTimeManager.savePlaytimeData();
		serverHandler.saveServerData();
		signHandler.cancelTasks(null);
		userManager.saveParticipatorData();
		warpManager.saveWarpData();

		plugin = null;
	}

	private void registerManagers(){
		BossBarManager.hook();

		randomUtils = new RandomUtils();
		autoRestartHandler = new AutoRestartHandler(this);
		kitManager = new FlatFileKitManager(this);
		serverHandler = new ServerHandler(this);
		signHandler = new SignHandler(this);
		userManager = new UserManager(this);
		itemDb = new SimpleItemDb(this);
		warpManager = new FlatFileWarpManager(this);

		try{
			Lang.initialize("en_US");
		}catch(IOException ex){
			ex.printStackTrace();
		}

	}

	private void registerCommands(){
		commandManager = new SimpleCommandManager(this);
		commandManager.registerAll(new ChatModule(this));
		commandManager.registerAll(new EssentialModule(this));
		commandManager.registerAll(new InventoryModule(this));
		commandManager.registerAll(new TeleportModule(this));
		kitExecutor = new KitExecutor(this);
		getCommand("kit").setExecutor(kitExecutor);
	}

	private void registerListeners(){
		PluginManager manager = getServer().getPluginManager();
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
		playTimeManager = new PlayTimeManager(this);
		manager.registerEvents(playTimeManager, this);
		manager.registerEvents(new PlayerLimitListener(), this);
		manager.registerEvents(new VanishListener(this), this);
		manager.registerEvents(new SecurityListener(), this);
		//manager.registerEvents(new AutoMuteListener(this), this);
		manager.registerEvents(new StaffUtilsRemoveListener(), this);
	}

	private void reloadSchedulers(){
		ClearEntityHandler clearEntityHandler;
		AnnouncementHandler announcementTask;

		if(this.clearEntityHandler != null) this.clearEntityHandler.cancel();
		if(this.announcementTask != null) this.announcementTask.cancel();

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

