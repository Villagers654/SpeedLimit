package me.loving11ish.speedlimit.updatesystem;

import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = ColorUtils.translateColorCodes(messagesFile.getString("plugin-prefix"));
    final String PREFIX_PLACEHOLDER = "%PREFIX%";

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("SpeedLimit.update")|| player.hasPermission("SpeedLimit.*")||player.isOp()) {
            new UpdateChecker(SpeedLimit.getPlugin(), 75269).getVersion(version -> {
                if (!(SpeedLimit.getPlugin().getDescription().getVersion().equalsIgnoreCase(version))) {
                    player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-update-available-1")
                            .replace(PREFIX_PLACEHOLDER, PREFIX)));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-update-available-2")
                            .replace(PREFIX_PLACEHOLDER, PREFIX)));
                    player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-update-available-3")
                            .replace(PREFIX_PLACEHOLDER, PREFIX)));
                }
            });
        }
    }
}
