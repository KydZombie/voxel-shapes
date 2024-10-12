package io.github.kydzombie.voxelshapes.api;

import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface HasVoxelShape {
    @Nullable VoxelShape getVoxelShape(World world, int x, int y, int z);
}
