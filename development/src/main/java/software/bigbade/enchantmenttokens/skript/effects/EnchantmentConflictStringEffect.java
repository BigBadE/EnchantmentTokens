/*
 * Copyright (c) 2020 BigBadE, All rights reserved
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
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Pattern;

@Name("EnchantmentTarget")
@Description({"Sets the enchantment target, two conflicting enchantments cannot both be added to the same item", "Ex: Infinity and Mending.", "The string should be the key of the enchantment", "it should be namespace:enchantname", "vanilla enchants have the namespace 'minecraft', Skript enchantments are 'skript'"})
@Examples({"on Skript start:",
        "\u0009set the conflict of Test to minecraft:myenchant"})
public class EnchantmentConflictStringEffect extends Effect {
    static {
        Skript.registerEffect(EnchantmentConflictEffect.class, "set [the] conflict of %customenchant% to %string%");
    }

    private Expression<SkriptEnchantment> enchantment;
    private Expression<String> conflict;

    private static final Pattern splitPattern = Pattern.compile(":");

    @Override
    protected void execute(@Nonnull Event event) {
        String conflictEnchant = conflict.getSingle(event);
        String[] split = splitPattern.split(conflictEnchant);
        SkriptEnchantment skriptEnchantment = enchantment.getSingle(event);
        assert skriptEnchantment != null;
        if (split.length != 2) {
            EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "Enchantment {0} has incorrect conflict {1}", new Object[]{skriptEnchantment.getEnchantmentName(), conflictEnchant});
        }
        skriptEnchantment.addConflict(split[0], split[1]);
    }

    @Nonnull
    @Override
    public String toString(Event event, boolean b) {
        return "Set conflict of " + Objects.requireNonNull(enchantment.getSingle(event)).getEnchantmentName() + " to " + conflict.getSingle(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean, @Nonnull SkriptParser.ParseResult parseResult) {
        enchantment = (Expression<SkriptEnchantment>) expressions[0];
        conflict = (Expression<String>) expressions[1];
        return true;
    }
}