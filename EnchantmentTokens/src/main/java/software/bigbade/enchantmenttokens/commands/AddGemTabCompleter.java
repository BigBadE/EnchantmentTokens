package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddGemTabCompleter implements TabCompleter {
    @Override
    @NotNull
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) {
            List<Player> players = ((Player) commandSender).getWorld().getPlayers();
            List<String> names = new ArrayList<>();
            for(Player player : players)
                names.add(player.getDisplayName());
            return names;
        } else {
            return Collections.singletonList(CommandUtils.TOOMANYARGUMENTS);
        }
    }
}
