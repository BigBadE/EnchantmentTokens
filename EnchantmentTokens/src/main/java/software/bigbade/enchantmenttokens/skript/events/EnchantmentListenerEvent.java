package software.bigbade.enchantmenttokens.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

@Name("Register an enchantment listener")
@Description("Listens for the given event on the given enchantment. See https://raw.githubusercontent.com/wiki/BigBadE/EnchantmentTokens/development/Events.md for events.")
@Examples({"on \"block break\" for Test:",
        "	send \"Test\" to event-player"})
public class EnchantmentListenerEvent extends SkriptEvent {
    static {
        Skript.registerEvent("enchantmentevent", EnchantmentListenerEvent.class, EnchantmentEvent.class, "%string% for %customenchant%");
        EventValues.registerEventValue(EnchantmentEvent.class, Player.class, new Getter<Player, EnchantmentEvent>() {
            @Override
            @Nullable
            public Player get(final EnchantmentEvent e) {
                return (Player) e.getUser();
            }
        }, 0);
        EventValues.registerEventValue(EnchantmentEvent.class, SkriptEnchantment.class, new Getter<SkriptEnchantment, EnchantmentEvent>() {
            @Override
            @Nullable
            public SkriptEnchantment get(final EnchantmentEvent e) {
                return (SkriptEnchantment) e.getEnchantment();
            }
        }, 0);
    }

    private Literal<String> type;
    private Literal<SkriptEnchantment> enchantment;

    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        type = (Literal<String>) literals[0];
        enchantment = (Literal<SkriptEnchantment>) literals[1];
        return true;
    }

    @Override
    public boolean check(Event event) {
        ListenerType type = ListenerType.valueOf(this.type.getSingle(event).toUpperCase().replace(" ", "_"));
        EnchantmentEvent enchantEvent = (EnchantmentEvent) event;
        return enchantEvent.getType() == type && enchantEvent.getEnchantment().equals(enchantment.getSingle(event));
    }

    @Override
    public String toString(Event event, boolean b) {
        return type.getSingle(event) + " listener for " + enchantment.getSingle(event).getName();
    }
}
