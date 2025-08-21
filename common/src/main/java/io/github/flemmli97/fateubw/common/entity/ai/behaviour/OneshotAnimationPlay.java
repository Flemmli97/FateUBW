package io.github.flemmli97.fateubw.common.entity.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class OneshotAnimationPlay<E extends PathfinderMob & AnimatedEntity> extends ExtendedBehaviour<E> {

    private final String animation;
    private boolean keepRunning;

    public OneshotAnimationPlay(String animation) {
        this.animation = animation;
    }

    public OneshotAnimationPlay<E> keepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return !entity.getAnimationHandler().hasAnimation();
    }

    @Override
    protected void start(E entity) {
        entity.getAnimationHandler().setAnimation(this.animation);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.keepRunning;
    }
}
