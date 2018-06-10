package net.veilmc.base;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.veilmc.base.command.CommandManager;
import net.veilmc.base.command.SimpleCommandManager;
import net.veilmc.base.command.module.ChatModule;
import net.veilmc.base.command.module.EssentialModule;
import net.veilmc.base.command.module.InventoryModule;
import net.veilmc.base.command.module.TeleportModule;
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

		Bukkit.getConsoleSender().sendMessage("");
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[" + getDescription().getName() + "] Plugin loaded!"));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[" + getDescription().getName() + "] &eVersion: " + getDescription().getVersion()));
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[" + getDescription().getName() + "] &eVault: &aHOOKED"));
		Bukkit.getConsoleSender().sendMessage("");

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
		//manager.registerEvents(new ChatCommands(), this);
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

