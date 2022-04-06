package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.entity.minions.LesserMonster;
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

public class ItemGrimoire extends Item {

    public ItemGrimoire(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            LesserMonster monster = new LesserMonster(world, player);

            if (player.isCreative()) {
                this.spawn(player, monster, player.getItemInHand(hand));
                return InteractionResultHolder.success(player.getItemInHand(hand));
            } else {
                if (Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, 30)).orElse(false)) {
                    this.spawn(player, monster, player.getItemInHand(hand));
                    player.sendMessage(new TranslatableComponent("fate.mana.use").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                } else {
                    player.sendMessage(new TranslatableComponent("fate.mana.no").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                }
            }
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private void spawn(Player player, LesserMonster monster, ItemStack stack) {
        player.level.addFreshEntity(monster);
        if (player.getLastHurtMob() != null)
            monster.setTarget(player.getLastHurtMob());
        player.getCooldowns().addCooldown(stack.getItem(), 50);
    }
}
