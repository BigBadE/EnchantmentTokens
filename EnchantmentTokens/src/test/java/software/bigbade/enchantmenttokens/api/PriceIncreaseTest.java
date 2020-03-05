package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Test;

public class PriceIncreaseTest extends EasyMockSupport {
    @Mock
    private ConfigurationSection section = EasyMock.mock(ConfigurationSection.class);

    @Test
    public void testPriceIncreases() {
        EasyMock.expect(section.getInt("0")).andReturn(100);
        replayAll();
        Assert.assertEquals(100, PriceIncreaseTypes.CUSTOM.getPrice(0, section));
    }
}
