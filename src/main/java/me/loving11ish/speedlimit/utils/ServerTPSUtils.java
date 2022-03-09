package me.loving11ish.speedlimit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ServerTPSUtils {

    private final DecimalFormat format = new DecimalFormat("##.##", DecimalFormatSymbols.getInstance(Locale.US));

    private Object serverInstance;
    private Field tpsField;

    public ServerTPSUtils() {
        try {
            Class<?> getNMSClass = Class.forName("net.minecraft.server.MinecraftServer");
            this.serverInstance = getNMSClass.getDeclaredMethod("getServer").invoke(null);
            this.tpsField = getNMSClass.getDeclaredField("recentTps");
            this.tpsField.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getTPS(int time) {
        try {
            double[] tps = ((double[]) tpsField.get(serverInstance));
            return format.format(tps[time]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
