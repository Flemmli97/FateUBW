package io.github.flemmli97.fateubw.fabric.mixin;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.fabric.common.data.PlayerDataGet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements PlayerDataGet {

    @Unique
    private final PlayerData fateData = new PlayerData();

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void loadData(CompoundTag compound, CallbackInfo info) {
        this.fateData.readFromNBT(compound.getCompound(Fate.MODID + ":data"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void saveData(CompoundTag compound, CallbackInfo info) {
        compound.put(Fate.MODID + ":data", this.fateData.writeToNBT(new CompoundTag()));
    }

    @Override
    public PlayerData getData() {
        return this.fateData;
    }
}
