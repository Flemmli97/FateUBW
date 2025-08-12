package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.mixinhelper.SynchedEntityDataExtension;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(SynchedEntityData.class)
public class SynchedEntityDataAccessor implements SynchedEntityDataExtension {

    @Shadow
    @Final
    private SynchedEntityData.DataItem<?>[] itemsById;

    @Override
    public List<SynchedEntityData.DataValue<?>> fate$getAll() {
        List<SynchedEntityData.DataValue<?>> list = new ArrayList<>();
        for (SynchedEntityData.DataItem<?> dataItem : this.itemsById) {
            list.add(dataItem.value());
        }
        return list;
    }
}
