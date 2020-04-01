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

package software.bigbade.enchantmenttokens.command;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;
import software.bigbade.enchantmenttokens.commands.tabcompleter.EnchantTabCompleter;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EnchantTabCompleter.class, NamespacedKey.class })
public class EnchantTabCompleterTest {

    @Test
    public void testTabCompleter() {
        CommandSender commandSender = mock(CommandSender.class);
        when(commandSender.hasPermission("enchanttoken.admin")).thenReturn(false);
        when(commandSender.isOp()).thenReturn(true);

        EnchantmentHandler handler = mock(EnchantmentHandler.class);
        NamespacedKey key = mock(NamespacedKey.class);
        when(key.getNamespace()).thenReturn("namespace");
        when(handler.getAllEnchants()).thenReturn(Collections.singletonList(new CustomEnchantment(key, null, "test") {

        }));

        PlayerHandler playerHandler = mock(PlayerHandler.class);

        Command command = mock(Command.class);
        EnchantTabCompleter tabCompleter = new EnchantTabCompleter(handler, playerHandler);
        testTabComplete(tabCompleter.onTabComplete(commandSender, command, "", new String[]{"tes"}));
        testTabComplete(tabCompleter.onTabComplete(commandSender, command, "", new String[]{"namesp"}));

    }

    private void testTabComplete(@Nullable List<String> results) {
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("namespace:test", results.get(0));
    }
}
