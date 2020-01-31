package bigbade.enchantmenttokens.utils;

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentPlayer;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
import org.bukkit.entity.Player;

public class CurrencyAdditionHandler {
    public static void addGems(Player player, long amount, EnchantmentTokens main) {
        addGems(main.getPlayerHandler().getPlayer(player), amount, main);
    }

    public static void addGems(EnchantmentPlayer player, long amount, EnchantmentTokens main) {
        player.addGems(amount);

        String priceStr;
        if (player.usingGems())
            priceStr = amount + "G";
        else
            priceStr = TranslatedMessage.translate("dollar.symbol", "" + amount);
        player.getPlayer().sendMessage(TranslatedMessage.translate("command.add", priceStr));
    }
}
