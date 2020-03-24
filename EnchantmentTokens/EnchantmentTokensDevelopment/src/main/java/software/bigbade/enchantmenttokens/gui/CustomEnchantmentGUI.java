package software.bigbade.enchantmenttokens.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;

import java.util.HashMap;
import java.util.Map;

public class CustomEnchantmentGUI implements EnchantmentGUI {
    private EnchantmentPlayer opener;
    private ItemStack item;
    private Inventory inventory;
    private Map<Integer, EnchantButton> buttons = new HashMap<>();

    public CustomEnchantmentGUI(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public EnchantButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void addButton(EnchantButton button, int slot) {
        inventory.setItem(slot, button.getItem());
        buttons.put(slot, button);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public EnchantmentPlayer getOpener() {
        return opener;
    }

    public void setOpener(EnchantmentPlayer opener) {
        this.opener = opener;
    }
}
