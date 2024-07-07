package io.github.kydzombie.voxelshapes.api;

import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public interface HasVoxelShape {
    Box[] getVoxelShape(World world, int x, int y, int z);
}
