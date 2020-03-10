package net.minecraft.server.v1_15_R1;

import com.mojang.brigadier.CommandDispatcher;

public class MinecraftServer {
    public CommandDispatcher<?> getCommandDispatcher() {
        return new CommandDispatcher<>();
    }
}
