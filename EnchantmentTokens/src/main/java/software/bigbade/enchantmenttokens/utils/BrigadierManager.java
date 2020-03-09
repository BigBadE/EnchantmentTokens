package software.bigbade.enchantmenttokens.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class BrigadierManager {
    // obc.CraftServer#console field
    private static final Field CONSOLE_FIELD;

    // nms.MinecraftServer#getCommandDispatcher method
    private static final Method GET_COMMAND_DISPATCHER_METHOD;

    // nms.CommandListenerWrapper#getBukkitSender method
    private static final Method GET_BUKKIT_SENDER_METHOD;

    // nms.CommandDispatcher#getDispatcher (obfuscated) method
    private static final Method GET_BRIGADIER_DISPATCHER_METHOD;

    // obc.command.BukkitCommandWrapper constructor
    private static final Constructor<?> COMMAND_WRAPPER_CONSTRUCTOR;

    // ArgumentCommandNode#customSuggestions field
    private static final Field CUSTOM_SUGGESTIONS_FIELD;

    static {
        try {
            Class<?> craftServer = NMSManager.obcClass("CraftServer");
            CONSOLE_FIELD = craftServer.getDeclaredField("console");
            CONSOLE_FIELD.setAccessible(true);

            Class<?> minecraftServer = NMSManager.nmsClass("MinecraftServer");
            GET_COMMAND_DISPATCHER_METHOD = minecraftServer.getDeclaredMethod("getCommandDispatcher");
            GET_COMMAND_DISPATCHER_METHOD.setAccessible(true);

            Class<?> commandListenerWrapper = NMSManager.nmsClass("CommandListenerWrapper");
            GET_BUKKIT_SENDER_METHOD = commandListenerWrapper.getDeclaredMethod("getBukkitSender");
            GET_BUKKIT_SENDER_METHOD.setAccessible(true);

            Class<?> commandDispatcher = NMSManager.nmsClass("CommandDispatcher");
            GET_BRIGADIER_DISPATCHER_METHOD = Arrays.stream(commandDispatcher.getDeclaredMethods())
                    .filter(method -> method.getParameterCount() == 0)
                    .filter(method -> CommandDispatcher.class.isAssignableFrom(method.getReturnType()))
                    .findFirst().orElseThrow(NoSuchMethodException::new);
            GET_BRIGADIER_DISPATCHER_METHOD.setAccessible(true);

            Class<?> commandWrapperClass = NMSManager.obcClass("command.BukkitCommandWrapper");
            COMMAND_WRAPPER_CONSTRUCTOR = commandWrapperClass.getConstructor(craftServer, Command.class);

            CUSTOM_SUGGESTIONS_FIELD = ArgumentCommandNode.class.getDeclaredField("customSuggestions");
            CUSTOM_SUGGESTIONS_FIELD.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private CommandDispatcher<?> getDispatcher() {
        try {
            Object mcServerObject = CONSOLE_FIELD.get(Bukkit.getServer());
            Object commandDispatcherObject = GET_COMMAND_DISPATCHER_METHOD.invoke(mcServerObject);
            return (CommandDispatcher<?>) GET_BRIGADIER_DISPATCHER_METHOD.invoke(commandDispatcherObject);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void register(LiteralCommandNode<?> node) {
        Objects.requireNonNull(node, "node");

        CommandDispatcher dispatcher = getDispatcher();
        dispatcher.getRoot().addChild(node);
    }

    public void register() {
        register(LiteralArgumentBuilder.literal("ce").then(LiteralArgumentBuilder.literal("test")).build());
    }
}
