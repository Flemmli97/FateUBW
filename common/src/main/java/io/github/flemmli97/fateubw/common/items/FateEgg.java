package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.common.components.ServantSpawneggData;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CSpawnEggScreen;
import io.github.flemmli97.fateubw.common.registry.FateDataComponents;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class FateEgg extends SpawnEgg {

    public FateEgg(Supplier<? extends EntityType<? extends BaseServant>> type, int primary, int secondary, Properties props) {
        super(new EntityTypeHolder<>(BaseServant.class, type), primary, secondary, props);
    }

    @Override
    public boolean addToDefaultSpawneggs() {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("fateubw.tooltip.item.spawn").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (player instanceof ServerPlayer serverPlayer && e instanceof BaseServant servant) {
            ServantSpawneggData data = stack.getOrDefault(FateDataComponents.SERVANT_EGG_DATA.get(), ServantSpawneggData.DEFAULT);
            if (data.withMaster()) {
                servant.setOwner(player);
                if (data.joinGrailwar()) {
                    GrailWarHandler track = GrailWarHandler.get(serverPlayer.getServer());
                    GrailWarHandler.JoinResult res = track.checkJoining(serverPlayer);
                    if (res != GrailWarHandler.JoinResult.SUCCESS) {
                        player.sendSystemMessage(Component.translatable(res.translationKey).withStyle(ChatFormatting.RED));
                    } else {
                        track.join(servant);
                    }
                }
            }
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> res = super.use(level, player, hand);
        if (res.getResult() == InteractionResult.PASS) {
            if (player instanceof ServerPlayer serverPlayer)
                LoaderNetwork.INSTANCE.sendToPlayer(new S2CSpawnEggScreen(hand), serverPlayer);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return res;
    }
}