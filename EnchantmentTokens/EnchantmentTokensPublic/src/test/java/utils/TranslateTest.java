package utils;

import org.bukkit.configuration.ConfigurationSection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedString;

import java.util.Collections;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocaleManager.class, TranslatedString.class })
public class TranslateTest {
    @Test
    public void testTranslate() {
        ConfigurationSection section = mock(ConfigurationSection.class);
        when(section.get("country-language")).thenReturn("US");

        LocaleManager.updateLocale(section, Collections.emptyList());
        Assert.assertEquals("Test", new TranslatedString("test").translate());
    }
}