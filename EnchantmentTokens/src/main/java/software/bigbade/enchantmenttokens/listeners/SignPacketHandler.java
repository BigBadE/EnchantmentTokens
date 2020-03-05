package software.bigbade.enchantmenttokens.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.EnchantmentTokens;
import software.bigbade.enchantmenttokens.api.EnchantmentBase;
import software.bigbade.enchantmenttokens.localization.TranslatedMessage;
import software.bigbade.enchantmenttokens.utils.EnchantLogger;
import software.bigbade.enchantmenttokens.utils.enchants.EnchantUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class SignPacketHandler {
    private Set<Location> signs = new HashSet<>();
    private EnchantmentTokens main;
    private boolean gems;

    public SignPacketHandler(ProtocolManager manager, EnchantmentTokens main, boolean gems) {
        EnchantLogger.log(Level.INFO, "Registering sign listener");
        manager.addPacketListener(new SignPacketLoadAdapter(main, this));
        this.main = main;
        this.gems = gems;
    }

    public void removeSign(Location location) {
        signs.remove(location);
    }

    public Set<Location> getSigns() {
        return signs;
    }

    void handlePacket(NbtCompound compound, PacketEvent event) {
        List<String> text = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            try {
                text.add(compound.getString("Text" + i).split("text\":\"")[1].split("\"}")[0]);
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ignored) {
                if (i != 3)
                    return;
                else
                    break;
            }
        }
        if (!text.get(0).equals("[Enchantment]")) return;
        main.getEnchantmentHandler().getAllEnchants().forEach(base -> {
            if (base.getName().equalsIgnoreCase(text.get(1))) {
                signs.add(new Location(event.getPlayer().getWorld(), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
                updateSign(base, compound, event);
            }
        });
    }

    private void updateSign(EnchantmentBase base, NbtCompound compound, PacketEvent event) {
        String price = TranslatedMessage.translate("enchantment.notapplicable");
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (base.canEnchantItem(itemStack)) {
            int level = EnchantUtils.getNextLevel(itemStack, base);
            price = EnchantUtils.getPriceString(gems, level, base);
        }
        compound.put("Text3", "{\"extra\":[{\"text\":\"Price: " + price + "\"}],\"text\":\"\"}");
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