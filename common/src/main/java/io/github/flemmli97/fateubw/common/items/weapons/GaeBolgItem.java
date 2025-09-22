package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.utils.HitResultUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

public class GaeBolgItem extends TieredItem {

    public GaeBolgItem(Item.Properties props) {
        super(ItemTiers.GAE_BOLG, props);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        if (CommonConfig.gaeBolgMana > 0)
            tooltipComponents.add(Component.translatable("fateubw.tooltip.item.mana", CommonConfig.gaeBolgMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).useMana(CommonConfig.gaeBolgMana)) {
                GaeBolg gaeBolg = new GaeBolg(level, player);
                gaeBolg.shoot(player, player.getXRot(), player.getYRot(), 0, 2, 0);
                EntityHitResult res = HitResultUtils.calculateEntityFromLook(player, 32);
                if (res != null) {
                    gaeBolg.setTarget(res.getEntity());
                } else {
                    OrientedBoundingBox obb = new OrientedBoundingBox(new AABB(-8, -2, 0, 8, player.getBbHeight() + 4, 32)
                            .move(0, -player.getBbHeight() * 0.5, 0), player.getYRot(), -player.getXRot(), player.position().add(0, player.getBbHeight() * 0.5, 0));
                    List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox());
                    double d0 = Double.MAX_VALUE;
                    LivingEntity target = null;
                    for (LivingEntity entity : entities) {
                        if (entity == player || entities instanceof OwnableEntity ownable && player.getUUID().equals(ownable.getOwnerUUID()) || !obb.intersects(entity.getBoundingBox()))
                            continue;
                        double dist = entity.distanceToSqr(player);
                        if (dist < d0) {
                            target = entity;
                            d0 = dist;
                        }
                    }
                    if (target != null) {
                        gaeBolg.setTarget(target);
                    }
                }
                level.addFreshEntity(gaeBolg);
                if (!player.hasInfiniteMaterials()) {
                    stack.shrink(1);
                }
                return InteractionResultHolder.consume(stack);
            }
            player.sendSystemMessage(Component.translatable("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA));
            return InteractionResultHolder.fail(stack);
        }
        return InteractionResultHolder.success(stack);
    }
}
