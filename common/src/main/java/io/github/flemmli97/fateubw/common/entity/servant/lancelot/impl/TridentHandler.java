package io.github.flemmli97.fateubw.common.entity.servant.lancelot.impl;

import io.github.flemmli97.fateubw.common.entity.servant.lancelot.LancelotUseHandler;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

public class TridentHandler implements LancelotUseHandler {

    @Override
    public void startUse(LivingEntity entity, LivingEntity target, InteractionHand hand) {
    }

    @Override
    public void use(LivingEntity entity, LivingEntity target, InteractionHand hand) {
        ThrownTrident tridententity = new ThrownTrident(entity.level(), entity, entity.getItemInHand(hand).copy());
        double d0 = target.getX() - entity.getX();
        double d1 = target.getY(0.33) - tridententity.getY();
        double d2 = target.getZ() - entity.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        tridententity.shoot(d0, d1 + d3 * 0.2, d2, 1.6f, 14 - entity.level().getDifficulty().getId() * 4);
        entity.playSound(SoundEvents.DROWNED_SHOOT, 1, 1 / (entity.getRandom().nextFloat() * 0.4f + 0.8f));
        entity.level().addFreshEntity(tridententity);
    }

    @Override
    public boolean matches(ItemStack item) {
        return item.getItem() instanceof TridentItem;
    }

    @Override
    public boolean isBetterThan(LivingEntity entity, ItemStack current, ItemStack stack) {
        return ItemUtils.isItemBetter(entity, null, stack, current);
    }
}
