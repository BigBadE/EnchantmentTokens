package software.bigbade.enchantmenttokens.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.events.bukkit.ScheduledEvent;
import ch.njol.skript.events.bukkit.ScheduledNoWorldEvent;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;

import java.util.Objects;

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
        return enchantEvent.getType() == type;
    }

    @Override
    public String toString(Event event, boolean b) {
        return null;
    }
}
