package software.bigbade.enchantmenttokens.utils.enchants;


import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.Material;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.utils.ItemUtils;

import java.io.File;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EnchantmentFileLoader.class, File.class, EnchantmentTokens.class, ItemUtils.class})
public class EnchantmentFileLoaderTest {
    private final File file = mock(File.class);
    private final TaskChain<Object> chain = mock(TaskChain.class);
    //Must be mocked after the ItemUtils is
    private EnchantmentTokens main;

    @Before
    public void setupTest() {
        mockStatic(ItemUtils.class);
        when(ItemUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " ")).thenReturn(null);

        mockStatic(EnchantmentTokens.class);
        main = mock(EnchantmentTokens.class);

        when(EnchantmentTokens.newChain()).thenReturn(chain);
        when(chain.async(ArgumentMatchers.any())).thenCallRealMethod();

        when(file.listFiles()).thenThrow(new RuntimeException());
    }

    @Test
    public void testFileLoader() {
        EnchantmentFileLoader loader = new EnchantmentFileLoader(file, main);

    }
}