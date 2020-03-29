package software.bigbade.enchantmenttokens.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.api.VanillaEnchant;
import software.bigbade.enchantmenttokens.utils.RomanNumeralConverter;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EnchantCmd implements CommandExecutor {
    private EnchantmentHandler handler;

    public EnchantCmd(EnchantmentHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (!sender.hasPermission("enchanttoken.admin") && !sender.isOp()) {
            sender.sendMessage(StringUtils.COMMAND_ERROR_PERMISSION);
            return true;
        }
        if (args.length != 2 || !(sender instanceof Player)) {
            sender.sendMessage(StringUtils.COMMAND_ENCHANT_USAGE);
            return true;
        }

        ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
        int level;

        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(StringUtils.COMMAND_ENCHANT_USAGE);
            return true;
        }

        for (EnchantmentBase enchantment : handler.getAllEnchants()) {
            if (enchantment instanceof VanillaEnchant)
                continue;
            if (enchantment.getKey().toString().equals(args[0])) {
                addEnchant((Player) sender, item, enchantment, level);
                return true;
            }
        }
        sender.sendMessage(StringUtils.COMMAND_ERROR_NO_ENCHANTMENT);
        return true;
    }

    private void addEnchant(Player player, ItemStack item, EnchantmentBase base, int level) {
        item.addEnchantment(base.getEnchantment(), level);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        List<String> lore = meta.getLore();
        if (lore == null)
            lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + base.getEnchantmentName() + ": " + RomanNumeralConverter.getRomanNumeral(level));
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.sendMessage(StringUtils.COMMAND_ADD.translate(base.getEnchantmentName()));
    }
}
