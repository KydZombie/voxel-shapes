package io.github.kydzombie.voxelshapes.mixin;

import io.github.kydzombie.voxelshapes.api.HasVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow private World world;

    @Shadow protected abstract void method_1545(Box box);

    @Inject(method = "method_1554(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V", at = @At("HEAD"), cancellable = true)
    private void drawVoxelShapes(PlayerEntity playerEntity, HitResult hitResult, int i, ItemStack itemStack, float f, CallbackInfo ci) {
        if (i == 0 && hitResult.type == HitResultType.BLOCK && world.getBlockState(hitResult.blockX, hitResult.blockY, hitResult.blockZ).getBlock() instanceof HasVoxelShape block) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(3553);
            GL11.glDepthMask(false);
            float var6 = 0.002F;
            int var7 = world.getBlockId(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
            if (var7 > 0) {
                Block.BLOCKS[var7].updateBoundingBox(this.world, hitResult.blockX, hitResult.blockY, hitResult.blockZ);
                double var8 = playerEntity.field_1637 + (playerEntity.x - playerEntity.field_1637) * (double)f;
                double var10 = playerEntity.field_1638 + (playerEntity.y - playerEntity.field_1638) * (double)f;
                double var12 = playerEntity.field_1639 + (playerEntity.z - playerEntity.field_1639) * (double)f;
                for (Box box : block.getVoxelShape(world, hitResult.blockX, hitResult.blockY, hitResult.blockZ)) {
                    // TODO: Combine into non-overlapping shapes before rendering
                    method_1545(box.expand(var6, var6, var6).offset(-var8, -var10, -var12));
                }
            }

            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glDisable(3042);

            ci.cancel();
        }
    }
}
