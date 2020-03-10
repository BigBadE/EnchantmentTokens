package software.bigbade.enchantmenttokens.utils.enchants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationManager;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.ReflectionManager;
import software.bigbade.enchantmenttokens.utils.configuration.ConfigurationType;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class EnchantmentHandler {
    private List<EnchantmentBase> enchantments = new ArrayList<>();
    private List<VanillaEnchant> vanillaEnchants = new ArrayList<>();
    private List<SkriptEnchantment> skriptEnchantments = new ArrayList<>();
    private List<EnchantmentBase> allEnchants = new ArrayList<>();

    private FileConfiguration config;
    private FileConfiguration skriptConfiguration;
    private String skriptPath;

    public EnchantmentHandler(FileConfiguration config, String skriptPath) {
        this.config = config;
        this.skriptPath = skriptPath;
        skriptConfiguration = ConfigurationManager.loadConfigurationFile(new File(skriptPath));
    }

    public void registerEnchants(Collection<EnchantmentBase> enchantments) {
        List<Enchantment> vanillaRegistering = new ArrayList<>();
        ConfigurationSection section = ConfigurationManager.getSectionOrCreate(config, "enchants");

        for (String name : new ConfigurationType<>(Collections.singletonList("Fortune")).getValue("vanillaEnchants", section)) {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase().replace(" ", "_")));
            if (enchantment != null) vanillaRegistering.add(enchantment);
            else EnchantLogger.log(Level.SEVERE, "Could not find an enchantment by the name {0}", name);
        }

        vanillaRegistering.forEach(enchantment -> loadVanillaConfig(enchantment, section));

        skriptEnchantments.forEach(Enchantment::registerEnchantment);

        this.enchantments.addAll(enchantments);
        enchantments.forEach(Enchantment::registerEnchantment);

        allEnchants.addAll(enchantments);
        allEnchants.addAll(vanillaEnchants);

        ConfigurationManager.saveConfiguration(new File(skriptPath), skriptConfiguration);

        EnchantLogger.log(Level.INFO, "Registered enchantments");
    }

    private void loadVanillaConfig(Enchantment enchantment, ConfigurationSection section) {
        ConfigurationSection enchantSection = section.getConfigurationSection(enchantment.getKey().getKey());
        if (enchantSection == null)
            enchantSection = section.createSection(enchantment.getKey().getKey());
        String iconName = new ConfigurationType<>("Bedrock").getValue("icon", enchantSection);
        Material icon = Material.getMaterial(iconName.toUpperCase().replace(" ", "_"));

        if (icon == null) {
            enchantSection.set("icon", "Bedrock");
            icon = Material.BEDROCK;
        }

        if (new ConfigurationType<>(true).getValue("enabled", enchantSection)) {
            VanillaEnchant vanillaEnchant = new VanillaEnchant(icon, enchantment);
            vanillaEnchants.add(vanillaEnchant);
            for (Field field : EnchantmentBase.class.getDeclaredFields()) {
                ConfigurationManager.loadConfigForField(field, enchantSection, vanillaEnchant);
            }
            vanillaEnchant.loadConfig();
        }
    }

    @SuppressWarnings("unchecked")
    public void unregisterEnchants() {
        Field byKey = ReflectionManager.getField(Enchantment.class, "byKey");
        Field byName = ReflectionManager.getField(Enchantment.class, "byName");

        Map<NamespacedKey, Enchantment> byKeys = (HashMap<NamespacedKey, Enchantment>) ReflectionManager.getValue(byKey, null);
        assert byKeys != null;
        for (Enchantment enchantment : enchantments) {
            byKeys.remove(enchantment.getKey());
        }

        Map<NamespacedKey, String> byNames = (HashMap<NamespacedKey, String>) ReflectionManager.getValue(byName, null);
        assert byNames != null;
        for (Enchantment enchantment : enchantments) {
            byNames.remove(enchantment.getKey());
        }
    }

    public void addSkriptEnchant(SkriptEnchantment enchantment) {
        for (Field field : enchantment.getClass().getSuperclass().getDeclaredFields())
            ConfigurationManager.loadConfigForField(field, ConfigurationManager.getSectionOrCreate(skriptConfiguration, enchantment.getName()), enchantment);
        enchantment.loadConfig();
        skriptEnchantments.add(enchantment);
        allEnchants.add(enchantment);
    }

    public List<EnchantmentBase> getEnchantments() {
        return enchantments;
    }

    public List<SkriptEnchantment> getSkriptEnchantments() {
        return skriptEnchantments;
    }

    public List<EnchantmentBase> getAllEnchants() {
        return allEnchants;
    }
}
