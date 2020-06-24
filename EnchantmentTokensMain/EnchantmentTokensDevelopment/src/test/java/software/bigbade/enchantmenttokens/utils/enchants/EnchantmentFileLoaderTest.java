package software.bigbade.enchantmenttokens.utils.enchants;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentAddon;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.wrappers.EnchantmentChain;
import software.bigbade.enchantmenttokens.api.wrappers.EnchantmentTasks;
import software.bigbade.enchantmenttokens.localization.LocaleManager;
import software.bigbade.enchantmenttokens.utils.ItemUtils;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerHandler;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(
        {EnchantmentFileLoader.class, File.class, EnchantmentTokens.class,
                ItemUtils.class, Executors.class, Thread.class, JarFile.class, Class.class,
                PluginDescriptionFile.class, Bukkit.class, ReflectionManager.class, LocaleManager.class})
public class EnchantmentFileLoaderTest {
    private final File file = mock(File.class);
    private final EnchantmentChain chain = mock(EnchantmentChain.class);
    private final ExecutorService executor = new FakeExecutorService();
    private final ListenerHandler handler = mock(ListenerHandler.class);
    private final EnchantmentLoader enchantmentLoader =
            mock(EnchantmentLoader.class);
    private final EnchantmentAddon addon = mock(EnchantmentAddon.class);
    private final EnchantmentHandler enchantmentHandler =
            mock(EnchantmentHandler.class);
    private final JarFile jarFile = mock(JarFile.class);
    private final InputStream stream = mock(InputStream.class);
    private final PluginDescriptionFile descriptionFile =
            mock(PluginDescriptionFile.class);
    private final URLClassLoader classLoader = mock(URLClassLoader.class);
    private final Thread thread = mock(Thread.class);
    private final Logger logger = mock(Logger.class);
    private final JarEntry nonClass = mock(JarEntry.class);
    private final JarEntry directory = mock(JarEntry.class);
    private final JarEntry correctClass = mock(JarEntry.class);
    private final JarEntry[] entries =
            new JarEntry[]{directory, nonClass, correctClass};
    // Must be mocked after the ItemUtils is
    private EnchantmentTokens main;

    @Before
    @SneakyThrows
    public void setupTest() {
        mockStatic(ItemUtils.class);
        mockStatic(Bukkit.class);
        mockStatic(LocaleManager.class);
        when(ItemUtils.createItem(Material.BLACK_STAINED_GLASS_PANE, " "))
                .thenReturn(null);
        when(Bukkit.getVersion()).thenReturn("v1.15.2");
        mockStatic(Executors.class);
        mockStatic(ReflectionManager.class);

        whenNew(JarFile.class)
                .withArguments("testing/test.jar")
                .thenReturn(jarFile);
        whenNew(PluginDescriptionFile.class)
                .withArguments(stream)
                .thenReturn(descriptionFile);
        whenNew(URLClassLoader.class).withAnyArguments().thenReturn(classLoader);
        mockStatic(EnchantmentTokens.class);
        when(EnchantmentTokens.getEnchantLogger()).thenReturn(logger);
        main = mock(EnchantmentTokens.class);
        when(main.getListenerHandler()).thenReturn(handler);
        when(main.getEnchantmentLoader()).thenReturn(enchantmentLoader);
        when(main.getEnchantmentHandler()).thenReturn(enchantmentHandler);

        mockStatic(Thread.class);
        when(Thread.currentThread()).thenReturn(thread);

        when(Executors.newCachedThreadPool()).thenReturn(executor);

        whenNew(EnchantmentChain.class).withNoArguments().thenReturn(chain);
        when(chain.async(ArgumentMatchers.any(EnchantmentTasks.EnchantmentTask.class))).then(invocationOnMock -> {
            ((EnchantmentTasks.EnchantmentTask) invocationOnMock.getArgument(0)).runGeneric();
            return chain;
        });

        when(file.listFiles()).thenReturn(new File[]{file});
        when(file.getName()).thenReturn("test.jar");
        when(file.getAbsolutePath()).thenReturn("testing/test.jar");
        Future<?> future = mock(Future.class);
        when(future.get()).thenReturn(null);
        when(jarFile.getEntry("addon.yml")).thenReturn(null);
        when(jarFile.getInputStream(null)).thenReturn(stream);
        when(descriptionFile.getMain()).thenReturn("test.TestAddon");
        PowerMockito.<Class<?>>when(classLoader.loadClass("test.TestAddon"))
                .thenReturn(EnchantmentAddon.class);
        when(ReflectionManager.instantiate(EnchantmentAddon.class))
                .thenReturn(addon);
        when(jarFile.entries()).thenReturn(new FakeFileEnumeration(entries));
        when(directory.isDirectory()).thenReturn(true);
        when(nonClass.isDirectory()).thenReturn(false);
        when(correctClass.isDirectory()).thenReturn(false);
        when(nonClass.getName()).thenReturn("Test");
        when(correctClass.getName()).thenReturn("my/package/TestEnchant.class");
        PowerMockito.<Class<?>>when(classLoader.loadClass("my.package.TestEnchant"))
                .thenReturn(EnchantmentBase.class);
        when(main.getConfig()).thenReturn(null);
    }

    @Test
    public void testFileLoader() {
        EnchantmentFileLoader loader = new EnchantmentFileLoader(file, main);
        verify(main).saveConfig();
        verify(handler).registerListeners();
        Set<Class<EnchantmentBase>> classes = new HashSet<>();
        classes.add(EnchantmentBase.class);
        verify(addon).setup(main, descriptionFile);
        verify(enchantmentLoader).loadAddon(addon);
        verify(enchantmentLoader)
                .loadEnchantments(addon, enchantmentHandler, classes);
        verify(thread).setName("Enchantment-Loader");
        Assert.assertEquals(1, loader.getAddons().size());
        Optional<EnchantmentAddon> addonOptional =
                loader.getAddons().stream().findAny();
        addonOptional.orElseThrow(
                () -> new RuntimeException("Stream didn't find the addon"));
        addonOptional.ifPresent(
                (foundAddon) -> Assert.assertEquals(addon, foundAddon));

        when(file.listFiles()).thenReturn(null);
        loader.loadJars();
    }
}

@RequiredArgsConstructor
class FakeFileEnumeration implements Enumeration<JarEntry> {
    private final JarEntry[] elements;
    private int index = 0;

    @Override
    public boolean hasMoreElements() {
        return index <= elements.length - 1;
    }

    @Override
    public JarEntry nextElement() {
        JarEntry element = elements[index];
        index++;
        return element;
    }
}

class FakeExecutorService implements ExecutorService {
    @Override
    public void shutdown() {
    }

    @Nonnull
    @Override
    public List<Runnable> shutdownNow() {
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, @Nonnull TimeUnit unit) {
        return false;
    }

    @Nonnull
    @Override
    public <T> Future<T> submit(@Nonnull Callable<T> task) {
        try {
            Thread thread = new Thread(() -> {
                try {
                    task.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Nonnull
    @Override
    public <T> Future<T> submit(@Nonnull Runnable task, T result) {
        try {
            Thread thread = new Thread(task);
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(result);
    }

    @Nonnull
    @Override
    public Future<?> submit(@Nonnull Runnable task) {
        try {
            Thread thread = new Thread(task);
            thread.start();
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Nonnull
    @Override
    public <T> List<Future<T>>
    invokeAll(@Nonnull Collection<? extends Callable<T>> tasks) {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public <T> List<Future<T>> invokeAll(@Nonnull Collection<? extends Callable<T>> tasks,
                                         long timeout, @Nonnull TimeUnit unit) {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T invokeAny(@Nonnull Collection<? extends Callable<T>> tasks, long timeout,
                           @Nonnull TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void execute(Runnable command) {
        command.run();
    }
}
