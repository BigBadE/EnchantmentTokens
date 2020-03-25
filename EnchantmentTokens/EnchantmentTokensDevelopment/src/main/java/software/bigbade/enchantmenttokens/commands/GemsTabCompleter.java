package software.bigbade.enchantmenttokens.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bigbade.enchantmenttokens.api.StringUtils;

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
            return Collections.singletonList(ChatColor.stripColor(StringUtils.COMMAND_ERROR_PERMISSION));
        if (args.length > 2) return Collections.singletonList(ChatColor.stripColor(StringUtils.COMMAND_ERROR_TOO_MANY_ARGUMENTS));
        if(args.length == 1) {
            //Return player names
            List<String> players = new ArrayList<>();
            commandSender.getServer().getOnlinePlayers().forEach(player -> { if(player.getName().startsWith(args[0])) players.add(player.getName());});
            if(players.isEmpty())
                players.add(ChatColor.stripColor(StringUtils.COMMAND_ERROR_NO_PLAYER.translate(args[0])));
            return players;
        } else {
            return checkLong(args[1]);
        }
    }

    private List<String> checkLong(String number) {
        try {
            Long.parseLong(number);
            return Collections.emptyList();
        } catch (NumberFormatException e) {
            return Collections.singletonList(ChatColor.stripColor(StringUtils.COMMAND_ERROR_NOT_NUMBER.translate(number)));
        }
    }

    @Override
    public String getPermission() {
        return admin ? "enchanttoken.admin" : null;
    }
}