package io.github.kydzombie.voxelshapes.mixin.client;

import io.github.kydzombie.voxelshapes.Line;
import io.github.kydzombie.voxelshapes.api.HasVoxelShape;
import io.github.kydzombie.voxelshapes.api.VoxelShape;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.kydzombie.voxelshapes.BoxToLinesConverter.convertBoxesToLines;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow private World world;

    @Inject(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockId(III)I"), cancellable = true)
    private void voxelshapes_drawBlockVoxelShapesOutline(PlayerEntity playerEntity, HitResult hitResult, int i, ItemStack itemStack, float f, CallbackInfo ci) {
        BlockState blockState = world.getBlockState(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
        if (blockState.getBlock() instanceof HasVoxelShape block) {
            int id = world.getBlockId(hitResult.blockX, hitResult.blockY, hitResult.blockZ);
            if (id > 0) {
                Block.BLOCKS[id].updateBoundingBox(this.world, hitResult.blockX, hitResult.blockY, hitResult.blockZ);
                double interpolatedX = playerEntity.lastTickX + (playerEntity.x - playerEntity.lastTickX) * (double)f;
                double interpolatedY = playerEntity.lastTickY + (playerEntity.y - playerEntity.lastTickY) * (double)f;
                double interpolatedZ = playerEntity.lastTickZ + (playerEntity.z - playerEntity.lastTickZ) * (double)f;

                VoxelShape voxelShape = block.getVoxelShape(world, hitResult.blockX, hitResult.blockY, hitResult.blockZ);
                if (voxelShape != null) {
                    Line[] lines = voxelShape.getLines();
                    Vec3d offset = voxelShape.getOffset();

                    Tessellator tessellator = Tessellator.INSTANCE;
                    tessellator.start(1);
                    for (Line line : lines) {
                        tessellator.vertex(-interpolatedX + line.points[0].x + offset.x, -interpolatedY + line.points[0].y + offset.y, -interpolatedZ + line.points[0].z + offset.z);
                        tessellator.vertex(-interpolatedX + line.points[1].x + offset.x, -interpolatedY + line.points[1].y + offset.y, -interpolatedZ + line.points[1].z + offset.z);
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
