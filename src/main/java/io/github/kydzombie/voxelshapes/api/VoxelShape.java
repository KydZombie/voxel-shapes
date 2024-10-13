package io.github.kydzombie.voxelshapes.api;

import io.github.kydzombie.voxelshapes.Line;
import io.github.kydzombie.voxelshapes.impl.VoxelBox;
import io.github.kydzombie.voxelshapes.impl.VoxelVec3d;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class VoxelShape {
    private final VoxelData voxelData;
    private final VoxelVec3d offset;

    protected VoxelShape(VoxelData voxelData, VoxelVec3d offset) {
        this.voxelData = voxelData;
        this.offset = offset;
    }

    public List<Box> getOffsetBoxes() {
        return voxelData.getBoxes().stream().map((box) -> box.offset(offset.x(), offset.y(), offset.z())).toList();
    }

    public List<VoxelBox> getVoxelBoxes() {
        return voxelData.getVoxelBoxes();
    }

    public List<Box> getBoxes() {
        return voxelData.getBoxes();
    }

    public List<Line> getLines() {
        return voxelData.getLines();
    }

    public VoxelVec3d getVoxelOffset() {
        return offset;
    }

    public Vec3d getOffset() {
        return VoxelVec3d.devoxelify(offset);
    }
}
