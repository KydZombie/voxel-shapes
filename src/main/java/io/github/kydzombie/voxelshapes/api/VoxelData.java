package io.github.kydzombie.voxelshapes.api;

import io.github.kydzombie.voxelshapes.BoxToLinesConverter;
import io.github.kydzombie.voxelshapes.Line;
import net.minecraft.util.math.Box;
import net.modificationstation.stationapi.api.util.math.Vec3d;

public class VoxelData {
    private final Box[] boxes;
    private final Vec3d center;
    private Line[] lines = null;

    public VoxelData(Box[] boxes) {
        this(boxes, new Vec3d(0.5, 0.5, 0.5));
    }

    public VoxelData(Box[] boxes, Vec3d center) {
        this.boxes = boxes;
        this.center = center;
    }

    public VoxelData preCache() {
        computeLines();
        return this;
    }

    public VoxelShape withOffset(Vec3d offset) {
        return new VoxelShape(this, offset);
    }

    public VoxelShape withOffset(int x, int y, int z) {
        return withOffset(new Vec3d(x, y, z));
    }

    public Box[] getBoxes() {
        return boxes;
    }

    protected void computeLines() {
        lines = BoxToLinesConverter.convertBoxesToLines(boxes, center).toArray(Line[]::new);
    }

    public Line[] getLines() {
        if (lines == null) {
            computeLines();
        }
        return lines;
    }
}
