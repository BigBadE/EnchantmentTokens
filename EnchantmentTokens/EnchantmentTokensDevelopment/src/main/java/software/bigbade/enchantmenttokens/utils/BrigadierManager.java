/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.utils;

import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileFormat;
import org.bukkit.command.Command;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class BrigadierManager {

    //Private constructor to hide implicit public one.
    private BrigadierManager() {}

    public static void register(EnchantmentTokens tokens, Command command, String permission) {
        Commodore commodore = CommodoreProvider.getCommodore(tokens);
        try (InputStream is = BrigadierManager.class.getResourceAsStream("/commodore/" + command.getName() + ".commodore")) {
            if(is == null) {
                return;
            }
            LiteralCommandNode<?> commandNode = CommodoreFileFormat.parse(is);
            commodore.register(command, commandNode, player ->
                    permission != null && player.hasPermission(permission) || player.isOp());
        } catch (IOException e) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Error loading commodore", e);
        }
    }
}
