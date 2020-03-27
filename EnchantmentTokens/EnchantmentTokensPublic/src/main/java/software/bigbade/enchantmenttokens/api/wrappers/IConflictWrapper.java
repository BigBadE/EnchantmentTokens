package software.bigbade.enchantmenttokens.api.wrappers;

import org.bukkit.enchantments.Enchantment;

public interface IConflictWrapper {
    boolean conflicts(Enchantment enchantment);

    void addTarget(String addon, String name);

    void addTarget(Enchantment enchantment);
}
