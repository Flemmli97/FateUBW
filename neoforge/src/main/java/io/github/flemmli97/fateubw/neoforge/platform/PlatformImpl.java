package io.github.flemmli97.fateubw.neoforge.platform;

import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.neoforge.registry.FateAttachments;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.neoforge.event.EventHooks;

public class PlatformImpl implements Platform {

    @Override
    public PlayerData getPlayerData(Player player) {
        return player.getData(FateAttachments.PLAYER_DATA.get());
    }

    @Override
    public boolean canSpawnEvent(Mob mob, ServerLevelAccessor level, MobSpawnType spawnReason) {
        return EventHooks.checkSpawnPosition(mob, level, spawnReason);
    }

    @Override
    public CreativeModeTab.Builder tabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public boolean shouldSit(Entity entity) {
        return entity.getVehicle() != null && entity.getVehicle().shouldRiderSit();
    }
}
