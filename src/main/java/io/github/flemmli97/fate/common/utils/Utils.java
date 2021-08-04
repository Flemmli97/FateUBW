package io.github.flemmli97.fate.common.utils;

import io.github.flemmli97.fate.common.capability.CapabilityInsts;
import io.github.flemmli97.fate.common.capability.PlayerCap;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.common.registry.FateAttributes;
import io.github.flemmli97.fate.common.world.TruceHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Utils {

    public static boolean testNearbyEnemy(EntityServant servant) {
        List<?> var1 = servant.world.getEntitiesWithinAABB(EntityServant.class, servant.getBoundingBox().expand(32, 3.0D, 32));
        List<?> var2 = servant.world.getEntitiesWithinAABB(EntityServant.class, servant.getBoundingBox().expand(15, 3.0D, 15));
        return var1.size() > 1 && var2.size() < 2;
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
        UUID other = servant.getOwnerUUID();
        return other != null && TruceHandler.get(player.getServerWorld()).get(player.getUniqueID()).contains(other);
    }

    public static boolean inSameTeam(EntityServant servant, EntityServant other) {
        UUID first = servant.getOwnerUUID();
        UUID second = other.getOwnerUUID();
        return first != null && other != null && TruceHandler.get((ServerWorld) servant.world).get(first).contains(second);
    }

    public static EntityServant getServant(PlayerEntity player) {
        Optional<PlayerCap> cap = player.getCapability(CapabilityInsts.PlayerCap).resolve();
        return cap.map(iPlayer -> iPlayer.getServant(player)).orElse(null);
    }
}
