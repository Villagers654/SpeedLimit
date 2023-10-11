package me.loving11ish.speedlimit;

import com.rylinaux.plugman.api.PlugManAPI;
import com.tcoded.folialib.FoliaLib;
import io.papermc.lib.PaperLib;
import me.loving11ish.speedlimit.commands.SLHelp;
import me.loving11ish.speedlimit.commands.SLReload;
import me.loving11ish.speedlimit.events.ElytraFlightEvent;
import me.loving11ish.speedlimit.events.ElytraTPSFlightEvent;
import me.loving11ish.speedlimit.events.FlightEvent;
import me.loving11ish.speedlimit.events.PlayerWalkEvent;
import me.loving11ish.speedlimit.externalhooks.PlugManXAPI;
import me.loving11ish.speedlimit.filemanager.MessagesDataManager;
import me.loving11ish.speedlimit.updatesystem.JoinEvent;
import me.loving11ish.speedlimit.updatesystem.UpdateChecker;
import me.loving11ish.speedlimit.utils.ColorUtils;
import me.loving11ish.speedlimit.tasks.TPSCheckTasks;
import me.loving11ish.speedlimit.utils.VersionCheckerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpeedLimit extends JavaPlugin {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();

    private static SpeedLimit plugin;
    private static FoliaLib foliaLib;
    private static VersionCheckerUtils versionCheckerUtils;
    public MessagesDataManager messagesDataManager;

    private static Double serverTPS = 20.0;
    private final String PREFIX_PLACEHOLDER = "%PREFIX%";
    private String PREFIX;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        foliaLib = new FoliaLib(this);
        versionCheckerUtils = new VersionCheckerUtils();
        versionCheckerUtils.setVersion();

        //Server version compatibility check
        if (getVersionCheckerUtils().getVersion() < 13||getVersionCheckerUtils().getVersion() > 20){
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Your server version is: &d" + Bukkit.getServer().getVersion()));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cThis plugin is only supported on the Minecraft versions listed below:"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.13.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.14.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.15.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.16.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.17.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.18.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.19.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.20.x"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cIs now disabling!"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aA supported Minecraft version has been detected"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aYour server version is: &d" + Bukkit.getServer().getVersion()));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aContinuing plugin startup"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Suggest PaperMC if not using
        if (foliaLib.isUnsupported()||foliaLib.isSpigot()){
            PaperLib.suggestPaper(this);
        }

        //Check if PlugManX is enabled
        if (getServer().getPluginManager().isPluginEnabled("PlugManX")||PlugManXAPI.isPlugManXEnabled()){
            if (!PlugManAPI.iDoNotWantToBeUnOrReloaded("SpeedLimit")){
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&4WARNING WARNING WARNING WARNING!"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4You appear to be using an unsupported version of &d&lPlugManX"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Please &4&lDO NOT USE PLUGMANX TO LOAD/UNLOAD/RELOAD THIS PLUGIN!"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Please &4&lFULLY RESTART YOUR SERVER!"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4This plugin &4&lHAS NOT &4been validated to use this version of PlugManX!"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4&lNo official support will be given to you if you use this!"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4&lUnless Loving11ish has explicitly agreed to help!"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Please add SpeedLimit to the ignored-plugins list in PlugManX's config.yml"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &6Continuing plugin startup"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
            }else {
                console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aSuccessfully hooked into PlugManX"));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aSuccessfully added SpeedLimit to ignoredPlugins list."));
                console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &6Continuing plugin startup"));
                console.sendMessage(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cPlugManX not found!"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cDisabling PlugManX hook loader"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &6Continuing plugin startup"));
            console.sendMessage(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Register the config file
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Register & save the messages config
        this.messagesDataManager = new MessagesDataManager();
        this.messagesDataManager.MessagesDataManager(this);

        //Update plugin prefix
        PREFIX = messagesDataManager.getMessagesConfig().getString("plugin-prefix");

        //Register commands
        getCommand("slhelp").setExecutor(new SLHelp());
        getCommand("slreload").setExecutor(new SLReload());

        //Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerWalkEvent(), this);
        getServer().getPluginManager().registerEvents(new FlightEvent(), this);
        getServer().getPluginManager().registerEvents(new ElytraFlightEvent(), this);
        getServer().getPluginManager().registerEvents(new ElytraTPSFlightEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        //Run elytra velocity update task
        ElytraFlightEvent.updateElytraTriggerValue();

        //Start TPS checking tasks
        if (foliaLib.isFolia()){
            if (getConfig().getBoolean("tps.console.task-enabled")){
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("console-TPS-task-failed-folia-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("console-TPS-task-failed-folia-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        }else {
            if (getConfig().getBoolean("tps.console.task-enabled")){
                TPSCheckTasks.runTPSCheckTask();
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("console-TPS-task-start")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        }

        //Plugin load message
        console.sendMessage("-------------------------------------------");
        console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin by Loving11ish"));
        console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin successfully enabled!"));
        console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin Version &d" + pluginVersion));
        console.sendMessage("-------------------------------------------");

        //Check for available updates
        new UpdateChecker(75269).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-3")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }else {
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                console.sendMessage(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-3")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        });
    }

    @Override
    public void onDisable() {
        //Plugin shutdown logic

        //Unregister plugin listeners
        HandlerList.unregisterAll(this);

        //Shutdown message
        console.sendMessage("-------------------------------------------");
        console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin by Loving11ish"));

        //Stop background tasks
        try {
            if (!ElytraFlightEvent.getElytraTrigerUpdateTask().isCancelled()){
                ElytraFlightEvent.getElytraTrigerUpdateTask().cancel();
            }
            if (!TPSCheckTasks.getTPSTask().isCancelled()){
                TPSCheckTasks.getTPSTask().cancel();
            }
            if (foliaLib.isUnsupported()){
                Bukkit.getScheduler().cancelTasks(this);
            }
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bAll background tasks disabled successfully!"));
        }catch (Exception e){
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bAll background tasks disabled successfully!"));
        }

        //Final shutdown message
        console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin Version &d&l" + pluginVersion));
        console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin shutdown successfully!"));
        console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bGoodbye"));
        console.sendMessage("-------------------------------------------");

        //Cleanup any plugin remains
        messagesDataManager = null;
        foliaLib = null;
        plugin = null;
    }

    public static SpeedLimit getPlugin() {
        return plugin;
    }

    public FoliaLib getFoliaLib() {
        return foliaLib;
    }

    public static VersionCheckerUtils getVersionCheckerUtils() {
        return versionCheckerUtils;
    }

    public static Double getServerTPS() {
        return serverTPS;
    }

    public static void setServerTPS(Double serverTPS) {
        SpeedLimit.serverTPS = serverTPS;
    }
}
