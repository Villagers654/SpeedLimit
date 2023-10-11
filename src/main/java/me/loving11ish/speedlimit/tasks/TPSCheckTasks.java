package me.loving11ish.speedlimit.tasks;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import me.loving11ish.speedlimit.utils.ServerTPSUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.TimeUnit;

public class TPSCheckTasks {

    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    private static WrappedTask TPSTask;

    private static FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();
    private static FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    private static String PREFIX = messagesFile.getString("plugin-prefix");
    private static final String PREFIX_PLACEHOLDER = "%PREFIX%";
    private static final String TPS_PLACEHOLDER = "%TPS%";
    private static FoliaLib foliaLib = SpeedLimit.getPlugin().getFoliaLib();
    private static ServerTPSUtils tpsUtils = new ServerTPSUtils();

    public static void runTPSCheckTask() {
        TPSTask = foliaLib.getImpl().runTimerAsync(() ->
                updateServerTPS(), 1L, configFile.getInt("tps.run-interval"), TimeUnit.SECONDS);
    }

    private static void updateServerTPS() {
        String tps = tpsUtils.getTPS(0);
        if (configFile.getBoolean("tps.console.enabled")){
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("console-TPS").replace(PREFIX_PLACEHOLDER, PREFIX)
                    .replace(TPS_PLACEHOLDER, tps)));
        }
        SpeedLimit.setServerTPS(Double.valueOf(tps));
    }

    public static WrappedTask getTPSTask() {
        return TPSTask;
    }
}
