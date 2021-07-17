package io.github.flemmli97.fate.common.items.weapons;

import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.capability.IPlayer;
import io.github.flemmli97.fate.common.entity.EntityDaggerHook;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class ItemMedusaDagger extends ItemDagger {

    public ItemMedusaDagger(IItemTier tier, int baseDmg, float speed, Properties props) {
        super(tier, baseDmg, speed, props);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            LazyOptional<IPlayer> opt = player.getCapability(CapabilityInsts.PlayerCap);
            EntityDaggerHook thrownDagger = opt.resolve().map(IPlayer::getThrownDagger).orElse(null);
            if (thrownDagger == null) {
                EntityDaggerHook hook = new EntityDaggerHook(world, player);
                hook.shoot(player, player.rotationPitch, player.rotationYaw, 0, 3, 0);
                world.addEntity(hook);
                opt.ifPresent(cap -> cap.setThrownDagger(hook));
            } else {
                thrownDagger.retractHook();
            }
        }
        return ActionResult.resultConsume(player.getHeldItem(hand));
    }
}
