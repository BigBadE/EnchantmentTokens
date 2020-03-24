package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.gui.CustomEnchantmentGUI;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

public class EnchantMenuCmd implements CommandExecutor {
    private MenuFactory factory;

    public EnchantMenuCmd(MenuFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            CustomEnchantmentGUI inv = factory.genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(TranslatedMessage.translate("command.enchant.held"));
        }
        return true;
    }
}
