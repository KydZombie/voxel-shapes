package io.github.kydzombie.voxelshapes.mixin.server;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

    @Redirect(method = "onPlayerMove", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;method_832(DDDFF)V"))
    void playerMoveFix(ServerPlayNetworkHandler instance, double e, double f, double g, float h, float v) {
        // todo: reimplement taking new collisions into consideration
    }
}
