package io.github.flemmli97.fateubw.common.items;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
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
        super(type, primary, secondary, props);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, Player player) {
        if (player instanceof ServerPlayer && stack.hasCustomHoverName() && "Summon".equals(stack.getHoverName().getContents())) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                if (data.getServantUUID() == null) {
                    data.setServant((BaseServant) e);
                    ((BaseServant) e).setOwner(player);
                    GrailWarHandler track = GrailWarHandler.get(player.getServer());
                    track.join((ServerPlayer) player);
                } else {
                    player.sendMessage(new TranslatableComponent("chat.item.spawn").withStyle(ChatFormatting.RED), Util.NIL_UUID);
                }
            });
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public Component getEntityName(ItemStack stack) {
        Component comp = super.getEntityName(stack);
        if (comp != null && "Summon".equals(comp.getContents()))
            return null;
        return comp;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.item.spawn").withStyle(ChatFormatting.GOLD));
        super.appendHoverText(stack, world, tooltip, flag);
    }
}