package io.github.flemmli97.fateubw.platform;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.tenshilib.loader.LoaderInitializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ServerLevelAccessor;

public interface Platform {

    Platform INSTANCE = LoaderInitializer.getImplInstance(Platform.class,
            "io.github.flemmli97.fateubw.fabric.platform.PlatformImpl",
            "io.github.flemmli97.fateubw.neoforge.platform.PlatformImpl");

    PlayerData getPlayerData(Player player);

    boolean canSpawnEvent(Mob mob, ServerLevelAccessor level, MobSpawnType spawnReason);

    CreativeModeTab.Builder tabBuilder();

    default boolean shouldSit(Entity entity) {
        return entity.getVehicle() != null;
    }
}
