/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
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

package com.bigbade.enchantmenttokens.logging;

import com.google.common.collect.Sets;
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.EnchantmentAddon;

import java.io.File;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UnwrappedErrorLogger {
    private static final Set<String> blockedPackages = Sets.newHashSet("com.bigbade.enchantmenttokens", "java");

    private UnwrappedErrorLogger() {
    }

    public static void logFileError(File jar, Throwable error) {
        EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error loading addon {0}", jar.getName());
        logError(EnchantmentTokens.getEnchantLogger(), unwrapThrowable(error));
    }

    public static void logAddonError(EnchantmentAddon addon, Throwable error) {
        addon.getLogger().log(Level.SEVERE, "Error loading addon {0} (v{1}). {2}", new Object[]{addon.getName(), addon.getDescription().getVersion(), ((addon.getErrorTracker() != null) ? " Please report this to " + addon.getErrorTracker() : "")});
        logError(addon.getLogger(), unwrapThrowable(error));
    }

    private static Throwable unwrapThrowable(Throwable error) {
        Throwable throwable = error;
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable;
    }

    private static void logError(Logger logger, Throwable error) {
        for (StackTraceElement element : error.getStackTrace()) {
            String string = element.toString();
            if (isBlockedPackage(string)) {
                continue;
            }
            logger.log(Level.SEVERE, "at {0}", string);
        }
    }

    private static boolean isBlockedPackage(String element) {
        for (String blockedPackage : blockedPackages) {
            if (element.startsWith(blockedPackage)) {
                return true;
            }
        }
        return false;
    }
}
