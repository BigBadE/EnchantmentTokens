/*
 * Addons for the Custom Enchantment API in Minecraft
 * Copyright (C) 2020 BigBadE
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package software.bigbade.enchantmenttokens.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Name("EnchantmentConflict")
@Description("Sets the enchantment conflict, two conflicting enchantments cannot both be added to the same item. Ex: Infinity and Mending")
@Examples({"on Skript start:",
        "    set the conflict of Test to MyEnchant"})
public class EnchantmentConflictEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentConflictEffect.class, "Set [the] conflict of %customenchant% to %customenchant%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<SkriptEnchantment> conflict;

    @Override
    protected void execute(@Nonnull Event event) {
        SkriptEnchantment conflictEnchant = conflict.getSingle(event);
        enchantment.getSingle(event).addConflict(conflictEnchant);
    }

    @Nonnull
    @Override
    public String toString(@Nullable Event event, boolean b) {
        if(event == null) {
            return "Set conflict of enchantment";
        }
        return "Set conflict of " + enchantment.getSingle(event).getEnchantmentName() + " to " + conflict.getSingle(event).getEnchantmentName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean, @Nonnull SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        conflict = (Expression<SkriptEnchantment>) expressions[1];
        return true;
    }
}
