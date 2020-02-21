package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.Inventory;

public class SubInventory extends EnchantmentGUI {
    private Inventory inventory;

    public SubInventory(Inventory inventory) {
        super(inventory);
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
