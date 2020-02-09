package software.bigbade.enchantmenttokens.utils;

import org.junit.Assert;
import org.junit.Test;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.util.Collections;
import java.util.Locale;

public class TranslateTest {
    @Test
    public void testTranslate() {
        LocaleManager.updateLocale(Locale.US, Collections.emptyList());
        Assert.assertEquals("Test", TranslatedMessage.translate("test"));
    }
}
