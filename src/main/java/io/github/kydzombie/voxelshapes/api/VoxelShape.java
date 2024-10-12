package io.github.kydzombie.voxelshapes.api;

import io.github.kydzombie.voxelshapes.Line;
import net.minecraft.util.math.Box;
import net.modificationstation.stationapi.api.util.math.Vec3d;

import java.util.Arrays;

public class VoxelShape {
    private final VoxelData voxelData;
    private final Vec3d offset;

    public VoxelShape(VoxelData voxelData, Vec3d offset) {
        this.voxelData = voxelData;
        this.offset = offset;
    }

    public Box[] getOffsetBoxes() {
        return Arrays.stream(voxelData.getBoxes()).map((box) -> box.offset(offset.x, offset.y, offset.z)).toArray(Box[]::new);
    }

    public Box[] getBoxes() {
        return voxelData.getBoxes();
    }

    public Line[] getLines() {
        return voxelData.getLines();
    }

    public Vec3d getOffset() {
        return offset;
    }
}
