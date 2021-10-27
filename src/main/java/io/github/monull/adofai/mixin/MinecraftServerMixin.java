package io.github.monull.adofai.mixin;

import io.github.monull.adofai.ADOFAICommand;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyConstant(method = "runServer", constant = @Constant(longValue = 50L))
    long modifyTickTime(long tickTime) {
        return ADOFAICommand.getTicks();
    }


}
