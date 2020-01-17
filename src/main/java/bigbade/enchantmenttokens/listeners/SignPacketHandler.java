package bigbade.enchantmenttokens.listeners;

/*
EnchantmentTokens
Copyright (C) 2019-2020 Big_Bad_E
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import bigbade.enchantmenttokens.EnchantmentTokens;
import bigbade.enchantmenttokens.api.EnchantmentBase;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignPacketHandler {
    private List<Location> signs = new ArrayList<>();

    public SignPacketHandler(ProtocolManager manager, EnchantmentTokens main, List<EnchantmentBase> enchantments) {
        manager.addPacketListener(new SignPacketLoadAdapter(main, signs, enchantments));
        manager.addPacketListener(new SignPacketUpdateAdapter(main, signs, enchantments));
    }

    public void removeSign(Location location) {
        signs.remove(location);
    }

    public List<Location> getSigns() {
        return signs;
    }
}

class SignPacketLoadAdapter extends PacketAdapter {
    private List<EnchantmentBase> enchantments;
    private List<Location> signs;

    public SignPacketLoadAdapter(EnchantmentTokens main, List<Location> signs, List<EnchantmentBase> enchantments) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK);
        this.enchantments = enchantments;
        this.signs = signs;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        List<NbtBase<?>> compounds = container.getListNbtModifier().read(0);
        for (int i = 0; i < compounds.size(); i++) {
            NbtCompound compound = (NbtCompound) compounds.get(i);
            if (compound.getString("id").equals("minecraft:sign")) {
                List<String> text = new ArrayList<>();
                for (int i2 = 1; i2 < 5; i2++) {
                    try {
                        text.add(compound.getString("Text" + i2).split("text\":\"")[1].split("\"}")[0]);
                    } catch (IndexOutOfBoundsException ignored) {
                    }
                }
                if (text.size() != 2) return;
                if (text.get(0).equals("[Enchantment]")) {
                    for (EnchantmentBase base1 : enchantments) {
                        if (base1.getName().equals(text.get(1))) {
                            signs.add(new Location(event.getPlayer().getWorld(), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
                            String price = "N/A";
                            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
                            if (base1.canEnchantItem(itemStack)) {
                                int level = base1.getStartLevel();
                                for (Map.Entry<Enchantment, Integer> enchats : itemStack.getEnchantments().entrySet())
                                    if (enchats.getKey().getKey().equals(base1.getKey())) {
                                        level = enchats.getValue();
                                        break;
                                    }
                                if (level <= base1.getMaxLevel())
                                    price = base1.getDefaultPrice(level) + "G";
                                else
                                    price = "Maxed!";
                            }
                            compound.put("Text3", "{\"extra\":[{\"text\":\"" + price + "\"}],\"text\":\"\"}");
                            compounds.set(i, compound);
                            break;
                        }
                    }
                }
            }
        }
        container.getListNbtModifier().write(0, compounds);
    }
}

class SignPacketUpdateAdapter extends PacketAdapter {
    private List<EnchantmentBase> enchantments;
    private List<Location> signs;

    public SignPacketUpdateAdapter(EnchantmentTokens main, List<Location> signs, List<EnchantmentBase> enchantments) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.TILE_ENTITY_DATA);
        this.enchantments = enchantments;
        this.signs = signs;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        if (container.getIntegers().getValues().get(0) == 9) {
            NbtCompound base = (NbtCompound) container.getNbtModifier().read(0);
            List<String> text = new ArrayList<>();
            for (int i = 1; i < 5; i++) {
                try {
                    text.add(base.getString("Text" + i).split("text\":\"")[1].split("\"}")[0]);
                } catch (IndexOutOfBoundsException ignored) {
                }
            }
            if (text.size() != 2) return;
            if (text.get(0).equals("[Enchantment]")) {
                for (EnchantmentBase base1 : enchantments) {
                    if (base1.getName().equals(text.get(1))) {
                        signs.add(new Location(event.getPlayer().getWorld(), base.getInteger("x"), base.getInteger("y"), base.getInteger("z")));
                        String price = "N/A";
                        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
                        if (base1.canEnchantItem(itemStack)) {
                            int level = base1.getStartLevel();
                            for (Map.Entry<Enchantment, Integer> enchants : itemStack.getEnchantments().entrySet())
                                if (enchants.getKey().getKey().equals(base1.getKey())) {
                                    level = enchants.getValue();
                                    break;
                                }
                            if (level <= base1.getMaxLevel())
                                price = base1.getDefaultPrice(level) + "G";
                            else
                                price = "Maxed!";
                        }
                        base.put("Text3", "{\"extra\":[{\"text\":\"Price: " + price + "\"}],\"text\":\"\"}");
                        container.getNbtModifier().write(0, base);
                        break;
                    }
                }
            }
        }
    }
}