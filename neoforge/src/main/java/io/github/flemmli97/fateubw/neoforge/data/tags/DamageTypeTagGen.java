package io.github.flemmli97.fateubw.neoforge.data.tags;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.lib.FateTags;
import io.github.flemmli97.fateubw.common.registry.FateDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGen extends TagsProvider<DamageType> {

    public DamageTypeTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, Fate.MODID, fileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(FateDamageTypes.GRAIL, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_ENCHANTMENTS, DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.BYPASSES_RESISTANCE, DamageTypeTags.BYPASSES_INVULNERABILITY, DamageTypeTags.NO_KNOCKBACK);

        this.tag(FateDamageTypes.EXCALIBUR, DamageTypeTags.BYPASSES_ARMOR, FateTags.DamageTypes.IS_MAGIC,
                DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.NO_KNOCKBACK);
        this.tag(FateDamageTypes.ENUMA_ELISH, DamageTypeTags.BYPASSES_ARMOR, FateTags.DamageTypes.IS_MAGIC,
                DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.NO_KNOCKBACK);
        this.tag(FateDamageTypes.BABYLON, DamageTypeTags.IS_PROJECTILE);
        this.tag(FateDamageTypes.THROWN_ITEM, DamageTypeTags.IS_PROJECTILE);
        this.tag(FateDamageTypes.GAE_BOLG, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_SHIELD,
                DamageTypeTags.BYPASSES_SHIELD, FateTags.DamageTypes.IS_MAGIC);
        this.tag(FateDamageTypes.CALADBOLG, DamageTypeTags.BYPASSES_ARMOR,
                DamageTypeTags.BYPASSES_SHIELD, FateTags.DamageTypes.IS_MAGIC);
        this.tag(FateDamageTypes.ARCHER_NORMAL, DamageTypeTags.IS_PROJECTILE);
        this.tag(FateDamageTypes.MAGIC_BEAM, DamageTypeTags.BYPASSES_ARMOR, FateTags.DamageTypes.IS_MAGIC);
        this.tag(FateDamageTypes.MAGIC_SHOT, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.IS_PROJECTILE, FateTags.DamageTypes.IS_MAGIC);
        this.tag(FateDamageTypes.TSUBAME, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.BYPASSES_COOLDOWN);
        this.tag(FateDamageTypes.GAE_DEARG, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.BYPASSES_ENCHANTMENTS);
        this.tag(FateDamageTypes.GORDIUS_TRAMPLE, DamageTypeTags.IS_LIGHTNING);
        this.tag(FateDamageTypes.PEGASUS_CHARGE, DamageTypeTags.BYPASSES_ARMOR, FateTags.DamageTypes.IS_MAGIC);
        this.tag(FateDamageTypes.PETRIFICATION, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.BYPASSES_COOLDOWN, DamageTypeTags.NO_KNOCKBACK);
        this.tag(FateDamageTypes.AESTUS_DOMUS, DamageTypeTags.BYPASSES_ARMOR, DamageTypeTags.BYPASSES_SHIELD, DamageTypeTags.NO_KNOCKBACK);

        this.tag(Tags.DamageTypes.IS_MAGIC)
                .addTag(FateTags.DamageTypes.IS_MAGIC);
    }

    @SafeVarargs
    protected final void tag(ResourceKey<DamageType> key, TagKey<DamageType>... tags) {
        for (TagKey<DamageType> tag : tags) {
            this.tag(tag).add(key);
        }
    }
}
