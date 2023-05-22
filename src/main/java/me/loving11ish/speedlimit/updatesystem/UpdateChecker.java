package me.loving11ish.speedlimit.updatesystem;

import com.tcoded.folialib.FoliaLib;
import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class UpdateChecker {

    private int resourceId;

    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = ColorUtils.translateColorCodes(messagesFile.getString("plugin-prefix"));
    final String PREFIX_PLACEHOLDER = "%PREFIX%";
    final String ERROR_PLACEHOLDER = "%ERROR%";
    private FoliaLib foliaLib = SpeedLimit.getPlugin().getFoliaLib();

    Logger logger = SpeedLimit.getPlugin().getLogger();

    public UpdateChecker(int resourceId) {
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        foliaLib.getImpl().runAsync(() -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                logger.warning(ColorUtils.translateColorCodes(messagesFile.getString("update-check-failure-1").replace(PREFIX_PLACEHOLDER, PREFIX)
                        .replace(ERROR_PLACEHOLDER, exception.getMessage())));
                logger.warning(ColorUtils.translateColorCodes(messagesFile.getString("update-check-failure-2").replace(PREFIX_PLACEHOLDER, PREFIX)
                        .replace(ERROR_PLACEHOLDER, exception.getMessage())));
                logger.warning(ColorUtils.translateColorCodes(messagesFile.getString("update-check-failure-3").replace(PREFIX_PLACEHOLDER, PREFIX)
                        .replace(ERROR_PLACEHOLDER, exception.getMessage())));
            }
        });
    }
}
