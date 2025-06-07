package io.github.flemmli97.fateubw.mixinhelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Path;

import java.util.List;

public interface PathNavigationEx {

    /**
     * Creates a path that contains the given positions along it in order.
     */
    Path fateubw$createPathFor(List<BlockPos> targets, int accuracy);

}
