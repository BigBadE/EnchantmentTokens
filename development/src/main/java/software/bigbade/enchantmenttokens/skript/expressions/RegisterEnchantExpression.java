/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.skript.expressions;

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
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantmentHandler;

import javax.annotation.Nonnull;
import java.util.Objects;

@Name("Register enchantment")
@Description("Registers an enchantment with given name.")
@Examples({"on Skript start:",
        "\u0009create a new custom enchant named \"Test\" with an icon of command block "})
public class RegisterEnchantExpression extends SimpleExpression<SkriptEnchantment> {
    private Expression<String> name;
    private Expression<ItemType> iconExpression;

    private EnchantmentTokens main;

    static {
        Skript.registerExpression(RegisterEnchantExpression.class, SkriptEnchantment.class, ExpressionType.COMBINED, "[create] [a] [new] custom enchant[ment] named %string% with [an] icon [of] %itemtype%");
    }

    @Override
    @Nonnull
    public String toString(Event event, boolean b) {
        return "Registering enchantment " + name.getSingle(event);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(@Nonnull Expression<?>[] expressions, int i, @Nonnull Kleenean kleenean, @Nonnull SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) expressions[0];
        iconExpression = (Expression<ItemType>) expressions[1];
        main = (EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens");
        return true;
    }

    @Override
    protected SkriptEnchantment[] get(@Nonnull Event event) {
        String nameStr = name.getSingle(event);
        ItemType icon = this.iconExpression.getSingle(event);
        Objects.requireNonNull(icon);
        EnchantmentHandler enchantmentHandler = main.getEnchantmentHandler();
        for(EnchantmentBase enchantment : enchantmentHandler.getSkriptEnchant()) {
            if(enchantment.getKey().getKey().equals(nameStr)) {
                enchantment.setIcon(icon.getMaterial());
                return new SkriptEnchantment[] { (SkriptEnchantment) enchantment };
            }
        }
        return new SkriptEnchantment[] { null };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    @Nonnull
    public Class<? extends SkriptEnchantment> getReturnType() {
        return SkriptEnchantment.class;
    }
}
