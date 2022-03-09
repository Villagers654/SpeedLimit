package me.loving11ish.speedlimit;

import me.loving11ish.speedlimit.commands.SLHelp;
import me.loving11ish.speedlimit.commands.SLReload;
import me.loving11ish.speedlimit.events.ElytraFlightEvent;
import me.loving11ish.speedlimit.events.FlightEvent;
import me.loving11ish.speedlimit.events.PlayerMoveEvent;
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

    private PluginDescriptionFile pluginInfo = getDescription();
    private String pluginVersion = pluginInfo.getVersion();
    private static SpeedLimit plugin;

    public MessagesDataManager messagesDataManager;

    private static Double serverTPS = 20.0;

    private final String PREFIX_PLACEHOLDER = "%PREFIX%";

    Logger logger = this.getLogger();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.13")||Bukkit.getServer().getVersion().contains("1.14")
                ||Bukkit.getServer().getVersion().contains("1.15")||Bukkit.getServer().getVersion().contains("1.16")
                ||Bukkit.getServer().getVersion().contains("1.17")||Bukkit.getServer().getVersion().contains("1.18"))){
            logger.warning(ChatColor.RED + "-------------------------------------------");
            logger.warning(ChatColor.RED + "SpeedLimit - This plugin is only supported on the Minecraft versions listed below:");
            logger.warning(ChatColor.RED + "SpeedLimit - 1.13.x");
            logger.warning(ChatColor.RED + "SpeedLimit - 1.14.x");
            logger.warning(ChatColor.RED + "SpeedLimit - 1.15.x");
            logger.warning(ChatColor.RED + "SpeedLimit - 1.16.x");
            logger.warning(ChatColor.RED + "SpeedLimit - 1.17.x");
            logger.warning(ChatColor.RED + "SpeedLimit - 1.18.x");
            logger.warning(ChatColor.RED + "SpeedLimit - Is now disabling!");
            logger.warning(ChatColor.RED + "-------------------------------------------");
            Bukkit.getPluginManager().disablePlugin(this);
        }else {
            logger.info(ChatColor.GREEN + "-------------------------------------------");
            logger.info(ChatColor.GREEN + "SpeedLimit - A supported Minecraft version has been detected");
            logger.info(ChatColor.GREEN + "SpeedLimit - Continuing plugin startup");
            logger.info(ChatColor.GREEN + "-------------------------------------------");
        }

        //Register the config file
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Register & save the messages config
        this.messagesDataManager = new MessagesDataManager();
        this.messagesDataManager.MessagesDataManager(this);


        //Register commands
        getCommand("slhelp").setExecutor(new SLHelp());
        getCommand("slreload").setExecutor(new SLReload());

        //Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerMoveEvent(), this);
        getServer().getPluginManager().registerEvents(new FlightEvent(), this);
        getServer().getPluginManager().registerEvents(new ElytraFlightEvent(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);

        //Run elytra velocity update task
        ElytraFlightEvent.updateElytraTriggerValue();

        //Start TPS checking tasks
        TPSCheckTasks.checkTPSOne();

        //Plugin load message
        logger.info("-------------------------------------------");
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bPlugin by Loving11ish"));
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bPlugin successfully enabled!"));
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bPlugin Version &d" + pluginVersion));
        logger.info("-------------------------------------------");

        //Check for available updates
        String PREFIX = this.messagesDataManager.getMessagesConfig().getString("plugin-prefix");
        new UpdateChecker(this, 75269).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-no-update-3")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }else {
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-1")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-2")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
                logger.info(ColorUtils.translateColorCodes(messagesDataManager.getMessagesConfig().getString("plugin-update-available-3")
                        .replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        });
    }

    @Override
    public void onDisable() {
        //Shutdown message
        logger.info("-------------------------------------------");
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bPlugin by Loving11ish"));
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bPlugin Version &d" + pluginVersion));

        //Stop TPS tasks
        try {
            if (Bukkit.getScheduler().isCurrentlyRunning(TPSCheckTasks.taskID1)||Bukkit.getScheduler().isQueued(TPSCheckTasks.taskID1)){
                Bukkit.getScheduler().cancelTask(TPSCheckTasks.taskID1);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(TPSCheckTasks.taskID2)||Bukkit.getScheduler().isQueued(TPSCheckTasks.taskID2)){
                Bukkit.getScheduler().cancelTask(TPSCheckTasks.taskID2);
            }
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bAll background tasks disabled successfully!"));
        }catch (Exception e){
            logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bAll background tasks disabled successfully!"));
        }

        //Final shutdown message
        logger.info(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7] &bPlugin shutdown successfully!"));
        logger.info("-------------------------------------------");

    }
    public static SpeedLimit getPlugin() {
        return plugin;
    }

    public static Double getServerTPS() {
        return serverTPS;
    }

    public static void setServerTPS(Double serverTPS) {
        SpeedLimit.serverTPS = serverTPS;
    }
}
