package net.minecraft.server.v1_15_R1;

public class CommandDispatcher {
    private com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> root = new com.mojang.brigadier.CommandDispatcher<>();

    public com.mojang.brigadier.CommandDispatcher commandDispather() {
        return root;
    }
}
