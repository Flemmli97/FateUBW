package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ItemModelProps;
import io.github.flemmli97.fateubw.common.items.weapons.ClassSpear;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        int sealid = 1;
        for (RegistryEntrySupplier<Item> reg : ModItems.ITEMS.getEntries()) {
            if (reg == ModItems.ENUMAELISH || reg == ModItems.HERACLES_AXE)
                continue;
            if (reg == ModItems.MEDUSA_DAGGER) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("handheld"))
                        .texture("layer0", this.itemTexture(reg.getID()))
                        .override().predicate(ItemModelProps.THROWN_DAGGER_ID, 0)
                        .predicate(ItemModelProps.HELD_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_held", ModelLocationUtils.decorateItemModelLocation("handheld"))
                                .texture("layer0", this.itemTexture(reg.getID().getPath() + "_held"))).end()
                        .override().predicate(ItemModelProps.THROWN_DAGGER_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_thrown", ModelLocationUtils.decorateItemModelLocation("handheld"))
                                .texture("layer0", this.itemTexture(reg.getID().getPath() + "_thrown"))
                                .transforms().transform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
                                .rotation(0, 90, -25)
                                .translation(1.13f, 6.3f, 1.13f)
                                .scale(0.68f, 0.68f, 0.68f)
                                .end()
                                .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                                .rotation(0, -90, 25)
                                .translation(1.13f, 6.3f, 1.13f)
                                .scale(0.68f, 0.68f, 0.68f)
                                .end()
                                .end());
            } else if (reg == ModItems.ARCHBOW) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", "fateubw:items/emiyas_bow")
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.05f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 0)
                        .model(this.withExistingParent(reg.getID().getPath() + "_pull_0", new ResourceLocation(Fate.MODID, "item/" + reg.getID().getPath()))
                                .texture("layer0", "fateubw:items/emiyas_bow_pull_0")).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.65f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 0)
                        .model(this.withExistingParent(reg.getID().getPath() + "_pull_1", new ResourceLocation(Fate.MODID, "item/" + reg.getID().getPath()))
                                .texture("layer0", "fateubw:items/emiyas_bow_pull_1")).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.9f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 0)
                        .model(this.withExistingParent(reg.getID().getPath() + "_pull_2", new ResourceLocation(Fate.MODID, "item/" + reg.getID().getPath()))
                                .texture("layer0", "fateubw:items/emiyas_bow_pull_2")).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.05f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_caladbolg_pull_0", new ResourceLocation(Fate.MODID, "item/" + reg.getID().getPath()))
                                .texture("layer0", "fateubw:items/emiyas_bow_caladbolg_pull_0")).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.65f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_caladbolg_pull_1", new ResourceLocation(Fate.MODID, "item/" + reg.getID().getPath()))
                                .texture("layer0", "fateubw:items/emiyas_bow_caladbolg_pull_1")).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.9f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_caladbolg_pull_2", new ResourceLocation(Fate.MODID, "item/" + reg.getID().getPath()))
                                .texture("layer0", "fateubw:items/emiyas_bow_caladbolg_pull_2")).end()
                        .transforms().transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                        .rotation(-80, 260, -40)
                        .translation(-1, -2, 2.5f)
                        .scale(0.9f, 0.9f, 0.9f)
                        .end()
                        .transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
                        .rotation(-80, -280, 40)
                        .translation(-1, -2, 2.5f)
                        .scale(0.9f, 0.9f, 0.9f)
                        .end()
                        .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                        .rotation(0, -90, 25)
                        .translation(1.13f, 3.2f, 1.13f)
                        .scale(0.68f, 0.68f, 0.68f)
                        .end()
                        .transform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
                        .rotation(0, 90, -25)
                        .translation(1.13f, 3.2f, 1.13f)
                        .scale(0.68f, 0.68f, 0.68f)
                        .end();
            } else if (reg == ModItems.GAEBOLG || reg == ModItems.GAEBUIDHE
                    || reg == ModItems.GAEDEARG) {
                this.withExistingParent(reg.getID().getPath(), new ResourceLocation(Fate.MODID, "item/spear_item"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg == ModItems.ARONDIGHT || reg == ModItems.MONOHOSHI_ZAO || reg == ModItems.STAFF) {
                this.withExistingParent(reg.getID().getPath(), new ResourceLocation(Fate.MODID, "item/32x32_weapon"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg.get() instanceof SpawnEgg)
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
            else if (reg == ModItems.INVISEXCALIBUR) {
                this.withExistingParent(reg.getID().getPath(), new ResourceLocation(Fate.MODID, "item/32x32_weapon"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            }else if (reg == ModItems.EXCALIBUR) {
                this.withExistingParent(reg.getID().getPath(), new ResourceLocation(Fate.MODID, "item/32x32_weapon"))
                        .texture("layer0", "fateubw:items/excalibur")
                        .override().predicate(ItemModelProps.ACTIVE_ID, 1).model(this.getExistingFile(new ResourceLocation(Fate.MODID, "excalibur_active")));
            } else if (reg.get() instanceof BlockItem blockItem) {
                this.getBuilder(reg.getID().getPath()).parent(new ModelFile.UncheckedModelFile(new ResourceLocation(blockItem.getBlock().getRegistryName().getNamespace(), "block/" + blockItem.getBlock().getRegistryName().getPath())));
            } else if (reg.get() instanceof SwordItem || reg.get() instanceof ClassSpear || reg == ModItems.STAFF) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("handheld"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg == ModItems.ICON_0 || reg == ModItems.ICON_1 || reg == ModItems.ICON_2 || reg == ModItems.ICON_3) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", new ResourceLocation(reg.getID().getNamespace(), "gui/command_seal_" + sealid));
                sealid++;
            } else if (reg == ModItems.COMMANDER) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", new ResourceLocation(reg.getID().getNamespace(), "gui/command_seal_1"));
            } else if (reg == ModItems.CRYSTAL_RED || reg == ModItems.CRYSTAL_YELLOW || reg == ModItems.CRYSTAL_BLACK || reg == ModItems.CRYSTAL_BLUE || reg == ModItems.CRYSTAL_GREEN) {
                this.withExistingParent(reg.getID().getPath(), new ResourceLocation(Fate.MODID, "item/gem_shard"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            }
        }
    }

    private ResourceLocation itemTexture(ResourceLocation item) {
        return new ResourceLocation(item.getNamespace(), "items/" + item.getPath());
    }

    private ResourceLocation itemTexture(String texture) {
        return new ResourceLocation(Fate.MODID, "items/" + texture);
    }
}
