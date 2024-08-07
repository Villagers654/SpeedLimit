package me.loving11ish.speedlimit.commands;

import com.tcoded.folialib.FoliaLib;
import me.loving11ish.speedlimit.events.ElytraFlightEvent;
import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class SLReload implements CommandExecutor {

    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = ColorUtils.translateColorCodes(messagesFile.getString("plugin-prefix"));
    final String PREFIX_PLACEHOLDER = "%PREFIX%";
    private FoliaLib foliaLib = SpeedLimit.getPlugin().getFoliaLib();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("speedlimit.command.reload")||player.hasPermission("speedlimit.command.*")
                    ||player.hasPermission("speedlimit.*")||player.isOp()){
                this.reloadPlugin(sender);
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-no-permission").replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        }else {
            this.reloadPlugin(sender);
        }
        return true;
    }

    private void reloadPlugin(CommandSender sender) {
        sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-begin-1").replace(PREFIX_PLACEHOLDER, PREFIX)));
        sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-begin-2").replace(PREFIX_PLACEHOLDER, PREFIX)));
        sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-begin-3").replace(PREFIX_PLACEHOLDER, PREFIX)));
        sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-begin-4").replace(PREFIX_PLACEHOLDER, PREFIX)));
        SpeedLimit.getPlugin().onDisable();
        foliaLib.getScheduler().runLater((task) ->
                Bukkit.getPluginManager().getPlugin("SpeedLimit").onEnable(), 5L, TimeUnit.SECONDS);
        foliaLib.getScheduler().runLater((task) -> {
            SpeedLimit.getPlugin().reloadConfig();
            SpeedLimit.getPlugin().messagesDataManager.reloadMessagesConfig();
            ElytraFlightEvent.updateElytraTriggerValue();
            sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-successful-1").replace(PREFIX_PLACEHOLDER, PREFIX)));
            sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-successful-2").replace(PREFIX_PLACEHOLDER, PREFIX)));
            sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-successful-3").replace(PREFIX_PLACEHOLDER, PREFIX)));
            sender.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("reload-successful-4").replace(PREFIX_PLACEHOLDER, PREFIX)));
        }, 5L, TimeUnit.SECONDS);
    }

}
