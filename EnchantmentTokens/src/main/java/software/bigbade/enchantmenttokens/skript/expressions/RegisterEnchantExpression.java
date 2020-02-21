package software.bigbade.enchantmenttokens.skript.expressions;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import java.util.logging.Level;

@Name("Register enchantment")
@Description("Registers an enchantment with given name.")
@Examples({"on Skript start:",
        "	create a new custom enchant named \"Test\" with an icon of command block "})
public class RegisterEnchantExpression extends SimpleExpression<SkriptEnchantment> {
    private Expression<String> name;
    private Expression<ItemType> icon;

    static {
        Skript.registerExpression(RegisterEnchantExpression.class, SkriptEnchantment.class, ExpressionType.COMBINED, "[create] [a] [new] custom enchant[ment] named %string% with [an] icon [of] %itemtype%");
    }

    @Override
    public String toString(Event event, boolean b) {
        return "Registering enchantment " + name.getSingle(event);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) expressions[0];
        icon = (Expression<ItemType>) expressions[1];
        return true;
    }

    @Override
    protected SkriptEnchantment[] get(Event event) {
        String nameStr = name.getSingle(event);
        EnchantmentHandler enchantmentHandler = ((EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens")).getEnchantmentHandler();
        for(SkriptEnchantment enchantment : enchantmentHandler.getSkriptEnchantments()) {
            if(enchantment.getName().equals(nameStr)) {
                enchantment.setIcon(icon.getSingle(event).getMaterial());
                return new SkriptEnchantment[] { enchantment };
            }
        }

        EnchantLogger.log(Level.SEVERE, "Could not correctly create {0}, please report this along with any errors.", nameStr);
        return new SkriptEnchantment[] { null };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SkriptEnchantment> getReturnType() {
        return SkriptEnchantment.class;
    }
}
