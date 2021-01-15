/*
 * Custom enchantments for Minecraft
 * Copyright (C) 2021 Big_Bad_E
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

package com.bigbade.enchantmenttokens.skript.events;

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
import com.bigbade.enchantmenttokens.EnchantmentTokens;
import com.bigbade.enchantmenttokens.api.CustomEnchantmentEvent;
import com.bigbade.enchantmenttokens.api.ListenerType;
import com.bigbade.enchantmenttokens.skript.SkriptEnchantment;
import com.bigbade.enchantmenttokens.utils.RegexPatterns;
import com.bigbade.enchantmenttokens.utils.listeners.ListenerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Name("Register an enchantment listener")
@Description("Listens for the given event on the given enchantment. See https://raw.githubusercontent.com/wiki/BigBadE/EnchantmentTokens/development/Events.md for events.")
@Examples({"on \"block break\" for Test:",
        "\u0009send \"Test\" to event-player"})
public class EnchantmentListenerEvent extends SelfRegisteringSkriptEvent {
    static {
        Skript.registerEvent("enchantmentevent", EnchantmentListenerEvent.class, CustomEnchantmentEvent.class, "%string% for %customenchant%");
        //noinspection rawtypes
        EventValues.registerEventValue(CustomEnchantmentEvent.class, Player.class,
                new Getter<Player, CustomEnchantmentEvent>() {
                    @Nonnull
                    @Override
                    public Player get(@Nonnull final CustomEnchantmentEvent e) {
                        return e.getUser();
                    }
                }, 0);
    }

    private Literal<String> type;
    private Literal<SkriptEnchantment> enchantmentExpression;

    private Trigger trigger;

    private EnchantmentTokens main;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(@Nonnull Literal<?>[] literals, int i, @Nonnull SkriptParser.ParseResult parseResult) {
        type = (Literal<String>) literals[0];
        enchantmentExpression = (Literal<SkriptEnchantment>) literals[1];
        main = (EnchantmentTokens) Bukkit.getPluginManager().getPlugin("EnchantmentTokens");
        ListenerType listenerType = ListenerType.valueOf(
                RegexPatterns.SPACE_PATTERN.matcher(type.getSingle().toUpperCase()).replaceAll("_"));
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
    public String toString(@Nullable Event event, boolean b) {
        if(event == null) {
            return "Event listener for an enchantment";
        }
        SkriptEnchantment enchantment = this.enchantmentExpression.getSingle(event);
        if(enchantment == null) {
            return type.getSingle(event) + " listener for an enchantment";
        }
        return type.getSingle(event) + " listener for " + enchantment.getEnchantmentName();
    }
}
