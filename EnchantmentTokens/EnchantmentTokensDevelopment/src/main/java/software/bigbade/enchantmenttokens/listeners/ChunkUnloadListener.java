package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Set;

public class ChunkUnloadListener implements Listener {
    private Set<Location> signs;

    public ChunkUnloadListener(Set<Location> signs) {
        this.signs = signs;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        signs.removeIf(location -> ((int) location.getX()) << 4 == event.getChunk().getX() && ((int) location.getZ()) << 4 == event.getChunk().getZ());
    }
}