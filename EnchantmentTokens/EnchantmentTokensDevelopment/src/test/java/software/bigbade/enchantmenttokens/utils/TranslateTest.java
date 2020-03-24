package software.bigbade.enchantmenttokens.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Assert;
import org.junit.Test;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.util.Collections;

public class TranslateTest extends EasyMockSupport {
    @Mock
    private ConfigurationSection section = mock(ConfigurationSection.class);

    @Test
    public void testTranslate() {
        EasyMock.expect(section.get("country-language")).andReturn("US");
        replayAll();

        LocaleManager.updateLocale(section, Collections.emptyList());
        Assert.assertEquals("Test", TranslatedMessage.translate("test"));
    }
}
