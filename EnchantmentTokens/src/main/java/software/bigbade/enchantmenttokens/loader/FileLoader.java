package software.bigbade.enchantmenttokens.loader;

import software.bigbade.enchantmenttokens.utils.ByteUtils;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class FileLoader {
    private String path;
    private ByteUtils utils = new ByteUtils();
    private Map<UUID, Long> cache = new ConcurrentHashMap<>();

    public FileLoader(String path) {
        this.path = path;
    }

    public long getGems(Player player) {
        for (Map.Entry<UUID, Long> entrySet : cache.entrySet())
            if (entrySet.getKey().equals(player.getUniqueId()))
                return entrySet.getValue();
        return loadGems(player);
    }

    private long loadGems(Player player) {
        File playerFile = new File(path + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        if (!playerFile.exists()) {
            ConfigurationManager.createFolder(playerFile.getParentFile());
            ConfigurationManager.createFile(playerFile);
        } else {
            try {
                FileInputStream stream = new FileInputStream(playerFile);
                int offset = getOffset(stream, player.getUniqueId());

                if(offset == -1) {
                    cache.put(player.getUniqueId(), 0L);
                    return 0;
                }

                byte[] gemsBytes = new byte[8];
                stream.read(gemsBytes, 0, 8);
                long gems = utils.bytesToLong(gemsBytes);
                cache.put(player.getUniqueId(), gems);
                return gems;
            } catch (IOException e) {
                EnchantLogger.log(Level.SEVERE, "Could not read player data", e);
            }
        }
        return 0;
    }

    public void removePlayer(Player player, long gems) {
        for (Map.Entry<UUID, Long> removing : cache.entrySet()) {
            if (player.getUniqueId().equals(removing.getKey())) {
                savePlayer(player, gems);
                cache.remove(removing.getKey());
                return;
            }
        }
    }

    private void savePlayer(Player player, long gems) {
        File playerFile = new File(path + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        try {
            FileInputStream stream = new FileInputStream(playerFile);
            int passed = getOffset(stream, player.getUniqueId());
            stream.close();

            FileOutputStream output;
            if(passed == -1) {
                output = new FileOutputStream(playerFile, true);
                output.write(utils.longToBytes(player.getUniqueId().getMostSignificantBits()));
                output.write(utils.longToBytes(player.getUniqueId().getLeastSignificantBits()));
                output.write(utils.longToBytes(gems));
            } else {
                output = new FileOutputStream(playerFile);
                output.write(utils.longToBytes(gems), passed, 8);
            }
            output.close();
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Error saving data", e);
        }
    }

    private int getOffset(InputStream stream, UUID id) {
        try {
            int passed = 0;
            for (; passed < stream.available(); passed += 24) {
                byte[] temp = new byte[8];
                stream.read(temp);
                long mostSigBits = utils.bytesToLong(temp);
                if (mostSigBits == id.getMostSignificantBits()) {
                    stream.read(temp);
                    long leastSigBits = utils.bytesToLong(temp);
                    if (leastSigBits == id.getLeastSignificantBits()) {
                        passed += 16;
                        return passed;
                    }
                }
            }
            return -1;
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Error reading player save data", e);
        }
        return -1;
    }

    public ByteUtils getUtils() {
        return utils;
    }
}