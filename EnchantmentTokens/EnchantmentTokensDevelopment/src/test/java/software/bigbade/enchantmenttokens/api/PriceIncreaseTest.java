package software.bigbade.enchantmenttokens.api;

import org.bukkit.configuration.ConfigurationSection;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Test;

public class PriceIncreaseTest extends EasyMockSupport {
    @Mock
    private ConfigurationSection section = mock(ConfigurationSection.class);

    @Test
    public void testPriceIncreases() {
        EasyMock.expect(section.getInt("0")).andReturn(100).times(2);
        replayAll();

        assert section.getInt("0") == 100;
        assert PriceIncreaseTypes.CUSTOM.getPrice(0, section) == 100;
        verifyAll();
    }
}
