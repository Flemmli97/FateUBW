package io.github.flemmli97.fateubw.common.attachment;

import io.github.flemmli97.fateubw.common.network.S2CItemInUse;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.world.entity.LivingEntity;

public class ItemStackData {

    private boolean inUse;

    public void setInUse(LivingEntity entity, boolean inUse, boolean mainHand) {
        this.inUse = inUse;
        if (!entity.level.isClientSide) {
            NetworkCalls.INSTANCE.sendToTracking(new S2CItemInUse(entity.getId(), this.inUse, mainHand), entity);
        }
    }

    public boolean inUse() {
        return this.inUse;
    }
}
