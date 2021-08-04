package io.github.flemmli97.fate.common.items.weapons;

import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.entity.minions.EntityLesserMonster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemGrimoire extends Item {

    public ItemGrimoire(Properties props) {
        super(props);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (!world.isRemote) {
            EntityLesserMonster monster = new EntityLesserMonster(world, player);

            if (player.isCreative()) {
                this.spawn(player, monster, player.getHeldItem(hand));
                return ActionResult.resultSuccess(player.getHeldItem(hand));
            } else {
                if (player.getCapability(CapabilityInsts.PlayerCap).map(mana -> mana.useMana(player, 30)).orElse(false)) {
                    this.spawn(player, monster, player.getHeldItem(hand));
                    player.sendMessage(new TranslationTextComponent("fate.mana.use").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                    return ActionResult.resultSuccess(player.getHeldItem(hand));
                } else {
                    player.sendMessage(new TranslationTextComponent("fate.mana.no").mergeStyle(TextFormatting.AQUA), Util.DUMMY_UUID);
                }
            }
            return ActionResult.resultPass(player.getHeldItem(hand));
        }
        return ActionResult.resultPass(player.getHeldItem(hand));
    }

    private void spawn(PlayerEntity player, EntityLesserMonster monster, ItemStack stack) {
        player.world.addEntity(monster);
        if (player.getLastAttackedEntity() != null)
            monster.setAttackTarget(player.getLastAttackedEntity());
        player.getCooldownTracker().setCooldown(stack.getItem(), 50);
    }
}
