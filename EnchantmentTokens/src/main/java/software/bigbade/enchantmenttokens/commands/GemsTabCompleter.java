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
        if (args.length > 3) return Collections.singletonList(TranslatedMessage.translate("command.arguments.toomany"));
        return suggestions;
    }
}