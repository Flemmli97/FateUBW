package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedusa;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ItemModelProps {

    public static final ClampedItemPropertyFunction ACTIVE_ITEM_PROP = (stack, world, entity, seed) -> entity != null && entity.getUseItem().getItem() == stack.getItem() ? 1 : 0;

    public static final ClampedItemPropertyFunction THROWN_DAGGER_PROP = (stack, world, entity, seed) -> {
        Entity thrown = null;
        if (entity instanceof Player player) {
            thrown = Platform.INSTANCE.getPlayerData(player).map(PlayerData::getThrownDagger).orElse(null);
        } else if (entity instanceof EntityMedusa medusa) {
            return medusa.daggerThrown() ? 1 : 0;
        }
        if (thrown == null)
            return 0;
        boolean off = entity.getOffhandItem() == stack;
        boolean main = !off && entity.getMainHandItem() == stack;
        return (main || off) ? 1 : 0;
    };

    public static final ClampedItemPropertyFunction BOW_PULL_PROP = (stack, world, entity, seed) -> {
        if (entity == null) {
            return 0.0F;
        } else {
            return !entity.isUsingItem() || entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 10.0F;
        }
    };
}
