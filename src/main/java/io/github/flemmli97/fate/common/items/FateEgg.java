package io.github.flemmli97.fate.common.items;

import com.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.fate.common.capability.PlayerCapProvider;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.common.world.GrailWarHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.function.Supplier;

public class FateEgg extends SpawnEgg {

    public FateEgg(Supplier<? extends EntityType<? extends EntityServant>> type, int primary, int secondary, Properties props) {
        super(type, primary, secondary, props);
    }

    @Override
    public boolean onEntitySpawned(Entity e, ItemStack stack, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity && stack.hasDisplayName() && "Summon".equals(stack.getDisplayName().getUnformattedComponentText())) {
            player.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {
                if (cap.getServant(player) == null) {
                    cap.setServant(player, (EntityServant) e);
                    ((EntityServant) e).setOwner(player);
                    GrailWarHandler track = GrailWarHandler.get((ServerWorld) player.world);
                    track.join((ServerPlayerEntity) player);
                } else {
                    player.sendMessage(new TranslationTextComponent("chat.item.spawn").mergeStyle(TextFormatting.RED), Util.DUMMY_UUID);
                }
            });
        }
        return super.onEntitySpawned(e, stack, player);
    }

    @Override
    public ITextComponent getEntityName(ItemStack stack) {
        ITextComponent comp = super.getEntityName(stack);
        if (comp != null && "Summon".equals(comp.getUnformattedComponentText()))
            return null;
        return comp;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent("item.spawn.tooltip").mergeStyle(TextFormatting.GOLD));
        super.addInformation(stack, world, tooltip, flag);
    }
}