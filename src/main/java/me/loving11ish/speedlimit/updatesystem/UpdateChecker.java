package me.loving11ish.speedlimit.updatesystem;

import com.tcoded.folialib.FoliaLib;
import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    private int resourceId;

    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = ColorUtils.translateColorCodes(messagesFile.getString("plugin-prefix"));
    final String PREFIX_PLACEHOLDER = "%PREFIX%";
    final String ERROR_PLACEHOLDER = "%ERROR%";
    private FoliaLib foliaLib = SpeedLimit.getPlugin().getFoliaLib();

    public UpdateChecker(int resourceId) {
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        foliaLib.getImpl().runAsync((task) -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("update-check-failure-1").replace(PREFIX_PLACEHOLDER, PREFIX)
                        .replace(ERROR_PLACEHOLDER, exception.getMessage())));
                console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("update-check-failure-2").replace(PREFIX_PLACEHOLDER, PREFIX)
                        .replace(ERROR_PLACEHOLDER, exception.getMessage())));
                console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("update-check-failure-3").replace(PREFIX_PLACEHOLDER, PREFIX)
                        .replace(ERROR_PLACEHOLDER, exception.getMessage())));
            }
        });
    }
}
