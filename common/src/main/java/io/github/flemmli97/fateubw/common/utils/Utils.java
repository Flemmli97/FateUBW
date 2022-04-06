package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.registry.ModAttributes;
import io.github.flemmli97.fateubw.common.world.TruceHandler;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;

public class Utils {

    public static boolean testNearbyEnemy(BaseServant servant) {
        List<?> var1 = servant.level.getEntitiesOfClass(BaseServant.class, servant.getBoundingBox().expandTowards(32, 3.0D, 32));
        List<?> var2 = servant.level.getEntitiesOfClass(BaseServant.class, servant.getBoundingBox().expandTowards(15, 3.0D, 15));
        return var1.size() > 1 && var2.size() < 2;
    }

    public static float getDamageAfterMagicAbsorb(BaseServant servant, float damage) {
        return (float) (damage * servant.getAttribute(ModAttributes.MAGIC_RESISTANCE.get()).getValue());
    }

    public static float projectileReduce(BaseServant servant, float damage) {
        float reduceAmount = (float) (servant.getAttribute(ModAttributes.PROJECTILE_RESISTANCE.get()).getValue() * 0.04);
        return damage * reduceAmount;
    }

    public static boolean inSameTeam(ServerPlayer player, UUID other) {
        return TruceHandler.get(player.getServer()).get(player.getUUID()).contains(other);
    }

    public static boolean inSameTeam(ServerPlayer player, BaseServant servant) {
        UUID other = servant.getOwnerUUID();
        return other != null && TruceHandler.get(player.getServer()).get(player.getUUID()).contains(other);
    }

    public static boolean inSameTeam(BaseServant servant, BaseServant other) {
        if (servant.getServer() == null)
            return false;
        UUID first = servant.getOwnerUUID();
        UUID second = other.getOwnerUUID();
        return first != null && other != null && TruceHandler.get(servant.getServer()).get(first).contains(second);
    }
}
