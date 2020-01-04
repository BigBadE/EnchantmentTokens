package bigbade.enchantmenttokens.loader;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.utils.ByteUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FileLoader {
    private EnchantmentTokens main;
    private ByteUtils utils = new ByteUtils();
    private List<EnchantmentPlayer> cache = new ArrayList<>();

    public FileLoader(EnchantmentTokens main) {
        this.main = main;
    }

    public EnchantmentPlayer loadPlayer(Player player) {
        for (EnchantmentPlayer player1 : cache)
            if (player1.getPlayer() == player)
                return player1;
        if (cache.size() > 50) saveCache();
        EnchantmentPlayer player1 = EnchantmentPlayer.loadPlayer(player, main, utils);
        cache.add(player1);
        return player1;
    }

    public void savePlayer(EnchantmentPlayer player, boolean remove) {
        player.save(utils, main);
        if(remove)
            cache.remove(player);
    }

    public void saveCache() {
        EnchantmentTokens.LOGGER.info("Saving player cache!");
        for (EnchantmentPlayer player : cache)
            savePlayer(player, false);
        cache.clear();
    }
}