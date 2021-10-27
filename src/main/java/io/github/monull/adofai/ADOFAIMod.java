package io.github.monull.adofai;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ADOFAIMod implements ModInitializer {

    static Path workingDir;

    @Override
    public void onInitialize() {
        workingDir = FabricLoader.getInstance().getConfigDir().resolve("adofai");
        if (!Files.exists(workingDir)) {
            try {
                Files.createDirectory(workingDir);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        ADOFAICommand.setupCommands();
        ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
            ADOFAICommand.server = minecraftServer;
        });
    }
}
