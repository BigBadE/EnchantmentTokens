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
import bigbade.enchantmenttokens.api.VanillaEnchant;
import bigbade.enchantmenttokens.localization.TranslatedMessage;
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
    private EnchantmentTokens main;
    private boolean gems;

    public SignPacketHandler(ProtocolManager manager, EnchantmentTokens main, boolean gems) {
        manager.addPacketListener(new SignPacketLoadAdapter(main, this));
        manager.addPacketListener(new SignPacketUpdateAdapter(main, this));
        this.main = main;
        this.gems = gems;
    }

    public void removeSign(Location location) {
        signs.remove(location);
    }

    public List<Location> getSigns() {
        return signs;
    }

    void handlePacket(NbtCompound compound, PacketEvent event) {
        List<String> text = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            try {
                text.add(compound.getString("Text" + i).split("text\":\"")[1].split("\"}")[0]);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        if (text.size() != 2)
            return;
        if (text.get(0).equals("[Enchantment]")) {
            for (EnchantmentBase base : main.getEnchantmentHandler().getEnchantments()) {
                updateSign(base, text, compound, event);
                return;
            }
            for(VanillaEnchant base : main.getEnchantmentHandler().getVanillaEnchants()) {
                updateSign(base, text, compound, event);
                return;
            }
        }
    }

    private void updateSign(EnchantmentBase base, List<String> text, NbtCompound compound, PacketEvent event) {
        if (base.getName().equals(text.get(1))) {
            signs.add(new Location(event.getPlayer().getWorld(), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
            String price = TranslatedMessage.translate("enchantment.notapplicable");
            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
            if (base.canEnchantItem(itemStack)) {
                int level = base.getStartLevel();
                for (Map.Entry<Enchantment, Integer> enchants : itemStack.getEnchantments().entrySet())
                    if (enchants.getKey().getKey().equals(base.getKey())) {
                        level = enchants.getValue();
                        break;
                    }
                if (level <= base.getMaxLevel())
                    if (gems)
                        price = base.getDefaultPrice(level) + "G";
                    else
                        price = TranslatedMessage.translate("dollar.symbol") + base.getDefaultPrice(level);
                else
                    price = TranslatedMessage.translate("enchantment.max");
            }
            compound.put("Text3", "{\"extra\":[{\"text\":\"Price: " + price + "\"}],\"text\":\"\"}");
        }
    }
}

class SignPacketLoadAdapter extends PacketAdapter {
    private SignPacketHandler handler;

    public SignPacketLoadAdapter(EnchantmentTokens main, SignPacketHandler handler) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK);
        this.handler = handler;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        List<NbtBase<?>> compounds = container.getListNbtModifier().read(0);
        for (int i = 0; i < compounds.size(); i++) {
            NbtCompound compound = (NbtCompound) compounds.get(i);
            handler.handlePacket(compound, event);
            compounds.set(i, compound);
        }
        container.getListNbtModifier().write(0, compounds);
    }
}

class SignPacketUpdateAdapter extends PacketAdapter {
    private SignPacketHandler handler;

    public SignPacketUpdateAdapter(EnchantmentTokens main, SignPacketHandler handler) {
        super(main, ListenerPriority.NORMAL, PacketType.Play.Server.TILE_ENTITY_DATA);
        this.handler = handler;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        if (container.getIntegers().getValues().get(0) == 9) {
            NbtCompound compound = (NbtCompound) container.getNbtModifier().read(0);
            handler.handlePacket(compound, event);
            container.getNbtModifier().write(0, compound);
        }
    }
}