package me.loving11ish.speedlimit.tasks;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.WrappedTask;
import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import me.loving11ish.speedlimit.utils.ServerTPSUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TPSCheckTasks {

    Logger logger = SpeedLimit.getPlugin().getLogger();

    FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();
    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = messagesFile.getString("plugin-prefix");
    final String PREFIX_PLACEHOLDER = "%PREFIX%";
    final String TPS_PLACEHOLDER = "%TPS%";
    private FoliaLib foliaLib = SpeedLimit.getPlugin().getFoliaLib();

    public static WrappedTask wrappedTask2;
    public static WrappedTask wrappedTask3;

    public void checkTPSOne(){
        wrappedTask2 = foliaLib.getImpl().runTimerAsync(new Runnable() {
            Integer time = configFile.getInt("tps.run-interval");
            @Override
            public void run() {
                if (time == 1){
                    updateServerTPS();
                    checkTPSTwo();
                    wrappedTask2.cancel();
                }else {
                    time --;
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }

    public void checkTPSTwo(){
        wrappedTask3 = foliaLib.getImpl().runTimerAsync(new Runnable() {
            Integer time = configFile.getInt("tps.run-interval");
            @Override
            public void run() {
                if (time == 1){
                    updateServerTPS();
                    checkTPSOne();
                    wrappedTask3.cancel();
                }else {
                    time --;
                }
            }
        }, 0L, 1L, TimeUnit.SECONDS);
    }

    public void updateServerTPS() {
        ServerTPSUtils tpsUtils = new ServerTPSUtils();
        String tps = tpsUtils.getTPS(0);
        if (configFile.getBoolean("tps.console.enabled")){
            logger.info(ColorUtils.translateColorCodes(messagesFile.getString("console-TPS").replace(PREFIX_PLACEHOLDER, PREFIX)
                    .replace(TPS_PLACEHOLDER, tps)));
        }
        SpeedLimit.setServerTPS(Double.valueOf(tps));
    }
}
