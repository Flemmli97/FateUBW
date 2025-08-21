package io.github.flemmli97.fateubw.mixin;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.mixinhelper.HorseExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin implements HorseExtension {

    @Unique
    private boolean fate$totalControl;

    @Inject(method = "isStanding", at = @At("HEAD"), cancellable = true)
    private void standInject(CallbackInfoReturnable<Boolean> info) {
        if (this.fate$totalControl) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void saveData(CompoundTag compound, CallbackInfo info) {
        if (this.fate$totalControl) {
            compound.putBoolean(Fate.MODID + ":TotalControl", true);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readData(CompoundTag compound, CallbackInfo info) {
        this.fate$totalControl = compound.getBoolean(Fate.MODID + ":TotalControl");
    }

    @Override
    public void fate$setTotalControl(boolean flag) {
        this.fate$totalControl = flag;
    }
}
