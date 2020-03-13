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
import software.bigbade.enchantmenttokens.localization.TranslatedTextMessage;
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

    public SignPacketHandler(ProtocolManager manager, EnchantmentTokens main) {
        EnchantLogger.log(Level.INFO, "Registering sign listener");
        manager.addPacketListener(new SignPacketLoadAdapter(main, this));
        manager.addPacketListener(new SignPacketUpdateAdapter(main, this));
        this.main = main;
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
        main.getEnchantmentHandler().getAllEnchants().stream()
                .filter(base -> base.getName().equals(text.get(1)))
                .forEach(base -> {
                    if (event.getPacketType() == PacketType.Play.Server.MAP_CHUNK)
                        signs.add(new Location(event.getPlayer().getWorld(), compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z")));
                    compound.put("Text3", "{\"extra\":[{\"text\":\"Price: " + getPrice(base, event) + "\"}],\"text\":\"\"}");
                });
    }

    private static final String NOTAPPLICABLE = new TranslatedTextMessage("enchantment.notapplicable").getText();

    private String getPrice(EnchantmentBase base, PacketEvent event) {
        String price = NOTAPPLICABLE;
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (base.canEnchantItem(itemStack)) {
            int level = EnchantUtils.getNextLevel(itemStack, base);
            price = EnchantUtils.getPriceString(level, base);
        }
        return price;
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