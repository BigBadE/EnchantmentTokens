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

import java.util.logging.Level;

@Name("Register enchantment")
@Description("Registers an enchantment with given name.")
@Examples({"on Skript start:",
        "	create a new custom enchant named \"Test\" with an icon of command block "})
public class RegisterEnchantExpression extends SimpleExpression<SkriptEnchantment> {
    private Expression<String> name;
    private Expression<ItemType> icon;

    private EnchantmentTokens main;

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
        main = (EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens");
        return true;
    }

    @Override
    protected SkriptEnchantment[] get(Event event) {
        String nameStr = name.getSingle(event);
        EnchantmentHandler enchantmentHandler = main.getEnchantmentHandler();
        for(EnchantmentBase enchantment : enchantmentHandler.getSkriptEnchant()) {
            if(enchantment.getKey().getKey().equals(nameStr)) {
                enchantment.setIcon(icon.getSingle(event).getMaterial());
                return new SkriptEnchantment[] { (SkriptEnchantment) enchantment };
            }
        }

        EnchantmentTokens.getEnchantLogger().log(Level.SEVERE, "{0} does not have a tool set.", nameStr);
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
