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
import me.loving11ish.speedlimit.filemanager.MessagesDataManager;
import me.loving11ish.speedlimit.updatesystem.JoinEvent;
import me.loving11ish.speedlimit.updatesystem.UpdateChecker;
import me.loving11ish.speedlimit.utils.ColorUtils;
import me.loving11ish.speedlimit.tasks.TPSCheckTasks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SpeedLimit extends JavaPlugin {

    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();
    private static SpeedLimit plugin;
    private FoliaLib foliaLib;
    public MessagesDataManager messagesDataManager;
    private static Double serverTPS = 20.0;
    private final String PREFIX_PLACEHOLDER = "%PREFIX%";
    private String PREFIX;

    Logger logger = this.getLogger();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        foliaLib = new FoliaLib(this);

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13")||Bukkit.getServer().getVersion().contains("1.14")
                ||Bukkit.getServer().getVersion().contains("1.15")||Bukkit.getServer().getVersion().contains("1.16")
                ||Bukkit.getServer().getVersion().contains("1.17")||Bukkit.getServer().getVersion().contains("1.18")
                ||Bukkit.getServer().getVersion().contains("1.19")||Bukkit.getServer().getVersion().contains("1.20"))){
            logger.warning(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Your server version is: " + Bukkit.getServer().getVersion()));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cThis plugin is only supported on the Minecraft versions listed below:"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.13.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.14.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.15.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.16.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]:: &c1.17.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.18.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.19.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &c1.20.x"));
            logger.warning(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cIs now disabling!"));
            logger.warning(ColorUtils.translateColorCodes("-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aA supported Minecraft version has been detected"));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Your server version is: " + Bukkit.getServer().getVersion()));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aContinuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        }

        //Suggest PaperMC if not using
        if (foliaLib.isUnsupported()||foliaLib.isSpigot()){
            PaperLib.suggestPaper(this);
        }

        //Check if PlugManX is enabled
        if (isPlugManXEnabled() || getServer().getPluginManager().isPluginEnabled("PlugManX")){
            if (!PlugManAPI.iDoNotWantToBeUnOrReloaded("SpeedLimit")){
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&4WARNING WARNING WARNING WARNING!"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4You appear to be using an unsupported version of &d&lPlugManX"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Please &4&lDO NOT USE PLUGMANX TO LOAD/UNLOAD/RELOAD THIS PLUGIN!"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Please &4&lFULLY RESTART YOUR SERVER!"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4This plugin &4&lHAS NOT &4been validated to use this version of PlugManX!"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4&lNo official support will be given to you if you use this!"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4&lUnless Loving11ish has explicitly agreed to help!"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Please add SpeedLimit to the ignored-plugins list in PlugManX's config.yml"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &6Continuing plugin startup"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
                logger.severe(ColorUtils.translateColorCodes("&c-------------------------------------------"));
            }else {
                logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
                logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aSuccessfully hooked into PlugManX"));
                logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aSuccessfully added ChatPolice to ignoredPlugins list."));
                logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &6Continuing plugin startup"));
                logger.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            }
        }else {
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cPlugManX not found!"));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &cDisabling PlugManX hook loader"));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &6Continuing plugin startup"));
            logger.info(ColorUtils.translateColorCodes("-------------------------------------------"));
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
                logger.warning(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("console-TPS-task-failed-folia-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.warning(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("console-TPS-task-failed-folia-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        }else {
            if (getConfig().getBoolean("tps.console.task-enabled")){
                TPSCheckTasks tpsCheckTasks = new TPSCheckTasks();
                tpsCheckTasks.checkTPSOne();
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("console-TPS-task-start")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        }

        //Plugin load message
        logger.info("-------------------------------------------");
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin by Loving11ish"));
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin successfully enabled!"));
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin Version &d" + pluginVersion));
        logger.info("-------------------------------------------");

        //Check for available updates
        new UpdateChecker(75269).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-3")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }else {
                logger.warning(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.warning(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.warning(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-3")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        });
    }

    @Override
    public void onDisable() {
        //Shutdown message
        logger.info("-------------------------------------------");
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin by Loving11ish"));

        //Stop background tasks
        try {
            if (!ElytraFlightEvent.wrappedTask1.isCancelled()){
                ElytraFlightEvent.wrappedTask1.cancel();
            }
            if (!TPSCheckTasks.wrappedTask2.isCancelled()){
                TPSCheckTasks.wrappedTask2.cancel();
            }
            if (!TPSCheckTasks.wrappedTask3.isCancelled()){
                TPSCheckTasks.wrappedTask3.cancel();
            }
            if (foliaLib.isUnsupported()){
                Bukkit.getScheduler().cancelTasks(this);
            }
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bAll background tasks disabled successfully!"));
        }catch (Exception e){
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bAll background tasks disabled successfully!"));
        }

        //Final shutdown message
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin Version &d&l" + pluginVersion));
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bPlugin shutdown successfully!"));
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &bGoodbye"));
        logger.info("-------------------------------------------");

        //Cleanup any plugin remains
        messagesDataManager = null;
        foliaLib = null;
        plugin = null;
    }

    public boolean isPlugManXEnabled() {
        try {
            Class.forName("com.rylinaux.plugman.PlugMan");
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aFound PlugManX main class at:"));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &dcom.rylinaux.plugman.PlugMan"));
            return true;
        }catch (ClassNotFoundException e){
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aCould not find PlugManX main class at:"));
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &dcom.rylinaux.plugman.PlugMan"));
            return false;
        }
    }



    public static SpeedLimit getPlugin() {
        return plugin;
    }

    public FoliaLib getFoliaLib() {
        return foliaLib;
    }

    public static Double getServerTPS() {
        return serverTPS;
    }

    public static void setServerTPS(Double serverTPS) {
        SpeedLimit.serverTPS = serverTPS;
    }
}
