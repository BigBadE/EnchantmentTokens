/*
 * Copyright (c) 2020 BigBadE, All rights reserved
 */

package software.bigbade.enchantmenttokens.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SelfRegisteringSkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.Trigger;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.CustomEnchantmentEvent;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

import javax.annotation.Nonnull;
import java.util.Objects;

@Name("Register an enchantment listener")
@Description("Listens for the given event on the given enchantment. See https://raw.githubusercontent.com/wiki/BigBadE/EnchantmentTokens/development/Events.md for events.")
@Examples({"on \"block break\" for Test:",
        "	send \"Test\" to event-player"})
public class EnchantmentListenerEvent extends SelfRegisteringSkriptEvent {
    static {
        Skript.registerEvent("enchantmentevent", EnchantmentListenerEvent.class, CustomEnchantmentEvent.class, "%string% for %customenchant%");
        EventValues.registerEventValue(CustomEnchantmentEvent.class, Player.class, new Getter<Player, CustomEnchantmentEvent>() {
            @Nonnull
            @Override
            public Player get(@Nonnull final CustomEnchantmentEvent e) {
<<<<<<< HEAD:EnchantmentTokens/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/skript/events/EnchantmentListenerEvent.java
                return e.getUser();
=======
                return (Player) e.getUser();
>>>>>>> 3d705af96ebb617ac55d44878c2077b5e14535b9:EnchantmentTokensMain/EnchantmentTokensDevelopment/src/main/java/software/bigbade/enchantmenttokens/skript/events/EnchantmentListenerEvent.java
            }
        }, 0);
    }

    private Literal<String> type;
    private Literal<SkriptEnchantment> enchantmentExpression;

    private Trigger trigger;

    private EnchantmentTokens main;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] literals, int i, @Nonnull SkriptParser.ParseResult parseResult) {
        type = (Literal<String>) literals[0];
        enchantmentExpression = (Literal<SkriptEnchantment>) literals[1];
        main = (EnchantmentTokens) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("EnchantmentTokens"));
        ListenerType listenerType = ListenerType.valueOf(type.getSingle().replace(" ", "_").toUpperCase());
        main.getListenerHandler().getListenerManager(listenerType).add(event ->
                trigger.execute(event.getEvent()), enchantmentExpression.getSingle());
        return true;
    }

    @Override
    public void register(@Nonnull Trigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public void unregister(@Nonnull Trigger trigger) {
        this.trigger = null;
        for (ListenerType listenerType : ListenerType.values()) {
            ListenerManager<?> manager = main.getListenerHandler().getListenerManager(listenerType);
            manager.getListeners().keySet().removeIf(base -> base instanceof SkriptEnchantment);
        }
    }

    @Override
    public void unregisterAll() {
        //No idea what this does
    }

    @Nonnull
    @Override
    @Nonnull
    public String toString(Event event, boolean b) {
        SkriptEnchantment enchantment = this.enchantmentExpression.getSingle(event);
        Objects.requireNonNull(enchantment);
        return type.getSingle(event) + " listener for " + enchantment.getEnchantmentName();
    }
}
