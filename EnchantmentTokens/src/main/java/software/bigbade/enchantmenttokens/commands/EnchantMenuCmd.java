package software.bigbade.enchantmenttokens.commands;

import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.gui.EnchantmentMenuFactory;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchantMenuCmd implements CommandExecutor {
    private EnchantmentMenuFactory factory;

    public EnchantMenuCmd(EnchantmentMenuFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            EnchantmentGUI inv = factory.genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(TranslatedMessage.translate("command.enchant.held"));
        }
        return true;
    }
}
