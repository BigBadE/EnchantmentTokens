package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import software.bigbade.enchantmenttokens.commands.EnchantTabCompleter;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EnchantTabCompleter.class })
public class PriceIncreaseTest {

    @Test
    public void testPriceIncreases() {
        ConfigurationSection section = mock(ConfigurationSection.class);
        when(section.getInt("0")).thenReturn(100);

        Assert.assertEquals(100, PriceIncreaseTypes.CUSTOM.getPrice(0, section));
    }
}
