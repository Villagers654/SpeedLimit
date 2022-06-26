package me.loving11ish.speedlimit.events;

import me.loving11ish.speedlimit.SpeedLimit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class FlightEvent implements Listener {

    private FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();

    @EventHandler (priority = EventPriority.HIGHEST)
    public void OnFlightEvent(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (configFile.getList("disabled-Worlds").contains(player.getWorld().getName())){
            player.setFlySpeed((float) 0.1);
        }
        if (!(configFile.getList("disabled-Worlds").contains(player.getWorld().getName()))){
            if (!(player.hasPermission("SpeedLimit.bypass.flying")||player.hasPermission("SpeedLimit.bypass.*")
                    ||player.hasPermission("SpeedLimit.*")||player.isOp())) {
                if (configFile.getBoolean("flying-event.enabled")) {
                    player.setFlySpeed((float) configFile.getDouble("flying-event.speed"));
                }
            }else {
                player.setFlySpeed((float) 0.1);
            }
        }
    }
}
