package me.loving11ish.speedlimit.externalhooks;

import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class PlugManXAPI {

    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static boolean isPlugManXEnabled() {
        try {
            Class.forName("com.rylinaux.plugman.PlugMan");
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aFound PlugManX main class at:"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &dcom.rylinaux.plugman.PlugMan"));
            return true;
        }catch (ClassNotFoundException e){
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &aCould not find PlugManX main class at:"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &dcom.rylinaux.plugman.PlugMan"));
            return false;
        }
    }
}
