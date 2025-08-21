package io.github.flemmli97.fateubw.api.datapack;

import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import net.minecraft.world.entity.EntityType;

public class DataAccess {

    public static ServantProperties get(EntityType<?> type) {
        return DatapackHandler.SERVANT_PROPS.get(type);
    }
}
