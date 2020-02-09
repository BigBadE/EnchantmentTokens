package software.bigbade.enchantmenttokens.utils.commands;

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

import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.commands.*;

import java.util.Objects;

public class CommandManager {
    public CommandManager(EnchantmentTokens main) {
        Objects.requireNonNull(main.getCommand("adminenchant")).setExecutor(new EnchantCmd(main.getEnchantmentHandler(), main.getUtils()));
        Objects.requireNonNull(main.getCommand("adminenchant")).setTabCompleter(new EnchantTabCompleter(main.getEnchantmentHandler()));

        Objects.requireNonNull(main.getCommand("addgems")).setExecutor(new AddGemCmd(main.getPlayerHandler()));
        Objects.requireNonNull(main.getCommand("addgems")).setTabCompleter(new AddGemTabCompleter());

        EnchantMenuCmd menuCmd = new EnchantMenuCmd(main.getFactory());
        Objects.requireNonNull(main.getCommand("tokenenchant")).setExecutor(menuCmd);
        Objects.requireNonNull(main.getCommand("tokenenchant")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(main.getCommand("gembal")).setExecutor(new BalanceCmd(main.getPlayerHandler()));
        Objects.requireNonNull(main.getCommand("gembal")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(main.getCommand("enchantlist")).setExecutor(new EnchantmentListCommand(main.getEnchantmentHandler()));
        Objects.requireNonNull(main.getCommand("enchantlist")).setTabCompleter(new GenericTabCompleter());

        Objects.requireNonNull(main.getCommand("reloadenchants")).setExecutor(new RecompileEnchantsCmd(main));
        Objects.requireNonNull(main.getCommand("reloadenchants")).setTabCompleter(new GenericTabCompleter());
    }
}
