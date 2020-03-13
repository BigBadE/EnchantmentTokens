package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class GenericTabCompleter implements TabCompleter, IEnchantTabCompleter {
    private int args;
    private String permission;

    public GenericTabCompleter(int args, @Nullable String permission) {
        this.args = args;
        this.permission = permission;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(permission != null && !commandSender.hasPermission(permission) && !commandSender.isOp())
            return Collections.singletonList(CommandUtils.NOPERMISSION);
        if(args.length > this.args)
            return Collections.singletonList(CommandUtils.TOOMANYARGUMENTS);
        return Collections.emptyList();
    }

    public String getPermission() { return permission; }
}
