package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.common.entity.misc.ThrownGem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemCrystal extends Item {

    public ItemCrystal(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide)
            return InteractionResultHolder.success(player.getItemInHand(hand));
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isCreative())
            stack.shrink(1);
        player.playSound(SoundEvents.ARROW_SHOOT, 0.5F, 0.4F / (player.getRandom().nextFloat() * 0.4F + 0.8F));
        ThrownGem gem = new ThrownGem(world, player);
        gem.shoot(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
        world.addFreshEntity(gem);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
