package me.loving11ish.speedlimit.commands;

import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SLHelp implements CommandExecutor {
    
    ConsoleCommandSender console = Bukkit.getConsoleSender();

    FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    String PREFIX = ColorUtils.translateColorCodes(messagesFile.getString("plugin-prefix"));
    final String PREFIX_PLACEHOLDER = "%PREFIX%";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("speedlimit.command.help")||player.hasPermission("speedlimit.command.*")
                    ||player.hasPermission("speedlimit.*")||player.isOp()){
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-1").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-2").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-3").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-4").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-5").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-6").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-7").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-8").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-9").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-10").replace(PREFIX_PLACEHOLDER, PREFIX)));
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-11").replace(PREFIX_PLACEHOLDER, PREFIX)));
            }else {
                player.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("help-no-permission").replace(PREFIX_PLACEHOLDER, PREFIX)));
            }
        }else {
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-1").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-2").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-3").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-4").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-5").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-6").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-7").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-8").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-9").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-10").replace(PREFIX_PLACEHOLDER, PREFIX)));
            console.sendMessage(ColorUtils.translateColorCodes(messagesFile.getString("plugin-help-11").replace(PREFIX_PLACEHOLDER, PREFIX)));
        }
        return true;
    }
}
