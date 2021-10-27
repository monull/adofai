package io.github.monull.adofai;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ADOFAICommand {
    public static MinecraftServer server;

    private static int ticks = 20;

    public static int getTicks() {
        return ticks;
    }
    public static void setupCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            dispatcher.register(literal("adofai").then(literal("create").executes((context -> {
                PlayerEntity player = context.getSource().getPlayer();
                Vec3d pos = player.getPos();
                ArmorStandEntity entity = new ArmorStandEntity(context.getSource().getWorld(), pos.x, pos.y, pos.z);
                entity.setNoGravity(true);
                entity.setInvisible(true);
                entity.equipStack(EquipmentSlot.HEAD, Items.OAK_LOG.getDefaultStack());
                player.getEntityWorld().spawnEntity(entity);
                player.teleport(pos.x, pos.y + 3.0, pos.z);
                return 0;
            }))));

            dispatcher.register(literal("adofai").then(literal("load").then(argument("file", new FileArgumentType()).executes(context -> {
                new ADOFAIMap().load(context.getArgument("file", File.class));
                return 0;
            }))));

            dispatcher.register(literal("adofai").then(literal("tickrate").then(argument("tick", IntegerArgumentType.integer(1, 1000)).executes((context -> {
                ticks = 1000 / context.getArgument("tick", Integer.class);
                return 0;
            })))));
        }));
    }
}

class FileArgumentType implements ArgumentType<File> {
    @Override
    public File parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readString();
        File folder = new File(ADOFAIMod.workingDir.toUri());
        File file =  new File(folder, name);
        if (file.getName().endsWith(".adofai")) {
            return file;
        } else {
            return null;
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        File folder = new File(ADOFAIMod.workingDir.toUri());
        if (folder.exists()) {
            Arrays.stream(Objects.requireNonNull(folder.listFiles())).forEach(file -> {
                String name = file.getName();
                if (name.startsWith(builder.getRemainingLowerCase()) && name.endsWith(".adofai")) {
                    builder.suggest(name);
                }
            });
        } else {
            builder.suggest(folder.getPath());
        }
        return builder.buildFuture();
    }
}
