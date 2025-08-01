package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CSpawnEggScreen;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

    public static final String MASTER = Fate.MODID + ":Master";
    public static final String WAR = Fate.MODID + ":JoinWar";

    public FateEgg(Supplier<? extends EntityType<? extends BaseServant>> type, int primary, int secondary, Properties props) {
        super(new EntityTypeHolder<>(BaseServant.class, type), primary, secondary, props);
    }

    @Override
    public boolean addToDefaultSpawneggs() {
        return false;
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (player instanceof ServerPlayer serverPlayer && e instanceof BaseServant servant) {
            boolean owned = spawnOwned(stack);
            if (owned) {
                servant.setOwner(player);
            }
            if (owned && joinGrailwar(stack)) {
                GrailWarHandler track = GrailWarHandler.get(serverPlayer.getLevel().getServer());
                GrailWarHandler.JoinResult res = track.checkJoining(serverPlayer);
                if (res != GrailWarHandler.JoinResult.SUCCESS) {
                    player.sendMessage(new TranslatableComponent(res.translationKey).withStyle(ChatFormatting.RED), Util.NIL_UUID);
                } else {
                    track.join(servant);
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
                NetworkCalls.INSTANCE.sendToClient(new S2CSpawnEggScreen(hand), serverPlayer);
            return InteractionResultHolder.consume(player.getItemInHand(hand));
        }
        return res;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("fateubw.tooltip.item.spawn").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public static boolean spawnOwned(ItemStack stack) {
        boolean withMaster = false;
        if (stack.hasTag() && stack.getTag().contains(MASTER)) {
            withMaster = stack.getTag().getBoolean(MASTER);
        }
        return withMaster;
    }

    public static void withMaster(ItemStack stack, boolean master) {
        stack.getOrCreateTag().putBoolean(MASTER, master);
    }

    public static boolean joinGrailwar(ItemStack stack) {
        boolean joinWar = false;
        if (stack.hasTag() && stack.getTag().contains(WAR)) {
            joinWar = stack.getTag().getBoolean(WAR);
        }
        return joinWar;
    }

    public static void setJoinWar(ItemStack stack, boolean join) {
        stack.getOrCreateTag().putBoolean(WAR, join);
    }
}