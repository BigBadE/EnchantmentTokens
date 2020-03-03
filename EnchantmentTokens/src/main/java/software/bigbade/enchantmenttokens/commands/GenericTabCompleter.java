package software.bigbade.enchantmenttokens.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericTabCompleter implements TabCompleter {
    private int args;

    public GenericTabCompleter(int args) {
        this.args = args;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length > this.args)
            return Collections.singletonList("Too many arguments!");
        return new ArrayList<>();
    }
}
