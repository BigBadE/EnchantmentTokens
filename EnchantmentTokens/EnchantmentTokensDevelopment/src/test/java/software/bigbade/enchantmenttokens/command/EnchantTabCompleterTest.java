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
import software.bigbade.enchantmenttokens.commands.EnchantTabCompleter;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

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
        when(key.toString()).thenReturn("namespace:test");
        when(key.getKey()).thenReturn("test");
        when(handler.getAllEnchants()).thenReturn(Collections.singletonList(new CustomEnchantment(key,null, "test") {

        }));

        Command command = mock(Command.class);
        EnchantTabCompleter tabCompleter = new EnchantTabCompleter(handler);
        testTabComplete(tabCompleter.onTabComplete(commandSender, command, "", new String[]{"tes"}));
        testTabComplete(tabCompleter.onTabComplete(commandSender, command, "", new String[]{"namesp"}));

    }

    private void testTabComplete(@Nullable List<String> results) {
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("namespace:test", results.get(0));
    }
}
