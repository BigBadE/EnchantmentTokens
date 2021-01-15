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

package com.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import com.bigbade.enchantmenttokens.api.ArmorType;
import com.bigbade.enchantmenttokens.api.EventFactory;
import com.bigbade.enchantmenttokens.events.ArmorInteractEvent;
import com.bigbade.enchantmenttokens.events.EnchantmentEvent;
import com.bigbade.enchantmenttokens.utils.listeners.ListenerManager;

public class ArmorEquipListener extends BasicEnchantListener<ArmorInteractEvent> implements Listener {
    private final ListenerManager<ArmorInteractEvent> oldArmorListeners;
    private final ListenerManager<ArmorInteractEvent> newArmorListeners;

    public ArmorEquipListener(ListenerManager<ArmorInteractEvent> oldArmorListeners, ListenerManager<ArmorInteractEvent> newArmorListeners) {
        super(null);
        this.oldArmorListeners = oldArmorListeners;
        this.newArmorListeners = newArmorListeners;
    }

    private static boolean isAirOrNull(ItemStack item) {
        return item == null || item.getType().equals(Material.AIR);
    }

    private static boolean isInvalidClick(InventoryClickEvent event) {
        return event.getAction() == InventoryAction.NOTHING || !(event.getWhoClicked() instanceof Player)
                || (event.getSlotType() != InventoryType.SlotType.ARMOR
                && event.getSlotType() != InventoryType.SlotType.QUICKBAR
                && event.getSlotType() != InventoryType.SlotType.CONTAINER)
                || (event.getClickedInventory() != null
                && !event.getClickedInventory().getType().equals(InventoryType.PLAYER))
                || (!event.getInventory().getType().equals(InventoryType.CRAFTING)
                && !event.getInventory().getType().equals(InventoryType.PLAYER));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public final void onInventoryClick(final InventoryClickEvent event) {
        boolean shift = event.getClick().equals(ClickType.SHIFT_LEFT) && event.getClick().equals(ClickType.SHIFT_RIGHT);
        if (isInvalidClick(event)) {
            return;
        }
        ArmorType newArmorType = ArmorType.matchType(shift ? event.getCurrentItem() : event.getCursor());
        if (shift) {
            if (newArmorType == null) {
                return;
            }
            onShiftClick(newArmorType, event);
        } else {
            onNormalClick(newArmorType, event);
        }
    }

    private void onNormalClick(ArmorType armorType, InventoryClickEvent event) {
        if (armorType != null && event.getRawSlot() != armorType.getSlot()) {
            return;
        }
        ItemStack newArmorPiece = event.getCursor();
        ItemStack oldArmorPiece = event.getCurrentItem();
        if (event.getClick().equals(ClickType.NUMBER_KEY) && event.getClickedInventory() != null) {
            if (event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                ItemStack hotbarItem = event.getClickedInventory().getItem(event.getHotbarButton());
                if (!isAirOrNull(hotbarItem)) { // Equipping
                    armorType = ArmorType.matchType(hotbarItem);
                    newArmorPiece = hotbarItem;
                    oldArmorPiece = event.getClickedInventory().getItem(event.getSlot());
                } else { // Unequipping
                    armorType = ArmorType.matchType(!isAirOrNull(event.getCurrentItem()) ? event.getCurrentItem() : event.getCursor());
                }
            }
        } else if (isAirOrNull(event.getCursor()) && !isAirOrNull(event.getCurrentItem())) {// unequip with no new item going into the slot.
            armorType = ArmorType.matchType(event.getCurrentItem());
        }
        if (armorType == null || event.getRawSlot() != armorType.getSlot()) {
            return;
        }
        ArmorInteractEvent.EquipMethod method = ArmorInteractEvent.EquipMethod.PICK_DROP;
        if (event.getAction().equals(InventoryAction.HOTBAR_SWAP) || event.getClick().equals(ClickType.NUMBER_KEY)) {
            method = ArmorInteractEvent.EquipMethod.HOTBAR_SWAP;
        }
        ArmorInteractEvent armorEquipEvent = new ArmorInteractEvent((Player) event.getWhoClicked(), method, armorType, oldArmorPiece, newArmorPiece);
        Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
        if (armorEquipEvent.isCancelled()) {
            event.setCancelled(true);
        } else {
            onArmorEquip(armorEquipEvent);
        }
    }

    private void onShiftClick(ArmorType type, InventoryClickEvent event) {
        boolean equipping = true;
        if (event.getRawSlot() == type.getSlot()) {
            equipping = false;
        }
        Player player = (Player) event.getWhoClicked();
        if (equipping == isAirOrNull(player.getInventory().getArmorContents()[type.ordinal()])) {
            ArmorInteractEvent armorEquipEvent = new ArmorInteractEvent(player, ArmorInteractEvent.EquipMethod.SHIFT_CLICK, type, equipping ? null : event.getCurrentItem(), equipping ? event.getCurrentItem() : null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                event.setCancelled(true);
            } else {
                onArmorEquip(armorEquipEvent);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.useItemInHand().equals(Event.Result.DENY) || event.getAction() == Action.PHYSICAL) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ArmorType newArmorType = ArmorType.matchType(event.getItem());
            if (newArmorType != null && isAirOrNull(player.getInventory().getArmorContents()[newArmorType.ordinal()])) {
                ArmorInteractEvent armorEquipEvent = new ArmorInteractEvent(event.getPlayer(), ArmorInteractEvent.EquipMethod.HOTBAR, ArmorType.matchType(event.getItem()), null, event.getItem());
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if (armorEquipEvent.isCancelled()) {
                    event.setCancelled(true);
                    player.updateInventory();
                } else {
                    onArmorEquip(armorEquipEvent);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        ArmorType type = ArmorType.matchType(event.getOldCursor());
        if (event.getRawSlots().isEmpty()) {
            return;
        }
        if (type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)) {
            ArmorInteractEvent armorEquipEvent = new ArmorInteractEvent((Player) event.getWhoClicked(), ArmorInteractEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            } else {
                onArmorEquip(armorEquipEvent);
            }
        }
    }

    @EventHandler
    public void itemBreakEvent(PlayerItemBreakEvent event) {
        ArmorType type = ArmorType.matchType(event.getBrokenItem());
        if (type != null) {
            Player player = event.getPlayer();
            ArmorInteractEvent armorEquipEvent = new ArmorInteractEvent(player, ArmorInteractEvent.EquipMethod.BROKE, type, event.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                ItemStack itemStack = event.getBrokenItem().clone();
                itemStack.setAmount(1);
                Damageable damageable = (Damageable) itemStack.getItemMeta();
                assert damageable != null;
                damageable.setDamage((short) (damageable.getDamage() - 1));
                ItemStack[] armor = player.getInventory().getArmorContents();
                armor[type.ordinal()] = itemStack;
                player.getInventory().setArmorContents(armor);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        Boolean keepInventory = event.getEntity().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY);
        if (((LivingEntity) event.getEntity()).getHealth() - event.getFinalDamage() > 0 || keepInventory == null || keepInventory) {
            return;
        }
        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (isAirOrNull(itemStack)) {
                continue;
            }
            ArmorInteractEvent armorInteractEvent = new ArmorInteractEvent(player, ArmorInteractEvent.EquipMethod.DEATH, ArmorType.matchType(itemStack), itemStack, null);
            Bukkit.getServer().getPluginManager().callEvent(armorInteractEvent);
            if (armorInteractEvent.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void dispenseArmorEvent(BlockDispenseArmorEvent event) {
        ArmorType type = ArmorType.matchType(event.getItem());
        if (type != null && event.getTargetEntity() instanceof Player) {
            Player p = (Player) event.getTargetEntity();
            ArmorInteractEvent armorEquipEvent = new ArmorInteractEvent(p, ArmorInteractEvent.EquipMethod.DISPENSER, type, null, event.getItem());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if (armorEquipEvent.isCancelled()) {
                event.setCancelled(true);
            } else {
                onArmorEquip(armorEquipEvent);
            }
        }
    }

    public void onArmorEquip(ArmorInteractEvent event) {
        ItemStack item = event.getOldArmorPiece();
        if (item != null) {
            EnchantmentEvent<ArmorInteractEvent> enchantmentEvent = EventFactory.createCancellableEvent(event, item, event.getPlayer());
            callListeners(enchantmentEvent, oldArmorListeners);
        }
        item = event.getNewArmorPiece();
        if (item != null) {
            EnchantmentEvent<ArmorInteractEvent> enchantmentEvent = EventFactory.createCancellableEvent(event, item, event.getPlayer());
            callListeners(enchantmentEvent, newArmorListeners);
        }
    }
}
