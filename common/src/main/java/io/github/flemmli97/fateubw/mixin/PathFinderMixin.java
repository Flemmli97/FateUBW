package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.mixinhelper.PathFinderEx;
import io.github.flemmli97.fateubw.mixinhelper.PathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.Target;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(PathFinder.class)
public abstract class PathFinderMixin implements PathFinderEx {

    @Shadow
    @Final
    private NodeEvaluator nodeEvaluator;

    @Shadow
    @Nullable
    protected abstract Path findPath(ProfilerFiller profiler, Node node, Map<Target, BlockPos> targetPos, float maxRange, int accuracy, float searchDepthMultiplier);

    @Unique
    private List<BlockPos> fateubw$targetPositions;

    @ModifyVariable(method = "findPath(Lnet/minecraft/world/level/PathNavigationRegion;Lnet/minecraft/world/entity/Mob;Ljava/util/Set;FIF)Lnet/minecraft/world/level/pathfinder/Path;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/pathfinder/NodeEvaluator;done()V"))
    private Path after(Path path, PathNavigationRegion region, Mob mob, Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
        if (this.fateubw$targetPositions != null && !this.fateubw$targetPositions.isEmpty()) {
            Path newPath = PathUtils.chainPaths(path, (pos, node) -> {
                Map<Target, BlockPos> map = Map.of(this.nodeEvaluator.getGoal(pos.getX(), pos.getY(), pos.getZ()), pos);
                this.nodeEvaluator.prepare(region, mob);
                return this.findPath(region.getProfiler(), node, map, maxRange, accuracy, searchDepthMultiplier);
            }, this.fateubw$targetPositions);
            this.fateubw$targetPositions = null;
            return newPath;
        }
        return path;
    }

    @Override
    public void fateubw$setPathTargets(List<BlockPos> targetPositions) {
        this.fateubw$targetPositions = targetPositions;
    }
}
