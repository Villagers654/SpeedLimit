package me.loving11ish.speedlimit.tasks;

import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import me.loving11ish.speedlimit.utils.ServerTPSUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class TPSCheckTasks {

    Logger logger = SpeedLimit.getPlugin().getLogger();

    FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();
    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = messagesFile.getString("plugin-prefix");
    final String PREFIX_PLACEHOLDER = "%PREFIX%";
    final String TPS_PLACEHOLDER = "%TPS%";

    public static Integer taskID2;
    public static Integer taskID3;

    public void checkTPSOne(){
        taskID2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(SpeedLimit.getPlugin(), new Runnable() {
            Integer time = configFile.getInt("tps.run-interval");
            @Override
            public void run() {
                if (time == 1){
                    ServerTPSUtils tpsUtils = new ServerTPSUtils();
                    String tps = tpsUtils.getTPS(0);
                    if (configFile.getBoolean("tps.console.enabled")){
                        logger.info(ColorUtils.translateColorCodes(messagesFile.getString("console-TPS").replace(PREFIX_PLACEHOLDER, PREFIX)
                                .replace(TPS_PLACEHOLDER, tps)));
                    }
                    SpeedLimit.setServerTPS(Double.valueOf(tps));
                    checkTPSTwo();
                    Bukkit.getScheduler().cancelTask(taskID2);
                }else {
                    time --;
                }
            }
        },0, 20);
    }

    public void checkTPSTwo(){
        taskID3 = Bukkit.getScheduler().scheduleSyncRepeatingTask(SpeedLimit.getPlugin(), new Runnable() {
            Integer time = configFile.getInt("tps.run-interval");
            @Override
            public void run() {
                if (time == 1){
                    ServerTPSUtils tpsUtils = new ServerTPSUtils();
                    String tps = tpsUtils.getTPS(0);
                    if (configFile.getBoolean("tps.console.enabled")){
                        logger.info(ColorUtils.translateColorCodes(messagesFile.getString("console-TPS").replace(PREFIX_PLACEHOLDER, PREFIX)
                                .replace(TPS_PLACEHOLDER, tps)));
                    }
                    SpeedLimit.setServerTPS(Double.valueOf(tps));
                    checkTPSOne();
                    Bukkit.getScheduler().cancelTask(taskID3);
                }else {
                    time --;
                }
            }
        },0, 20);
    }
}
