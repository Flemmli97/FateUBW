package io.github.flemmli97.fateubw.common.entity.utils;

import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public interface TargetableOpponent {

    Predicate<LivingEntity> validTargetPredicate();
}
