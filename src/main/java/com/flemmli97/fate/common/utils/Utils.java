package com.flemmli97.fate.common.utils;

import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.world.TruceHandler;
import com.flemmli97.fate.common.registry.FateAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Utils {

    public static boolean testNearbyEnemy(EntityServant servant) {
        List<?> var1 = servant.world.getEntitiesWithinAABB(EntityServant.class, servant.getBoundingBox().expand((double) 32, 3.0D, (double) 32));
        List<?> var2 = servant.world.getEntitiesWithinAABB(EntityServant.class, servant.getBoundingBox().expand((double) 15, 3.0D, (double) 15));
        if (var1.size() > 1 && var2.size() < 2)
            return true;
        return false;
    }

    public static float getDamageAfterMagicAbsorb(EntityServant servant, float damage) {
        return (float) (damage * servant.getAttribute(FateAttributes.MAGIC_RESISTANCE).getValue());
    }

    public static float projectileReduce(EntityServant servant, float damage) {
        float reduceAmount = (float) (servant.getAttribute(FateAttributes.PROJECTILE_RESISTANCE).getValue() * 0.04);
        return damage * reduceAmount;
    }

    public static boolean inSameTeam(ServerPlayerEntity player, PlayerEntity other) {
        return TruceHandler.get(player.getServerWorld()).get(player.getUniqueID()).contains(other.getUniqueID());
    }

    public static boolean inSameTeam(ServerPlayerEntity player, EntityServant servant) {
        UUID other = servant.ownerUUID();
        return other == null ? false : TruceHandler.get(player.getServerWorld()).get(player.getUniqueID()).contains(other);
    }

    public static boolean inSameTeam(EntityServant servant, EntityServant other) {
        UUID first = servant.ownerUUID();
        UUID second = other.ownerUUID();
        return (first == null || other == null) ? false : TruceHandler.get((ServerWorld) servant.world).get(first).contains(second);
    }

    public static EntityServant getServant(PlayerEntity player) {
        Optional<IPlayer> cap = player.getCapability(PlayerCapProvider.PlayerCap).resolve();
        if(cap.isPresent())
            return cap.get().getServant(player);
        return null;
    }
}
