package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(fullyQualifiedNames = "org.bukkit.configuration.ConfigurationSection")
public class PriceIncreaseTest {
    @Test
    public void testPriceIncreases() {
        ConfigurationSection mock = Mockito.mock(ConfigurationSection.class);
        PowerMockito.when(mock.getInt("1")).thenReturn(100);
        Assert.assertEquals(PriceIncreaseTypes.CUSTOM.getPrice(1, mock), 100);
    }
}
