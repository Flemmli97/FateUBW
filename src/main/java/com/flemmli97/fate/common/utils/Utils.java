package com.flemmli97.fate.common.utils;

import com.flemmli97.fate.Fate;
import com.flemmli97.fate.common.capability.IPlayer;
import com.flemmli97.fate.common.capability.PlayerCapProvider;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.grail.TruceHandler;
import com.flemmli97.fate.common.registry.FateAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static boolean inSameTeam(PlayerEntity player, PlayerEntity other) {
        return TruceHandler.get(player.world).get(player.getUniqueID()).contains(other.getUniqueID());
    }

    public static boolean inSameTeam(PlayerEntity player, EntityServant servant) {
        UUID other = servant.ownerUUID();
        return other == null ? false : TruceHandler.get(player.world).get(player.getUniqueID()).contains(other);
    }

    public static boolean inSameTeam(EntityServant servant, EntityServant other) {
        UUID first = servant.ownerUUID();
        UUID second = other.ownerUUID();
        return (first == null || other == null) ? false : TruceHandler.get(servant.world).get(first).contains(second);
    }

    public static <T> T capGet(PlayerEntity player, Function<IPlayer, T> func, T or) {
        Optional<IPlayer> cap = player.getCapability(PlayerCapProvider.PlayerCap).resolve();
        if (cap.isPresent()) {
            return func.apply(cap.get());
        }
        return or;
    }

    public static void capDo(PlayerEntity player, Consumer<IPlayer> cons) {
        Optional<IPlayer> cap = player.getCapability(PlayerCapProvider.PlayerCap).resolve();
        if (cap.isPresent()) {
            cons.accept(cap.get());
        }
    }

    public static <V extends ForgeRegistryEntry<?>> V setRegName(V v, String name) {
        v.setRegistryName(new ResourceLocation(Fate.MODID, name));
        return v;
    }

    public static <V extends ForgeRegistryEntry<?>> V setRegName(V v, ResourceLocation name) {
        v.setRegistryName(name);
        return v;
    }
}
