package software.bigbade.enchantmenttokens.utils;

import org.bukkit.Bukkit;

public class NMSManager {
    private static final String SERVER_VERSION = getServerVersion();

    private static String getServerVersion() {
        Class<?> server = Bukkit.getServer().getClass();
        if (!server.getSimpleName().equals("CraftServer")) {
            return ".";
        }
        if (server.getName().equals("org.bukkit.craftbukkit.CraftServer")) {
            // Non versioned class
            return ".";
        } else {
            String version = server.getName().substring("org.bukkit.craftbukkit".length());
            return version.substring(0, version.length() - "CraftServer".length());
        }
    }

    public static String nms(String className) {
        return "net.minecraft.server" + SERVER_VERSION + className;
    }

    public static Class<?> nmsClass(String className) throws ClassNotFoundException {
        return Class.forName(nms(className));
    }

    public static String obc(String className) {
        return "org.bukkit.craftbukkit" + SERVER_VERSION + className;
    }

    public static Class<?> obcClass(String className) throws ClassNotFoundException {
        return Class.forName(obc(className));
    }

    private NMSManager() {}
}
