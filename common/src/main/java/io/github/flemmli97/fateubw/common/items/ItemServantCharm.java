package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemServantCharm extends Item {

    public final ResourceLocation type;

    public ItemServantCharm(ResourceLocation type, Properties props) {
        super(props);
        this.type = type;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (this.type == BuiltinServantClasses.NONE) {
            if (!level.isClientSide) {
                if (!player.isCreative())
                    stack.shrink(1);
                ItemEntity item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(),
                        new ItemStack(ModItems.CHARMS.get(level.random.nextInt(ModItems.CHARMS.size())).get()));
                item.setPickUpDelay(0);
                level.addFreshEntity(item);
            }
            return InteractionResultHolder.success(stack);

        }
        return InteractionResultHolder.pass(stack);
    }
}