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

public class EnchantmentListenerEvent extends SelfRegisteringSkriptEvent {
    private Trigger trigger;

    static {
        Skript.registerEvent("enchantmentevent", EnchantmentListenerEvent.class, EnchantmentEvent.class, "on %string% for %customenchant%");
        EventValues.registerEventValue(EnchantmentEvent.class, Player.class, new Getter<Player, EnchantmentEvent>() {
            @Override
            @Nullable
            public Player get(final EnchantmentEvent e) {
                return (Player) e.getUser();
            }
        }, 0);
    }

    @Override
    public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
        ListenerType type = ListenerType.valueOf(((Literal<String>) literals[0]).getSingle().toUpperCase().replace(" ", "_"));
        SkriptEnchantment enchantment = ((Literal<SkriptEnchantment>) literals[1]).getSingle();
        ((EnchantmentTokens) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("EnchantmentTokens"))).getListenerHandler().addListener(type, enchantment, (event) -> trigger.execute(event));
        return true;
    }

    @Override
    public void register(Trigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public void unregister(Trigger trigger) {
        this.trigger = null;
    }

    @Override
    public void unregisterAll() { }

    @Override
    public String toString(Event event, boolean b) {
        return null;
    }
}
