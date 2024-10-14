package io.github.kydzombie.voxelshapes.mixin.client;

import io.github.kydzombie.voxelshapes.Line;
import io.github.kydzombie.voxelshapes.api.HasVoxelShape;
import io.github.kydzombie.voxelshapes.api.VoxelShape;
import io.github.kydzombie.voxelshapes.impl.VoxelVec3d;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private World world;

    @Inject(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"), cancellable = true)
    private void voxelshapes_drawBlockVoxelShapesOutline(PlayerEntity playerEntity, HitResult hitResult, int i, ItemStack itemStack, float f, CallbackInfo ci) {
        int id = world.getBlockId(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
        Block block = Block.BLOCKS[id];
        if (block instanceof HasVoxelShape hasVoxelShape) {
            if (id > 0) {
                block.updateBoundingBox(this.world, hitResult.blockX, hitResult.blockY, hitResult.blockZ);
                double interpolatedX = playerEntity.lastTickX + (playerEntity.x - playerEntity.lastTickX) * (double) f;
                double interpolatedY = playerEntity.lastTickY + (playerEntity.y - playerEntity.lastTickY) * (double) f;
                double interpolatedZ = playerEntity.lastTickZ + (playerEntity.z - playerEntity.lastTickZ) * (double) f;

                VoxelShape voxelShape = hasVoxelShape.getVoxelShape(world, hitResult.blockX, hitResult.blockY, hitResult.blockZ);
                if (voxelShape != null) {
                    List<Line> lines = voxelShape.getLines();
                    VoxelVec3d offset = voxelShape.getVoxelOffset();

                    Tessellator tessellator = Tessellator.INSTANCE;
                    tessellator.start(1);
                    for (Line line : lines) {
                        tessellator.vertex(
                                -interpolatedX + line.start().x() + offset.x(),
                                -interpolatedY + line.start().y() + offset.y(),
                                -interpolatedZ + line.start().z() + offset.z()
                        );
                        tessellator.vertex(
                                -interpolatedX + line.end().x() + offset.x(),
                                -interpolatedY + line.end().y() + offset.y(),
                                -interpolatedZ + line.end().z() + offset.z()
                        );
                    }
                    tessellator.draw();
                }
            }

            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glDisable(3042);

            ci.cancel();
        }
    }
}
