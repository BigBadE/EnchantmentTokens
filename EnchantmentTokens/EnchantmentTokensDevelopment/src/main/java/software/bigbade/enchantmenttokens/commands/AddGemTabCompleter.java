package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddGemTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 1) {
            List<Player> players = ((Player) commandSender).getWorld().getPlayers();
            List<String> names = new ArrayList<>();
            for(Player player : players)
                names.add(player.getDisplayName());
            return names;
        } else {
            return Collections.singletonList(TranslatedMessage.translate("command.arguments.toomany"));
        }
    }
}
