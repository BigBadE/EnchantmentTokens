package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import software.bigbade.enchantmenttokens.gui.EnchantmentGUI;
import software.bigbade.enchantmenttokens.gui.MenuFactory;
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;

public class EnchantMenuCmd implements CommandExecutor {
    private MenuFactory factory;

    public EnchantMenuCmd(MenuFactory factory) {
        this.factory = factory;
    }

    private static final String HELD = new TranslatedTextMessage("command.enchant.held").getText();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            EnchantmentGUI inv = factory.genInventory((Player) commandSender);
            if (inv == null)
                commandSender.sendMessage(HELD);
        }
        return true;
    }
}
