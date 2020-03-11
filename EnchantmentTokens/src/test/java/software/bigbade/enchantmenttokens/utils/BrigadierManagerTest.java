package software.bigbade.enchantmenttokens.utils;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Test;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.logging.Logger;

public class BrigadierManagerTest extends EasyMockSupport {
    @Mock
    private EnchantmentTokens tokens = mock(EnchantmentTokens.class);

    @Mock
    private Command command = mock(Command.class);

    @Mock
    private PluginManager pluginManager = mock(PluginManager.class);

    @Mock
    private Server server = mock(Server.class);

    @Test
    public void testBrigadierManager() {
        //EasyMock.expect(command.getName()).andStubReturn("test");
        //EasyMock.expect(tokens.getServer()).andReturn(server);
        //EasyMock.expect(server.getPluginManager()).andReturn(pluginManager);

        //pluginManager.registerEvents(new Listener() {
        //}, tokens);
        //EasyMock.expectLastCall();
        //replayAll();

        //Bukkit.setServer(server);
        //BrigadierManager.register(tokens, command, "test");
    }
}