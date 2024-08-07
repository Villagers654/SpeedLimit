package me.loving11ish.speedlimit.events;

import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import io.papermc.lib.PaperLib;
import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.TimeUnit;

public class ElytraFlightEvent implements Listener {

    private static WrappedTask elytraTrigerUpdateTask;
    private static double velocityTriggerMultiplier;
    private static FoliaLib foliaLib = SpeedLimit.getPlugin().getFoliaLib();

    FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();
    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = messagesFile.getString("plugin-prefix");
    final String PREFIX_PLACEHOLDER = "%PREFIX%";

    public static void updateElytraTriggerValue(){
        elytraTrigerUpdateTask = foliaLib.getScheduler().runLaterAsync(() ->
                velocityTriggerMultiplier = SpeedLimit.getPlugin().getConfig().getDouble("elytra-flight-event.speed-limit.trigger-speed")
                , 500L, TimeUnit.MILLISECONDS);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onElytraFlight(PlayerMoveEvent event){
        Player player = event.getPlayer();
        double x = event.getFrom().getX();
        double y = event.getFrom().getY();
        double z = event.getFrom().getZ();
        float yaw = event.getFrom().getYaw();
        float pitch = event.getFrom().getPitch();
        if (configFile.getBoolean("elytra-flight-event.disable-all-elytra-flight")){
            if (player.isGliding()){
                Location location = new Location(player.getWorld(), x, y, z, yaw, pitch);
                y = location.getWorld().getHighestBlockYAt(location);
                location.setY(y + 1);
                player.setGliding(false);
                PaperLib.teleportAsync(player, location);
                player.sendMessage(ColorUtils.translateColorCodes(
                        messagesFile.getString("elytras-disabled-warning").replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
            return;
        }
        if (!(configFile.getBoolean("elytra-flight-event.speed-limit.enabled"))){
            return;
        }
        if (configFile.getList("disabled-Worlds").contains(player.getWorld().getName())){
            return;
        }
        if (player.hasPermission("speedlimit.bypass.elytra")||player.hasPermission("speedlimit.bypass.*")
                ||player.hasPermission("speedlimit.*")||player.isOp()){
            return;
        }
        if (player.isGliding()){
            if (Math.abs(event.getFrom().getX() - event.getTo().getX()) > velocityTriggerMultiplier
                    ||Math.abs(event.getFrom().getY() - event.getTo().getY()) > velocityTriggerMultiplier
                    ||Math.abs(event.getFrom().getZ() - event.getTo().getZ()) > velocityTriggerMultiplier){
                Location oldLocation = new Location(player.getWorld(), x, y + 1, z, yaw, pitch);
                if (configFile.getBoolean("elytra-flight-event.speed-limit.cancel-event")){
                    event.setCancelled(true);
                }else {
                    PaperLib.teleportAsync(player, oldLocation);
                }
                if (configFile.getBoolean("elytra-flight-event.speed-limit.send-message")){
                    player.sendMessage(ColorUtils.translateColorCodes(
                            messagesFile.getString("elytra-triggered-warning").replace(PREFIX_PLACEHOLDER, PREFIX)));
                }
            }
        }
    }

    public static WrappedTask getElytraTrigerUpdateTask() {
        return elytraTrigerUpdateTask;
    }
}
