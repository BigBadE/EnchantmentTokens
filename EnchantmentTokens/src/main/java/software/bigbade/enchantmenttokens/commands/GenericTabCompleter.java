package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;

import java.util.Collections;
import java.util.List;

public class GenericTabCompleter implements TabCompleter {
    private int args;
    private String permission;

    public GenericTabCompleter(int args, @Nullable String permission) {
        this.args = args;
        this.permission = permission;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(permission != null && !commandSender.hasPermission(permission) && !commandSender.isOp())
            return Collections.singletonList(TranslatedMessage.translate("command.permission"));
        if(args.length > this.args)
            return Collections.singletonList(TranslatedMessage.translate("command.arguments.toomany"));
        return Collections.emptyList();
    }
}
