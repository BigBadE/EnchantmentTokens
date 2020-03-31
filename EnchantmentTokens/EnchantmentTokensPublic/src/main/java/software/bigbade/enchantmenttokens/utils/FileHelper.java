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

package software.bigbade.enchantmenttokens.utils;

import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class FileHelper {
    //Private constructor to hide implicit public one
    private FileHelper() {}

    public static JarFile getJarFile(String path) {
        try {
            return new JarFile(path);
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load jar at " + path);
        }
        return null;
    }

    public static InputStream getJarStream(JarFile file, String name) {
        if(file == null)
            return null;
        try {
            return file.getInputStream(file.getJarEntry(name));
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load file from jar named " + file.getName());
        }
        return null;
    }
}
