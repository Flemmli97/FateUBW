package io.github.flemmli97.fateubw.common.entity.misc;

import io.github.flemmli97.fateubw.common.entity.utils.CustomArrowDamageSource;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import io.github.flemmli97.fateubw.common.registry.FateEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArcherArrow extends AbstractArrow implements CustomArrowDamageSource {

    public ArcherArrow(EntityType<? extends ArcherArrow> type, Level level) {
        super(type, level);
    }

    public ArcherArrow(Level level, LivingEntity shootingEntity, ItemStack weapon) {
        super(FateEntities.ARCHER_ARROW.get(), shootingEntity, level, ItemStack.EMPTY, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void doKnockback(LivingEntity entity, DamageSource damageSource) {
    }

    @Override
    public DamageSource getSource() {
        return FateDamageTypes.indirect(FateDamageTypes.ARCHER_NORMAL, this, this.getOwner() == null ? this : this.getOwner());
    }
}