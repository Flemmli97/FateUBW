package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.entity.misc.GaeBolg;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemGaeBolg extends ClassSpear {

    public ItemGaeBolg(Item.Properties props) {
        super(ItemTiers.gae_bolg, props, -1.5f, 4);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide) {
            GaeBolg gaeBolg = new GaeBolg(world, player);
            gaeBolg.shoot(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);

            if (player.isCreative()) {
                world.addFreshEntity(gaeBolg);
                return InteractionResultHolder.success(stack);
            } else {
                if (Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, 15)).orElse(false)) {
                    world.addFreshEntity(gaeBolg);
                    stack.shrink(1);
                    player.sendMessage(new TranslatableComponent("fate.mana.use").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                    return InteractionResultHolder.success(stack);

                } else {
                    player.sendMessage(new TranslatableComponent("fate.mana.no").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                }
            }
            return InteractionResultHolder.pass(stack);
        }
        return InteractionResultHolder.fail(stack);
    }
}
