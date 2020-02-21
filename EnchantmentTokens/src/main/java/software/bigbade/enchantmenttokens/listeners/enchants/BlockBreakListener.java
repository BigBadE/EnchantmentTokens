package software.bigbade.enchantmenttokens.listeners.enchants;

import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.listeners.SignPacketHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener extends BasicEnchantListener implements Listener {
    private SignPacketHandler handler;

    public BlockBreakListener(ListenerManager enchantListeners, SignPacketHandler handler) {
        super(enchantListeners);
        this.handler = handler;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        EnchantmentEvent enchantmentEvent = new EnchantmentEvent(ListenerType.BLOCK_BREAK, item).setTargetBlock(event.getBlock()).setUser(event.getPlayer());
        callListeners(enchantmentEvent);

        if(event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if(sign.getLine(0).equals("[Enchantment]")) {
                handler.removeSign(sign.getLocation());
            }
        }
    }
}