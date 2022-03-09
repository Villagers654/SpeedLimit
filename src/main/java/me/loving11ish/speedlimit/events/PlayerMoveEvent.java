package me.loving11ish.speedlimit.events;

import me.loving11ish.speedlimit.SpeedLimit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {

    private static final FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();

    @EventHandler
    public void OnPlayerWalk (org.bukkit.event.player.PlayerMoveEvent event){
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
