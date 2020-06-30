/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.loader;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
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
import java.util.logging.Level;

public class FileLoader {
    private static final String ERROR = "Could not save player data";
    private static final String EOF_ERROR = "Unexpected EOF!";

    private final String path;

    public FileLoader(String path) {
        this.path = path;
    }

    public void getData(Player player, GemCurrencyHandler currencyHandler) {
        File playerFile = new File(path + "\\data\\" + player.getUniqueId().toString().substring(0, 2) + "\\data.dat");
        if (!playerFile.exists()) {
            ConfigurationManager.createFolder(playerFile.getParentFile());
            ConfigurationManager.createFile(playerFile);
        } else {
            try (FileInputStream stream = new FileInputStream(playerFile)) {
                int offset = getOffset(stream, player.getUniqueId());

                if (offset == -1) {
                    return;
                }
                currencyHandler.setAmount(safeReadLong(stream));
                currencyHandler.setLocale(safeReadLocale(stream));

                int current = safeReadInteger(stream);
                while (current != 0) {
                    String key = safeReadString(stream, current);
                    current = safeReadInteger(stream);
                    String value = safeReadString(stream, current);
                    current = safeReadInteger(stream);
                    currencyHandler.addRawData(key, value);
                }
            } catch (IOException e) {
                EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not read player data", e);
            }
        }
    }

    public void savePlayer(EnchantmentPlayer player, GemCurrencyHandler handler) {
        File playerFile = new File(path + "\\data\\" + player.getPlayer().getUniqueId().toString().substring(0, 2) + "\\data.dat");
        try (FileInputStream stream = new FileInputStream(playerFile)) {
            int passed = getOffset(stream, player.getPlayer().getUniqueId());
            player.getGems().thenAccept(gems -> {
                try (FileOutputStream output = new FileOutputStream(playerFile)) {
                    writeData(handler, gems, passed, output);
                } catch (IOException e) {
                    EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, ERROR, e);
                }
            });
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, ERROR, e);
        }
    }

    private void writeData(GemCurrencyHandler currencyHandler, long gems, int index, FileOutputStream output) throws IOException {
        output.write(ByteUtils.longToBytes(gems), index, 8);
        index += 8;
        output.write(currencyHandler.getLocale().toLanguageTag().getBytes(), index, currencyHandler.getLocale().toLanguageTag().length());
        for (Map.Entry<String, String> entries : currencyHandler.getStoredData().entrySet()) {
            String key = entries.getKey();
            output.write(ByteUtils.intToBytes(key.length()), index, Integer.BYTES);
            index += Integer.BYTES;
            output.write(key.getBytes(), index, key.length());
            index += key.length();
            String value = entries.getValue();
            output.write(ByteUtils.intToBytes(value.length()), index, Integer.BYTES);
            index += Integer.BYTES;
            output.write(value.getBytes());
        }
        output.write(0);
    }

    private int getOffset(InputStream stream, UUID id) {

        int passed = 0;
        try {
            while (true) {
                long mostSigBits = safeReadLong(stream);
                if (mostSigBits == id.getMostSignificantBits()) {
                    long leastSigBits = safeReadLong(stream);
                    if (leastSigBits == id.getLeastSignificantBits()) {
                        passed += 16;
                        return passed;
                    } else {
                        passed += 16;
                    }
                } else {
                    passed += Long.BYTES + stream.skip(8);
                }
                int current = safeReadInteger(stream);
                passed += Integer.BYTES;
                while (current != 0) {
                    passed += stream.skip(current);
                    current = safeReadInteger(stream);
                    passed += Integer.BYTES;
                }
            }
        } catch (IOException e) {
            //EOF reached
            return passed;
        }
    }

    private long safeReadLong(InputStream stream) throws IOException {
        byte[] temp = new byte[Long.BYTES];
        if (stream.read(temp) != Long.BYTES) {
            throw new IOException(EOF_ERROR);
        }
        return ByteUtils.bytesToLong(temp);
    }

    private int safeReadInteger(InputStream stream) throws IOException {
        byte[] temp = new byte[Integer.BYTES];
        if (stream.read(temp) != Integer.BYTES) {
            throw new IOException(EOF_ERROR);
        }
        return ByteUtils.bytesToInt(temp);
    }

    private String safeReadString(InputStream stream, int length) throws IOException {
        byte[] temp = new byte[length];
        if (stream.read(temp) != length) {
            throw new IOException(EOF_ERROR);
        }
        return new String(temp);
    }

    private Locale safeReadLocale(InputStream stream) throws IOException {
        byte[] temp = new byte[5];
        if (stream.read(temp) != 5) {
            throw new IOException(EOF_ERROR);
        }
        return Locale.forLanguageTag(new String(temp));
    }
}