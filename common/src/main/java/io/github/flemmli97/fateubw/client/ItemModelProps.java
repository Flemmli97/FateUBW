package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.servant.Emiya;
import io.github.flemmli97.fateubw.common.entity.servant.Medusa;
import io.github.flemmli97.fateubw.common.items.weapons.ArcherBowItem;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ItemModelProps {

    public static final ResourceLocation ACTIVE_ID = Fate.modRes("active");
    public static final ResourceLocation THROWN_DAGGER_ID = Fate.modRes("thrown");
    public static final ResourceLocation BOW_PULL_ID = Fate.modRes("pull");
    public static final ResourceLocation CALADBOLG_ID = Fate.modRes("caladbolg");
    public static final ResourceLocation UNSEALED_ID = Fate.modRes("unsealed");

    public static final ClampedItemPropertyFunction ACTIVE_ITEM_PROP = (stack, world, entity, seed) -> stack.has(FateDataComponents.GLOWING_ITEM.get()) ? 1 : 0;
    public static final ClampedItemPropertyFunction UNSEALED_PROP = (stack, world, entity, seed) -> stack.has(FateDataComponents.UNSEALED.get()) ? 1 : 0;

    public static final ClampedItemPropertyFunction THROWN_DAGGER_PROP = (stack, world, entity, seed) -> {
        boolean thrown = false;
        if (entity instanceof Player player) {
            ChainDagger dagger = Platform.INSTANCE.getPlayerData(player).getThrownDagger();
            if (dagger != null) {
                thrown = dagger.fromMainHand() ? player.getMainHandItem() == stack : player.getOffhandItem() == stack;
            }
        } else if (entity instanceof Medusa medusa) {
            thrown = medusa.daggerThrown();
            if (thrown) {
                if (medusa.getMainHandItem() == stack)
                    return 1;
                if (medusa.getMainHandItem().isEmpty() && medusa.getOffhandItem() == stack)
                    return 1;
            }
        }
        return thrown ? 1 : 0;
    };

    public static final ClampedItemPropertyFunction BOW_PULL_PROP = (stack, world, entity, seed) -> {
        if (entity == null) {
            return 0.0F;
        } else {
            return !entity.isUsingItem() || entity.getUseItem() != stack ? 0.0F : (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 10.0F;
        }
    };

    public static final ClampedItemPropertyFunction CALADBOLG_CHARGE = (stack, world, entity, seed) -> {
        if (entity == null) {
            return 0.0F;
        } else {
            if (stack.getItem() instanceof ArcherBowItem bow && bow.charged(stack))
                return 1;
            if (entity instanceof Emiya emiya && emiya.getAnimationHandler().isCurrent(Emiya.CALADBOLG))
                return 1;
            return 0;
        }
    };
}
