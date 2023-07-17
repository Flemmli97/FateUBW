package io.github.flemmli97.fateubw.common.items.weapons;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ItemMedusaDagger extends ItemDagger {

    public ItemMedusaDagger(Tier tier, int baseDmg, float speed, Properties props) {
        super(tier, baseDmg, speed, props);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            Optional<PlayerData> opt = Platform.INSTANCE.getPlayerData(player);
            ChainDagger thrownDagger = opt.map(PlayerData::getThrownDagger).orElse(null);
            if (thrownDagger == null) {
                ChainDagger dagger = new ChainDagger(world, player, hand == InteractionHand.MAIN_HAND);
                dagger.shoot(player, player.getXRot(), player.getYRot(), 0, 1.5f, 0);
                world.addFreshEntity(dagger);
                opt.ifPresent(data -> data.setThrownDagger(dagger));
            } else {
                thrownDagger.retractHook();
            }
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }
}
