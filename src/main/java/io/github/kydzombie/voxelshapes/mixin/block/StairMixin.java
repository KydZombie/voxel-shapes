package io.github.kydzombie.voxelshapes.mixin.block;

import io.github.kydzombie.voxelshapes.api.HasCollisionVoxelShape;
import io.github.kydzombie.voxelshapes.api.HasVoxelShape;
import io.github.kydzombie.voxelshapes.api.VoxelData;
import io.github.kydzombie.voxelshapes.api.VoxelShape;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StairsBlock.class)
public class StairMixin implements HasVoxelShape {
    @Unique
    VoxelData[] voxelDatum = new VoxelData[] {
            new VoxelData(new Box[]{
                    Box.create(0, 0, 0, 1, 0.5, 1),
                    Box.create(.5, .5, 0, 1, 1, 1)
            }).preCache(),
            new VoxelData(new Box[]{
                    Box.create(0, 0, 0, 1, 0.5, 1),
                    Box.create(0, .5, 0, .5, 1, 1)
            }).preCache(),
            new VoxelData(new Box[]{
                    Box.create(0, 0, 0, 1, 0.5, 1),
                    Box.create(0, .5, .5, 1, 1, 1)
            }).preCache(),
            new VoxelData(new Box[]{
                    Box.create(0, 0, 0, 1, 0.5, 1),
                    Box.create(0, .5, 0, 1, 1, .5)
            }).preCache()
    };


    @Override
    public @Nullable VoxelShape getVoxelShape(World world, int x, int y, int z) {
        int meta = world.getBlockMeta(x, y, z);
        if (meta < 0 || meta > 3) return null;
        return voxelDatum[meta].withOffset(x, y, z);
    }
}
