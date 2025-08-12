package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.JsonCodecProvider;

import java.util.concurrent.CompletableFuture;

public class DamageTypeGen extends JsonCodecProvider<DamageType> {

    public DamageTypeGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, PackOutput.Target.DATA_PACK, Registries.DAMAGE_TYPE.location().getPath(), PackType.SERVER_DATA, DamageType.DIRECT_CODEC,
                lookupProvider, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void gather() {
        for (ResourceKey<DamageType> types : FateDamageTypes.TRANSLATIONS.keySet()) {
            if (types.equals(FateDamageTypes.GRAIL)) {
                this.unconditional(FateDamageTypes.GRAIL.location(), new DamageType(
                        FateDamageTypes.GRAIL.location().toLanguageKey(), DamageScaling.NEVER, 0.25f, DamageEffects.HURT));
            } else {
                this.unconditional(types.location(), new DamageType(
                        types.location().toLanguageKey(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f, DamageEffects.HURT));
            }
        }
    }
}
