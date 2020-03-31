/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.skript.type;

import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import java.io.IOException;
import java.util.logging.Level;

public class SkriptManager {
    public SkriptManager(SkriptAddon addon) {
        try {
            Classes.registerClass(new ClassInfo<>(SkriptEnchantment.class, "customenchant").user("CustomEnchantments")
                    .name("CustomEnchantment")
                    .description("A custom enchantment.")
                    .parser(new BaseParser())
                    .serializer(new BaseSerializer()));
            addon.loadClasses("software.bigbade.enchantmenttokens", "skript");
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Could not load Skript compatibility", e);
        }
    }
}
