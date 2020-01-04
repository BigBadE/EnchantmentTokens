package bigbade.enchantmenttokens.api;

import bigbade.enchantmenttokens.utils.MaterialGroupUtils;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.Inventory;

public class SubInventory {
    private Inventory inventory;
    private EnchantmentTarget type;

    public SubInventory(Inventory inventory, EnchantmentTarget type) {
        this.inventory = inventory;
        this.type = type;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public EnchantmentTarget getType() {
        return type;
    }
}
