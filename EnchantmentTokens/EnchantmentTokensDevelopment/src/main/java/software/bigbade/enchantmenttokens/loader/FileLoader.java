/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.loader;

import javafx.util.Pair;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.ByteUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class FileLoader {
    private String path;
    private ByteUtils utils = new ByteUtils();
    private Map<UUID, Pair<Long, Locale>> cache = new ConcurrentHashMap<>();

    public FileLoader(String path) {
        this.path = path;
    }

    public Pair<Long, Locale> getData(Player player) {
        for (Map.Entry<UUID, Pair<Long, Locale>> entrySet : cache.entrySet())
            if (entrySet.getKey().equals(player.getUniqueId()))
                return entrySet.getValue();
        return loadData(player);
    }

    private Pair<Long, Locale> loadData(Player player) {
        File playerFile = new File(path + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        if (!playerFile.exists()) {
            ConfigurationManager.createFolder(playerFile.getParentFile());
            ConfigurationManager.createFile(playerFile);
        } else {
            try (FileInputStream stream = new FileInputStream(playerFile)) {
                int offset = getOffset(stream, player.getUniqueId());

                if (offset == -1) {
                    Pair<Long, Locale> pair = new Pair<>(0L, Locale.getDefault());
                    cache.put(player.getUniqueId(), pair);
                    return pair;
                }

                long gems = safeReadLong(stream);
                Locale locale = safeReadLocale(stream);
                Pair<Long, Locale> pair = new Pair<>(gems, locale);
                cache.put(player.getUniqueId(), pair);
                return pair;
            } catch (IOException e) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not read player data", e);
            }
        }
        return new Pair<>(0L, Locale.getDefault());
    }

    public void removePlayer(Player player, long gems, Locale locale) {
        for (Map.Entry<UUID, Pair<Long, Locale>> removing : cache.entrySet()) {
            if (player.getUniqueId().equals(removing.getKey())) {
                try {
                    savePlayer(player, gems, locale);
                } catch (IOException e) {
                    EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not save player data", e);
                }
                cache.remove(removing.getKey());
                return;
            }
        }
    }

    private void savePlayer(Player player, long gems, Locale locale) throws IOException {
        File playerFile = new File(path + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        FileInputStream stream = new FileInputStream(playerFile);
        int passed = getOffset(stream, player.getUniqueId());
        stream.close();
        if (passed == -1) {
            try (FileOutputStream output = new FileOutputStream(playerFile, true)) {
                output.write(utils.longToBytes(player.getUniqueId().getMostSignificantBits()));
                output.write(utils.longToBytes(player.getUniqueId().getLeastSignificantBits()));
                output.write(utils.longToBytes(gems));
                output.write(locale.toLanguageTag().getBytes());
            }
        } else {
            try(FileOutputStream output = new FileOutputStream(playerFile)) {
                output.write(utils.longToBytes(gems), passed, 8);
                output.write(locale.toLanguageTag().getBytes(), passed + 8, locale.toLanguageTag().length());
            }
        }
    }

    private int getOffset(InputStream stream, UUID id) {
        try {
            int passed = 0;
            for (; passed < stream.available(); passed += 29) {
                long mostSigBits = safeReadLong(stream);
                if (mostSigBits == id.getMostSignificantBits()) {
                    long leastSigBits = safeReadLong(stream);
                    if (leastSigBits == id.getLeastSignificantBits()) {
                        passed += 16;
                        return passed;
                    }
                }
            }
            return -1;
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error reading player save data", e);
        }
        return -1;
    }

    private long safeReadLong(InputStream stream) throws IOException {
        byte[] temp = new byte[8];
        if (stream.read(temp) != 8) {
            throw new IOException("Unexpected end of file!");
        }
        return utils.bytesToLong(temp);
    }

    private Locale safeReadLocale(InputStream stream) throws IOException {
        byte[] temp = new byte[5];
        if (stream.read(temp) != 5) {
            throw new IOException("Unexpected end of file!");
        }
        return Locale.forLanguageTag(new String(temp));
    }

    public ByteUtils getUtils() {
        return utils;
    }
}