package me.loving11ish.speedlimit.events;

import me.loving11ish.speedlimit.SpeedLimit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerWalkEvent implements Listener {

    private FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();

    @EventHandler (priority = EventPriority.HIGH)
    public void OnPlayerWalk (PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (configFile.getList("disabled-Worlds").contains(player.getWorld().getName())){
            player.setWalkSpeed((float) 0.2);
        }
        if (!(configFile.getList("disabled-Worlds").contains(player.getWorld().getName()))){
            if (!(player.hasPermission("SpeedLimit.bypass.walking")||player.hasPermission("SpeedLimit.bypass.*")
                    ||player.hasPermission("SpeedLimit.*")||player.isOp())) {
                if (configFile.getBoolean("walking-event.enabled")) {
                    player.setWalkSpeed((float) configFile.getDouble("walking-event.speed"));
                }
            }else {
                player.setWalkSpeed((float) 0.2);
            }
        }
    }
}
