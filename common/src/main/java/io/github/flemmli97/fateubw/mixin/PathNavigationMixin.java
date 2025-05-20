package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.mixinhelper.PathFinderEx;
import io.github.flemmli97.fateubw.mixinhelper.PathNavigationEx;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PathNavigation.class)
public abstract class PathNavigationMixin implements PathNavigationEx {

    @Shadow
    @Final
    private PathFinder pathFinder;

    @Shadow
    @Nullable
    public abstract Path createPath(BlockPos pos, int accuracy);

    @Override
    public Path createPathFor(List<BlockPos> targets, int accuracy) {
        if (targets.isEmpty())
            return null;
        BlockPos pos = targets.get(0);
        targets.remove(0);
        ((PathFinderEx) this.pathFinder).fateubw$setPathTargets(targets);
        Path path = this.createPath(pos, accuracy);
        ((PathFinderEx) this.pathFinder).fateubw$setPathTargets(null);
        return path;
    }
}
