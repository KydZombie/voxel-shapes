package io.github.kydzombie.voxelshapes.mixin;

import io.github.kydzombie.voxelshapes.api.HasVoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FenceBlock.class)
abstract class FenceBlockMixin extends Block implements HasVoxelShape {
    public FenceBlockMixin(int id, Material material) {
        super(id, material);
    }

    @Override
    public Box[] getVoxelShape(World world, int x, int y, int z) {
        return new Box[] {
                Box.createCached(x, y, z, x + .5, y + .5, z + .5),
                Box.createCached(x + .5, y + .5, z + .5, x + 1, y + 1, z + 1)
        };
    }
}
