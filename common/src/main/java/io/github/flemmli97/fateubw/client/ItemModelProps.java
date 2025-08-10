package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.servant.EntityEmiya;
import io.github.flemmli97.fateubw.common.entity.servant.EntityMedusa;
import io.github.flemmli97.fateubw.common.items.weapons.ArcherBowItem;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ItemModelProps {

    public static final ResourceLocation HELD_ID = Fate.modRes("held");
    public static final ResourceLocation ACTIVE_ID = Fate.modRes("active");
    public static final ResourceLocation THROWN_DAGGER_ID = Fate.modRes("thrown");
    public static final ResourceLocation BOW_PULL_ID = Fate.modRes("pull");
    public static final ResourceLocation CALADBOLG_ID = Fate.modRes("caladbolg");

    public static int HELD_TYPE;

    public static final ClampedItemPropertyFunction HELD_MAIN_PROP = (stack, world, entity, i) -> HELD_TYPE;

    public static final ClampedItemPropertyFunction ACTIVE_ITEM_PROP = (stack, world, entity, seed) -> entity != null && entity.getUseItem().getItem() == stack.getItem() ? 1 : 0;

    public static final ClampedItemPropertyFunction THROWN_DAGGER_PROP = (stack, world, entity, seed) -> {
        boolean thrown = false;
        if (entity instanceof Player player) {
            ChainDagger dagger = Platform.INSTANCE.getPlayerData(player).map(PlayerData::getThrownDagger).orElse(null);
            if (dagger != null) {
                thrown = dagger.fromMainHand() ? player.getMainHandItem() == stack : player.getOffhandItem() == stack;
            }
        } else if (entity instanceof EntityMedusa medusa) {
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
            if (entity instanceof EntityEmiya emiya && emiya.getAnimationHandler().isCurrent(EntityEmiya.CALADBOLG))
                return 1;
            return 0;
        }
    };
}
