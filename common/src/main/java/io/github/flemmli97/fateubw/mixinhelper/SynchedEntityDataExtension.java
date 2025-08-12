package io.github.flemmli97.fateubw.mixinhelper;

import net.minecraft.network.syncher.SynchedEntityData;

import java.util.List;

public interface SynchedEntityDataExtension {

    List<SynchedEntityData.DataValue<?>> fate$getAll();
}
