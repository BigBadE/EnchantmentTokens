package software.bigbade.enchantmenttokens.listeners.enchants;

import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import software.bigbade.enchantmenttokens.api.EnchantmentPlayer;
import software.bigbade.enchantmenttokens.api.EventFactory;
import software.bigbade.enchantmenttokens.api.ListenerType;
import software.bigbade.enchantmenttokens.api.StringUtils;
import software.bigbade.enchantmenttokens.configuration.ConfigurationType;
import software.bigbade.enchantmenttokens.events.EnchantmentEvent;
import software.bigbade.enchantmenttokens.utils.SignHandler;
import software.bigbade.enchantmenttokens.utils.listeners.ListenerManager;
import software.bigbade.enchantmenttokens.utils.players.PlayerHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBreakListener extends BasicEnchantListener implements Listener {
    private SignHandler handler;
    private ConfigurationSection section;
    private Random random = new Random();
    private PlayerHandler playerHandler;

    public BlockBreakListener(ListenerManager enchantListeners, SignHandler handler, @Nullable ConfigurationSection section, @Nullable PlayerHandler playerHandler) {
        super(enchantListeners);
        this.handler = handler;
        this.section = section;
        this.playerHandler = playerHandler;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        EnchantmentEvent enchantmentEvent = EventFactory.createEvent(ListenerType.BLOCK_BREAK, item).setTargetBlock(event.getBlock()).setUser(event.getPlayer());
        callListeners(enchantmentEvent);

        if (event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).equals("[Enchantment]")) {
                handler.removeSign(sign.getLocation());
            }
        }

        if (section != null && random.nextDouble() <= new ConfigurationType<>(0.05).getValue("chance", section)) {
            EnchantmentPlayer player = playerHandler.getPlayer(event.getPlayer());
            long gems = (long) getGems(player);
            player.addGems(gems);
            event.getPlayer().sendMessage(StringUtils.GEMS_FIND.translate("" + gems));
        }
    }

    private double getGems(EnchantmentPlayer player) {
        return (Math.abs(random.nextGaussian())*new ConfigurationType<>(1).getValue("doubler", section)+new ConfigurationType<>(1).getValue("minimum", section))*player.getDoubler();
    }
}