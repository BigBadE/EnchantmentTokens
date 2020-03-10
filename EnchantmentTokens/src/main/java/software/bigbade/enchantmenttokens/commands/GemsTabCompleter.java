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

public class GemsTabCompleter implements TabCompleter, IEnchantTabCompleter {
    boolean admin;

    public GemsTabCompleter(boolean admin) {
        this.admin = admin;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(admin && !commandSender.hasPermission("enchanttoken.admin") && !commandSender.isOp())
            return Collections.singletonList(TranslatedMessage.translate("command.permission"));
        if (args.length > 2) return Collections.singletonList(TranslatedMessage.translate("command.arguments.toomany"));
        if(args.length == 1) {
            //Return player names
            List<String> players = new ArrayList<>();
            commandSender.getServer().getOnlinePlayers().forEach(player -> { if(player.getName().startsWith(args[0])) players.add(player.getName());});
            if(players.isEmpty())
                players.add(TranslatedMessage.translate("command.arguments.noplayer", args[0]));
            return players;
        } else {
            try {
                Long.parseLong(args[1]);
                return Collections.emptyList();
            } catch (NumberFormatException e) {
                return Collections.singletonList(TranslatedMessage.translate("command.add.notnumber"));
            }
        }
    }

    @Override
    public String getPermission() {
        return admin ? "enchanttoken.admin" : null;
    }
}