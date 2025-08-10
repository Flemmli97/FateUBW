package io.github.flemmli97.fateubw.platform;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.tenshilib.loader.LoaderInitializer;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;

public interface Platform {

    Platform INSTANCE = LoaderInitializer.getImplInstance(Platform.class,
            "io.github.flemmli97.fateubw.fabric.platform.PlatformImpl",
            "io.github.flemmli97.fateubw.forge.platform.PlatformImpl");

    boolean isDatagen();

    Optional<PlayerData> getPlayerData(Player player);

    boolean canSpawnEvent(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason, SpawnPlacementType place);

    <T extends CriterionTrigger<?>> T registerCriteriaTrigger(T criterion);

    CreativeModeTab.Builder tabBuilder();

    AbstractArrow customBowArrow(BowItem item, AbstractArrow def);

    default boolean shouldSit(Entity entity) {
        return entity.getVehicle() != null;
    }
}
