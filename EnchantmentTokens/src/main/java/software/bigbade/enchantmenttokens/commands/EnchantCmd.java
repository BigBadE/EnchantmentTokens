package software.bigbade.enchantmenttokens.commands;

import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.listeners.gui.EnchantmentGUIListener;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class EnchantCmd implements CommandExecutor {
    private EnchantmentHandler handler;
    private EnchantUtils utils;

    public EnchantCmd(EnchantmentHandler handler, EnchantUtils utils) {
        this.handler = handler;
        this.utils = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(TranslatedMessage.translate("command.permission"));
            return true;
        }
        if (args.length >= 1 && sender instanceof Player) {
            StringBuilder nameBuilder = new StringBuilder(args[0]);
            for (int i = 1; i < args.length - 1; i++) {
                nameBuilder.append(args[i]);
            }
            String name = nameBuilder.toString();
            ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            utils.addEnchantment(item, name, (Player) sender, false);
            for (EnchantmentBase enchantment : handler.getAllEnchants()) {
                if(enchantment instanceof VanillaEnchant)
                    continue;
                if (enchantment.getKey().getKey().equals(name.toLowerCase().replace("_", ""))) {
                    addEnchant((Player) sender, item, enchantment, Integer.parseInt(args[args.length - 1]));
                    return true;
                }
            }
            sender.sendMessage(TranslatedMessage.translate("command.enchant.notfound"));
        } else
            sender.sendMessage(TranslatedMessage.translate("command.enchant.usage"));
        return true;
    }

    private void addEnchant(Player player, ItemStack item, EnchantmentBase base, int level) {
        if(base instanceof VanillaEnchant) {
            item.addEnchantment(((VanillaEnchant) base).getEnchantment(), level);
        } else {
            item.addEnchantment(base, level);
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            if (meta.getLore() == null)
                meta.setLore(new ArrayList<>());
            meta.getLore().add(base.getName() + ": " + EnchantmentGUIListener.getRomanNumeral(level));
        }
        player.sendMessage(TranslatedMessage.translate("command.add", base.getName()));
    }
}
