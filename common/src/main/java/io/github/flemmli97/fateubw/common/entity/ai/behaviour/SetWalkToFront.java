package io.github.flemmli97.fateubw.common.entity.ai.behaviour;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;

public class SetWalkToFront<E extends PathfinderMob> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORIES = MemoryTest.builder(1)
            .hasMemory(MemoryModuleType.ATTACK_TARGET);

    protected BiFunction<E, LivingEntity, Float> distance = (owner, target) -> 5f;
    protected BiFunction<E, LivingEntity, Float> speedMod = (owner, target) -> 1f;

    public SetWalkToFront<E> distance(float distance) {
        return this.distance((owner, target) -> distance);
    }

    public SetWalkToFront<E> distance(BiFunction<E, LivingEntity, Float> distance) {
        this.distance = distance;
        return this;
    }

    public SetWalkToFront<E> speedMod(float speedModifier) {
        return this.speedMod((owner, target) -> speedModifier);
    }

    public SetWalkToFront<E> speedMod(BiFunction<E, LivingEntity, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORIES;
    }

    @Override
    protected void start(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        Vec3 look = target.getViewVector(1).scale(this.distance.apply(entity, target));
        Vec3 vec3 = target.position().add(look);
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(vec3, this.speedMod.apply(entity, target), 0));
    }
}
