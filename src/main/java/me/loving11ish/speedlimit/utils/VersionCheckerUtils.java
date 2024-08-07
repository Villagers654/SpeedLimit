package me.loving11ish.speedlimit.utils;

import me.loving11ish.speedlimit.SpeedLimit;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.util.logging.Level;
import java.util.regex.PatternSyntaxException;

public class VersionCheckerUtils {

    ConsoleCommandSender console = Bukkit.getConsoleSender();

    private final String serverPackage;
    private int version;

    public VersionCheckerUtils() {
        String version = Bukkit.getVersion();

        int startIndex = version.indexOf("(MC: ");
        int endIndex = version.indexOf(")", startIndex);

        serverPackage = version.substring(startIndex + 5, endIndex);
    }

    public void setVersion() {
        try {
            version = Integer.parseInt(serverPackage.split("\\.")[1]);
        }catch (NumberFormatException | PatternSyntaxException e){
            console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Unable to process server version!"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Some features may break unexpectedly!"));
            console.sendMessage(ColorUtils.translateColorCodes("&7[&bSpeed&3Limit&7]: &4Report any issues to the developer!"));
            console.sendMessage(ColorUtils.translateColorCodes("&c-------------------------------------------"));
        }
    }

    public String getServerPackage() {
        return serverPackage;
    }

    public int getVersion() {
        return version;
    }
}
