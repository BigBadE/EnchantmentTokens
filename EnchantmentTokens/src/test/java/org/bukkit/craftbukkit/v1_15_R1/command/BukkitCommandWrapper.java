package org.bukkit.craftbukkit.v1_15_R1.command;

import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;

public class BukkitCommandWrapper {
    private final CraftServer server;
    private final Command command;

    public BukkitCommandWrapper(CraftServer server, Command command) {
        this.server = server;
        this.command = command;
    }
}
