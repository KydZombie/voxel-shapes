package io.github.kydzombie.voxelshapes.mixin.block;

import io.github.kydzombie.voxelshapes.api.HasCollisionVoxelShape;
import io.github.kydzombie.voxelshapes.api.HasVoxelShape;
import io.github.kydzombie.voxelshapes.api.VoxelData;
import io.github.kydzombie.voxelshapes.api.VoxelShape;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FenceBlock.class)
abstract class FenceBlockMixin extends Block implements HasVoxelShape, HasCollisionVoxelShape {
    public FenceBlockMixin(int id, Material material) {
        super(id, material);
    }

    @Override
    public @Nullable VoxelShape getVoxelShape(World world, int x, int y, int z) {
        int fenceId = Block.FENCE.id;
        boolean posX = world.getBlockId(x + 1, y, z) == fenceId;
        boolean negX = world.getBlockId(x - 1, y, z) == fenceId;
        boolean posZ = world.getBlockId(x, y, z + 1) == fenceId;
        boolean negZ = world.getBlockId(x, y, z - 1) == fenceId;
        int ct = 1;
        ct += posZ ? 1 : 0;
        ct += negZ ? 1 : 0;
        Box[] boxes = new Box[ct];
        boxes[0] = Box.create((negX ? 0 : 0.375F), 0.F, 0.375F, (posX ? 1.F : 0.625F), 1.F, 0.625F);
        if (posZ) {
            boxes[1] = Box.create(0.375F, 0, 0.625F, 0.625F, 1, 1);
        }
        if (negZ) {
            int index = 1;
            if (posZ)
                index = 2;
            boxes[index] = Box.create(0.375F, 0, 0, 0.625F, 1, 0.375F);
        }
        return new VoxelData(boxes).withOffset(x, y, z);
    }

    public @Nullable VoxelShape getCollisionVoxelShape(World world, int x, int y, int z) {
        Box[] boxes = getVoxelShape(world, x, y, z).getBoxes();
        for (Box box : boxes) {
            box.maxY += .5;
        }
        return new VoxelData(boxes).withOffset(x, y, z);
    }
}
