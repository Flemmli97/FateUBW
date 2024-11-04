package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2COpenGui;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ItemServantCommander extends Item {

    public static final String KEY = Fate.MODID + ":InteractionUUID";

    public ItemServantCommander(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND)
            return InteractionResult.PASS;
        if (!(player instanceof ServerPlayer serverPlayer))
            return living instanceof BaseServant ? InteractionResult.SUCCESS : InteractionResult.PASS;
        if (living instanceof BaseServant servant && player.getUUID().equals((servant.getOwnerUUID()))) {
            stack = player.getItemInHand(hand); // passed in stack is a copy if in creative. this ensures nbt is set
            setInteractionEntity(stack, living);
            NetworkCalls.INSTANCE.sendToClient(new S2COpenGui(servant), serverPlayer);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public static UUID getInteractionEntity(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.hasUUID(KEY) ? stack.getTag().getUUID(KEY) : null;
    }

    public static void setInteractionEntity(ItemStack stack, LivingEntity entity) {
        CompoundTag compound = new CompoundTag();
        compound.putUUID(KEY, entity.getUUID());
        stack.setTag(compound);
    }
}