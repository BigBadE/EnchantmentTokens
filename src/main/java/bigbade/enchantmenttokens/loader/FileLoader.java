package bigbade.enchantmenttokens.loader;

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

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.utils.ByteUtils;
import bigbade.enchantmenttokens.utils.EnchantmentPlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileLoader {
    private EnchantmentTokens main;
    private ByteUtils utils = new ByteUtils();
    private Map<UUID, Long> cache = new ConcurrentHashMap<>();

    public FileLoader(EnchantmentTokens main) {
        this.main = main;
    }

    public long getGems(Player player) {
        for (Map.Entry<UUID, Long> entrySet : cache.entrySet())
            if (entrySet.getKey().equals(player.getUniqueId()))
                return entrySet.getValue();
        return loadGems(player);
    }

    private long loadGems(Player player) {
        File playerFile = new File(main.getDataFolder().getAbsolutePath() + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        if (!playerFile.exists()) {
            try {
                playerFile.getParentFile().mkdir();
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileInputStream stream = new FileInputStream(playerFile);
                for (int i = 0; i < stream.available() / 24; i += 1) {
                    byte[] temp = new byte[8];
                    stream.read(temp);
                    long mostSigBits = utils.bytesToLong(temp);
                    if (mostSigBits == player.getUniqueId().getMostSignificantBits()) {
                        stream.read(temp);
                        long leastSigBits = utils.bytesToLong(temp);
                        if (leastSigBits == player.getUniqueId().getLeastSignificantBits()) {
                            stream.read(temp);
                            return utils.bytesToLong(temp);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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
        File playerFile = new File(main.getDataFolder().getAbsolutePath() + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        try {
            FileInputStream stream = new FileInputStream(playerFile);
            int passed = 0;
            for (int i = 0; i < stream.available() / 24; i += 1) {
                byte[] temp = new byte[8];
                stream.read(temp);
                long mostSigBits = utils.bytesToLong(temp);
                if (mostSigBits == player.getUniqueId().getMostSignificantBits()) {
                    stream.read(temp);
                    long leastSigBits = utils.bytesToLong(temp);
                    if (leastSigBits == player.getUniqueId().getLeastSignificantBits()) {
                        passed += 16;
                        break;
                    } else {
                        stream.skip(8);
                    }
                } else {
                    stream.skip(16);
                }
                passed += 24;
            }

            FileOutputStream output = new FileOutputStream(playerFile);
            output.write(utils.longToBytes(gems), passed, 8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ByteUtils getUtils() {
        return utils;
    }
}