package software.bigbade.enchantmenttokens.api.wrappers;

import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantmentConflictWrapper implements IConflictWrapper {
    private Map<String, List<String>> conflicts = new HashMap<>();

    @Override
    public boolean conflicts(Enchantment enchantment) {
        return false;
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Override
    public void addTarget(String addon, String name) {
        if (conflicts.containsKey(addon))
            conflicts.get(addon).add(name);
        else
            conflicts.put(addon, Arrays.asList(name));
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Override
    public void addTarget(Enchantment enchantment) {
        if (conflicts.containsKey(enchantment.getKey().getNamespace()))
            conflicts.get(enchantment.getKey().getNamespace()).add(enchantment.getKey().getKey());
        else
            conflicts.put(enchantment.getKey().getNamespace(), Arrays.asList(enchantment.getKey().getKey()));
    }
}
