package software.bigbade.enchantmenttokens.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.util.Collections;
import java.util.Locale;

public class TranslateTest {
    @Test
    public void testTranslate() {
        ConfigurationSection mock = Mockito.mock(ConfigurationSection.class);
        PowerMockito.when(mock.getString("country-language")).thenReturn("US");
        LocaleManager.updateLocale(mock, Collections.emptyList());
        Assert.assertEquals("Test", TranslatedMessage.translate("test"));
    }
}
