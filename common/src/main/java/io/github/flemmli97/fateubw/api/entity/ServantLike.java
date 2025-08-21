package io.github.flemmli97.fateubw.api.entity;

import io.github.flemmli97.fateubw.api.datapack.ServantProperties;
import io.github.flemmli97.fateubw.common.entity.BaseServant;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * Entities able to participate in a grailwar should implement this in case you want to run your own entity version.
 * <p>
 * One implementation example can be seen at {@link BaseServant}
 */
public interface ServantLike<T extends Mob & ServantLike<T>> extends OwnableEntity {

    TicketType<ChunkPos> TRACKINGTICKET = TicketType.create("servant", Comparator.comparingLong(ChunkPos::toLong), 5);

    @SuppressWarnings("unchecked")
    default T get() {
        return (T) this;
    }

    /**
     * Call this from the tick method within your entity.
     * This will make your entity load the chunk its currently in which is required for proper participant lookup.
     * Else your entity won't be found if unloaded
     */
    default void trackingTick() {
        T entity = this.get();
        if (entity.level() instanceof ServerLevel serverLevel) {
            GrailWarHandler handler = GrailWarHandler.get(serverLevel.getServer());
            if (handler.isParticipant(entity)) {
                ChunkPos pos = entity.chunkPosition();
                ((ServerChunkCache) entity.level().getChunkSource()).addRegionTicket(TRACKINGTICKET, pos, 2, pos);
                handler.moveToPlayer(entity);
            }
        }
    }

    @Override
    @Nullable
    Player getOwner();

    void setOwner(Player player);

    /**
     * You can obtain one from the datapack system via {@link io.github.flemmli97.fateubw.api.datapack.DataAccess#get(EntityType)}
     * which you should cache
     */
    ServantProperties props();

    Component nobelPhantasm();

    void onPlayerCommand(ServerPlayer player, CommandType command);

    /**
     * Additional commands the player can give.
     * One example would be commanding medea to place a magic circle
     */
    default String[] specialCommands() {
        return null;
    }

    default void doSpecialCommand(ServerPlayer sender, String id) {

    }

    /**
     * Gets called when the owner player needs additional entity data in their gui such as armor etc.
     * If the player is not tracking this entity the data would otherwise be missing.
     * If you want that data you need to manually sync it
     */
    void shouldScheduleEntityDataSync(boolean sync);
}
