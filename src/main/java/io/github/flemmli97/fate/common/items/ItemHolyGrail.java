package io.github.flemmli97.fate.common.items;

import io.github.flemmli97.fate.network.PacketHandler;
import io.github.flemmli97.fate.network.S2CGrailGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemHolyGrail extends Item {

    public ItemHolyGrail(Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            PacketHandler.sendToClient(new S2CGrailGui(), (ServerPlayerEntity) player);
            return ActionResult.resultConsume(player.getHeldItem(hand));
        }
        return ActionResult.resultPass(player.getHeldItem(hand));
    }
}
