package software.bigbade.enchantmenttokens.utils;

import org.bukkit.Location;

import java.util.Set;

public interface SignHandler {
    Set<Location> getSigns();

    void addSign(Location sign);

    void removeSign(Location sign);
}
