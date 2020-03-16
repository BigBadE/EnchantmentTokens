package software.bigbade.enchantmenttokens.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Test;
import software.bigbade.enchantmenttokens.api.CustomEnchantment;
import software.bigbade.enchantmenttokens.commands.EnchantTabCompleter;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import java.util.Collections;
import java.util.List;

public class EnchantTabCompleterTest extends EasyMockSupport {
    @Mock
    private EnchantmentHandler handler = mock(EnchantmentHandler.class);

    @Mock
    private CommandSender commandSender = mock(CommandSender.class);

    @Test
    public void testTabCompleter() {
        EasyMock.expect(commandSender.hasPermission("enchanttoken.admin")).andReturn(false).times(2);
        EasyMock.expect(commandSender.isOp()).andReturn(true).times(2);
        EasyMock.expect(handler.getAllEnchants()).andReturn(Collections.singletonList(new CustomEnchantment("test", null, "namespace") {

        })).times(2);
        commandSender.sendMessage("namespace:test");
        EasyMock.expectLastCall().times(2);
        replayAll();

        EnchantTabCompleter tabCompleter = new EnchantTabCompleter(handler);
        List<String> results = tabCompleter.onTabComplete(commandSender, EasyMock.mock(Command.class), "", new String[]{"tes"});
        assert results != null;
        assert !results.isEmpty();
        assert results.get(0).equals("namespace:test");
        results = tabCompleter.onTabComplete(commandSender, EasyMock.mock(Command.class), "", new String[]{"namesp"});
        assert results != null;
        assert !results.isEmpty();
        assert results.get(0).equals("namespace:test");
    }
}
