package software.bigbade.enchantmenttokens.loader;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import software.bigbade.enchantmenttokens.utils.ByteUtils;
import software.bigbade.enchantmenttokens.utils.ConfigurationManager;
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
                byte[] gems = new byte[8];
                stream.read(gems, offset, 8);
                return utils.bytesToLong(gems);
            } catch (IOException e) {
                EnchantLogger.log(Level.SEVERE, "Could not read player data", e);
            }
        }
        return 0;
    }

    public void removePlayer(Player player) {
        for (Map.Entry<UUID, Long> removing : cache.entrySet()) {
            if (player.getUniqueId().equals(removing.getKey())) {
                savePlayer(player, removing.getValue());
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

            FileOutputStream output = new FileOutputStream(playerFile);
            output.write(utils.longToBytes(gems), passed, 8);
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
                passed += stream.read(temp);
                long mostSigBits = utils.bytesToLong(temp);
                if (mostSigBits == id.getMostSignificantBits()) {
                    passed += stream.read(temp);
                    long leastSigBits = utils.bytesToLong(temp);
                    if (leastSigBits == id.getLeastSignificantBits()) {
                        passed += 16;
                        break;
                    } else {
                        passed += stream.skip(8);
                    }
                } else {
                    passed += stream.skip(16);
                }
            }
            return passed;
        } catch (IOException e) {
            EnchantLogger.log(Level.SEVERE, "Error reading player save data", e);
        }
        return -1;
    }

    public ByteUtils getUtils() {
        return utils;
    }
}