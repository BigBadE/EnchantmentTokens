package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GemsTabCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length > 2) return Collections.singletonList(TranslatedMessage.translate("command.arguments.toomany"));
        if(args.length == 1) {
            //Return player names
            commandSender.getServer().getOnlinePlayers().forEach(player -> { if(player.getName().startsWith(args[0])) suggestions.add(player.getName());});
        } else {
            try {
                Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                return Collections.singletonList(TranslatedMessage.translate("command.add.notnumber"));
            }
        }
        return suggestions;
    }
}