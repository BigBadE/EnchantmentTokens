package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.gui.MenuFactory;

import javax.annotation.Nonnull;

public class EnchantMenuCmd implements CommandExecutor {
    private MenuFactory factory;

    public EnchantMenuCmd(MenuFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (commandSender instanceof Player) {
            EnchantmentGUI inv = factory.genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(StringUtils.COMMAND_ENCHANT_HELD);
        }
        return true;
    }
}
