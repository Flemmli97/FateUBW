package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.common.network.S2CGrailGui;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemHolyGrail extends Item {

    public ItemHolyGrail(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkCalls.INSTANCE.sendToClient(new S2CGrailGui(), serverPlayer);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
