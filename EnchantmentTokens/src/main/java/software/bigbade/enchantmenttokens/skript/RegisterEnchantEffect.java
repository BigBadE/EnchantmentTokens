package software.bigbade.enchantmenttokens.skript;

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
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.EnchantmentTokens;

import java.util.Objects;

@Name("Register enchantment")
@Description("Registers an enchantment with given name.")
public class RegisterEnchantEffect extends Effect {
    private Expression<String> name;
    private Expression<ItemStack> icon;

    static {
        Skript.registerEffect(RegisterEnchantEffect.class, "[a] new customenchant named %string% with [an] icon [of] %material%");
    }

    @Override
    public String toString(Event event, boolean b) {
        return "Registering enchantment " + name.getSingle(event);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) expressions[0];
        icon = (Expression<ItemStack>) expressions[1];
        return true;
    }

    @Override
    protected void execute(Event event) {
        SkriptEnchantment base = new SkriptEnchantment(name.getSingle(event), icon.getSingle(event).getType());
        ((EnchantmentTokens) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Enchantmenttokens"))).getEnchantmentHandler().addSkriptEnchant(base);
    }
}
