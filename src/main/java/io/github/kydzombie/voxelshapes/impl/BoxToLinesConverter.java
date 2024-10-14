package io.github.kydzombie.voxelshapes.impl;

import io.github.kydzombie.voxelshapes.api.Line;
import io.github.kydzombie.voxelshapes.api.VoxelBox;
import io.github.kydzombie.voxelshapes.api.VoxelVec3d;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class BoxToLinesConverter {
    public static List<Line> convertBoxesToLines(Box[] boxes, Vec3d center) {
        return convertBoxesToLines(VoxelBox.voxelify(boxes), VoxelVec3d.voxelify(center));
    }

    public static List<Line> convertBoxesToLines(VoxelBox[] boxes, VoxelVec3d center) {
        List<Line> lines = new ArrayList<>();

        // TODO: Make lines not go out from a center, and instead out from each box.
        for (VoxelBox box : boxes) {
            VoxelVec3d corner1 = new VoxelVec3d(box.minX(), box.minY(), box.minZ());
            VoxelVec3d corner2 = new VoxelVec3d(box.minX(), box.minY(), box.maxZ());
            VoxelVec3d corner3 = new VoxelVec3d(box.minX(), box.maxY(), box.minZ());
            VoxelVec3d corner4 = new VoxelVec3d(box.minX(), box.maxY(), box.maxZ());
            VoxelVec3d corner5 = new VoxelVec3d(box.maxX(), box.minY(), box.minZ());
            VoxelVec3d corner6 = new VoxelVec3d(box.maxX(), box.minY(), box.maxZ());
            VoxelVec3d corner7 = new VoxelVec3d(box.maxX(), box.maxY(), box.minZ());
            VoxelVec3d corner8 = new VoxelVec3d(box.maxX(), box.maxY(), box.maxZ());

            lines.add(new Line(corner1, corner2));
            lines.add(new Line(corner1, corner3));
            lines.add(new Line(corner1, corner5));
            lines.add(new Line(corner2, corner4));
            lines.add(new Line(corner2, corner6));
            lines.add(new Line(corner3, corner4));
            lines.add(new Line(corner3, corner7));
            lines.add(new Line(corner4, corner8));
            lines.add(new Line(corner5, corner6));
            lines.add(new Line(corner5, corner7));
            lines.add(new Line(corner6, corner8));
            lines.add(new Line(corner7, corner8));
        }

        // Remove coinciding edges from lines
        removeCoincidingEdges(lines);

        // Expand lines

        return lines.stream().map(line -> line.expand(0.002, center)).toList();
    }

    private static void removeCoincidingEdges(List<Line> lines) {
        Set<Line> toRemove = new HashSet<>();
        Set<Line> toAdd = new HashSet<>();

        for (int i = 0; i < lines.size(); i++) {
            Line line1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                Line line2 = lines.get(j);
                if (line1.coincidesWith(line2)) {
                    toRemove.add(line1);
                    toRemove.add(line2);
                } else if (line1.partiallyCoincidesWith(line2)) {
                    Line[] nonOverlappingParts = line1.getNonOverlappingParts(line2);
                    toRemove.add(line1);
                    toRemove.add(line2);
                    Collections.addAll(toAdd, nonOverlappingParts);
                }
            }
        }

        lines.removeAll(toRemove);
        lines.addAll(toAdd);
    }
}

