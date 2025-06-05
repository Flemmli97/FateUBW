package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.summons.LesserMonster;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemGrimoire extends Item {

    public ItemGrimoire(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        if (Config.Common.grimoireMana > 0)
            tooltipComponents.add(new TranslatableComponent("fateubw.tooltip.item.mana", Config.Common.grimoireMana).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if (player.isCreative() || Platform.INSTANCE.getPlayerData(player).map(mana -> mana.useMana(player, Config.Common.grimoireMana)).orElse(false)) {
                this.spawn(player, player.getItemInHand(hand));
                return InteractionResultHolder.consume(player.getItemInHand(hand));
            }
            player.sendMessage(new TranslatableComponent("fateubw.chat.mana.missing").withStyle(ChatFormatting.AQUA), Util.NIL_UUID);
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
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
