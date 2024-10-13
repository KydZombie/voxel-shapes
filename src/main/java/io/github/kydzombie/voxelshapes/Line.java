package io.github.kydzombie.voxelshapes;

import io.github.kydzombie.voxelshapes.impl.VoxelVec3d;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public record Line(VoxelVec3d start, VoxelVec3d end) {

    public Line(Vec3d start, Vec3d end) {
        this(VoxelVec3d.voxelify(start), VoxelVec3d.voxelify(end));
    }

    public Line expand(double d, VoxelVec3d center) {
        return new Line(movePointAwayFromCenter(start, center, d), movePointAwayFromCenter(end, center, d));
    }

    private static VoxelVec3d movePointAwayFromCenter(VoxelVec3d point, VoxelVec3d center, double distance) {
        VoxelVec3d direction = point.subtract(center).normalize();
        VoxelVec3d displacement = direction.multiply(distance);
        return point.add(displacement);
    }

    public boolean coincidesWith(Line other) {
        return (start.equals(other.start) && end.equals(other.end)) ||
                (start.equals(other.end) && start.equals(other.start));
    }

    public boolean partiallyCoincidesWith(Line other) {
        return containsPoint(other.start) && containsPoint(other.end) ||
                other.containsPoint(start) && other.containsPoint(end);
    }

    private boolean containsPoint(VoxelVec3d point) {
        double minX = Math.min(start.x(), end.x());
        double maxX = Math.max(start.x(), end.x());
        double minY = Math.min(start.y(), end.y());
        double maxY = Math.max(start.y(), end.y());
        double minZ = Math.min(start.z(), end.z());
        double maxZ = Math.max(start.z(), end.z());

        return (point.x() >= minX && point.x() <= maxX) &&
                (point.y() >= minY && point.y() <= maxY) &&
                (point.z() >= minZ && point.z() <= maxZ);
    }

    public Line[] getNonOverlappingParts(Line other) {
        List<Line> nonOverlappingParts = new ArrayList<>();

        if (this.containsPoint(other.start) && this.containsPoint(other.end)) {
            // Other line is completely inside this line, split this line into two parts
            if (!start.equals(other.start)) {
                nonOverlappingParts.add(new Line(start, other.start));
            }
            if (!end.equals(other.end)) {
                nonOverlappingParts.add(new Line(other.end, end));
            }
        } else if (this.containsPoint(other.start)) {
            // Other line overlaps at the start of this line
            nonOverlappingParts.add(new Line(start, other.start));
            nonOverlappingParts.add(new Line(other.start, end));
        } else if (this.containsPoint(other.end)) {
            // Other line overlaps at the end of this line
            nonOverlappingParts.add(new Line(start, other.end));
            nonOverlappingParts.add(new Line(other.end, end));
        } else {
            // This line is completely inside other line
            if (!other.start.equals(start)) {
                nonOverlappingParts.add(new Line(other.start, start));
            }
            if (!other.end.equals(end)) {
                nonOverlappingParts.add(new Line(end, other.end));
            }
        }

        return nonOverlappingParts.toArray(new Line[0]);
    }
}