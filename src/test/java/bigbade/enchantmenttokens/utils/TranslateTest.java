package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.localization.TranslatedMessage;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Locale;

public class TranslateTest {
    @Test
    public void testTranslate() {
        LocaleManager.updateLocale(Locale.US, new ArrayList<>());
        Assert.assertEquals("Enchantment", TranslatedMessage.translate("enchantment"));
    }
}
