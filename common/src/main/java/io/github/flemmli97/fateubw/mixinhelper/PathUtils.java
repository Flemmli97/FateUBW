package io.github.flemmli97.fateubw.mixinhelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class PathUtils {

    public static Path chainPaths(Path path, BiFunction<BlockPos, Node, Path> pathFunc, List<BlockPos> targets) {
        List<Node> nodes = new ArrayList<>();
        BlockPos posTarget = path.getTarget();
        boolean reached = path.canReach();
        for (int i = 0; i < path.getNodeCount(); i++) {
            nodes.add(path.getNode(i));
        }
        for (BlockPos target : targets) {
            Node last = nodes.getLast();
            Path newPath = pathFunc.apply(target, last);
            if (newPath != null) {
                for (int i = 0; i < newPath.getNodeCount(); i++) {
                    Node node = newPath.getNode(i);
                    if (!nodes.contains(node)) {
                        nodes.add(newPath.getNode(i));
                    }
                }
                posTarget = newPath.getTarget();
                reached = newPath.canReach();
            }
        }
        return new Path(nodes, posTarget, reached);
    }
}
