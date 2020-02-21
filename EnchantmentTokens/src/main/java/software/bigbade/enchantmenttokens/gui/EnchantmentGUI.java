package software.bigbade.enchantmenttokens.gui;

import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.utils.EnchantButton;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentGUI {
    private Player opener;
    private ItemStack item;
    private Inventory inventory;
    private Map<Integer, EnchantButton> buttons = new HashMap<>();

    public EnchantmentGUI(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public EnchantButton getButton(int slot) {
        return buttons.get(slot);
    }

    public void addButton(EnchantButton button, int slot) {
        buttons.put(slot, button);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Player getOpener() {
        return opener;
    }

    public void setOpener(Player opener) {
        this.opener = opener;
    }
}
