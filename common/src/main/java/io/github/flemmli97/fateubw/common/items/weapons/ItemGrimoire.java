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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (player.isCreative()) {
                this.spawn(player, player.getItemInHand(hand));
                return InteractionResultHolder.success(player.getItemInHand(hand));
            } else {
                if (Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, 30)).orElse(false)) {
                    this.spawn(player, player.getItemInHand(hand));
                    player.sendMessage(new TranslatableComponent("fateubw.mana.use").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                } else {
                    player.sendMessage(new TranslatableComponent("fateubw.mana.no").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
                }
            }
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    private void spawn(Player player, ItemStack stack) {
        LesserMonster monster = new LesserMonster(player.level, player);
        double x = player.getX() + player.getRandom().nextInt(8) - 4.0;
        double y = player.getY() + player.getRandom().nextInt(2) - 1.0;
        double z = player.getZ() + player.getRandom().nextInt(8) - 4.0;
        monster.setPos(x, y, z);
        int tries = 0;
        while (tries < 10) {
            if (player.level.noCollision(monster)) {
                break;
            }
            x = player.getX() + player.getRandom().nextInt(8) - 4.0;
            y = player.getY() + player.getRandom().nextInt(2) - 1.0;
            z = player.getZ() + player.getRandom().nextInt(8) - 4.0;
            monster.setPos(x, y, z);
            tries++;
        }
        if (tries == 10 && !player.level.noCollision(monster))
            return;
        player.level.addFreshEntity(monster);
        if (player.getLastHurtMob() != null)
            monster.setTarget(player.getLastHurtMob());
        player.getCooldowns().addCooldown(stack.getItem(), 50);
    }
}
