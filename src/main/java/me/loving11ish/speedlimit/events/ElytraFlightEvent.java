package me.loving11ish.speedlimit.events;

import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ElytraFlightEvent implements Listener {

    public static Integer taskID1;
    private static double velocityTriggerMultiplier;

    private static final FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();
    private static final FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    private static final String PREFIX = messagesFile.getString("plugin-prefix");
    private static final String PREFIX_PLACEHOLDER = "%PREFIX%";

    public static void updateElytraTriggerValue(){
        taskID1 = Bukkit.getScheduler().scheduleSyncDelayedTask(SpeedLimit.getPlugin(), new Runnable() {
            @Override
            public void run() {
                velocityTriggerMultiplier = configFile.getDouble("elytra-flight-event.speed-limit.trigger-speed");
            }
        }, 10);
    }

    @EventHandler
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
                player.setGliding(false);
                player.teleport(location);
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
        if (player.hasPermission("SpeedLimit.bypass.elytra")||player.hasPermission("SpeedLimit.bypass")
                ||player.hasPermission("SpeedLimit.*")||player.isOp()){
            return;
        }
        if (player.isGliding()){
            if (configFile.getBoolean("tps.dynamic-elytra-check.enabled")){
                if (SpeedLimit.getServerTPS() <= configFile.getDouble("tps.dynamic-elytra-check.trigger-value")){
                    double yTop = event.getFrom().getY();
                    Location location = new Location(player.getWorld(), x, yTop, z, yaw, pitch);
                    yTop = location.getWorld().getHighestBlockYAt(location);
                    location.setY(yTop);
                    player.setGliding(false);
                    player.teleport(location);
                    if (configFile.getBoolean("tps.dynamic-elytra-check.send-warning-message")){
                        player.sendMessage(ColorUtils.translateColorCodes(
                                messagesFile.getString("TPS-elytras-disabled-warning").replace(PREFIX_PLACEHOLDER, PREFIX)));
                    }
                    return;
                }
                return;
            }
            if (Math.abs(event.getFrom().getX() - event.getTo().getX()) > velocityTriggerMultiplier
                    ||Math.abs(event.getFrom().getY() - event.getTo().getY()) > velocityTriggerMultiplier
                    ||Math.abs(event.getFrom().getZ() - event.getTo().getZ()) > velocityTriggerMultiplier){
               Location oldLocation = new Location(player.getWorld(), x, y, z, yaw, pitch);
                if (configFile.getBoolean("elytra-flight-event.speed-limit.cancel-event")){
                    event.setCancelled(true);
                }else {
                    player.teleport(oldLocation);
                }
                if (configFile.getBoolean("elytra-flight-event.speed-limit.send-message")){
                    player.sendMessage(ColorUtils.translateColorCodes(
                            messagesFile.getString("elytra-triggered-warning").replace(PREFIX_PLACEHOLDER, PREFIX)));
                }
            }
        }
    }
}
