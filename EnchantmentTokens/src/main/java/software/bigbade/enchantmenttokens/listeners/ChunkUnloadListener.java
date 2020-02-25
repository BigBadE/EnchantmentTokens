package software.bigbade.enchantmenttokens.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChunkUnloadListener implements Listener {
    private Set<Location> signs;

    public ChunkUnloadListener(Set<Location> signs) {
        this.signs = signs;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Iterator<Location> iterator = signs.iterator();
        while(iterator.hasNext()) {
            Location location = iterator.next();
            if(Math.floor(location.getX()/16f) == event.getChunk().getX()) {
                if(Math.floor(location.getZ()/16) == event.getChunk().getZ()) {
                    signs.remove(location);
                }
            }
        }
    }
}