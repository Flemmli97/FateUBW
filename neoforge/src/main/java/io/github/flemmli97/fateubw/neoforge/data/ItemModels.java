package io.github.flemmli97.fateubw.neoforge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.client.ItemModelProps;
import io.github.flemmli97.fateubw.common.registry.FateItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        int sealid = 1;
        for (RegistryEntrySupplier<Item, ?> reg : FateItems.ITEMS.getEntries()) {
            if (reg == FateItems.ENUMAELISH || reg == FateItems.HERACLES_AXE)
                continue;
            if (reg == FateItems.MEDUSA_DAGGER) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("handheld"))
                        .texture("layer0", this.itemTexture(reg.getID()))
                        .override().predicate(ItemModelProps.THROWN_DAGGER_ID, 0)
                        .predicate(ItemModelProps.HELD_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_held", ModelLocationUtils.decorateItemModelLocation("handheld"))
                                .texture("layer0", this.itemTexture(reg.getID().getPath() + "_held"))).end()
                        .override().predicate(ItemModelProps.THROWN_DAGGER_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_thrown", ModelLocationUtils.decorateItemModelLocation("handheld"))
                                .texture("layer0", this.itemTexture(reg.getID().getPath() + "_thrown"))
                                .transforms().transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                                .rotation(0, 90, -25)
                                .translation(1.13f, 6.3f, 1.13f)
                                .scale(0.68f, 0.68f, 0.68f)
                                .end()
                                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                                .rotation(0, -90, 25)
                                .translation(1.13f, 6.3f, 1.13f)
                                .scale(0.68f, 0.68f, 0.68f)
                                .end()
                                .end());
            } else if (reg == FateItems.ARCHBOW) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", Fate.modRes("item/emiyas_bow"))
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.05f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 0)
                        .model(this.withExistingParent(reg.getID().getPath() + "_pull_0", Fate.modRes("item/" + reg.getID().getPath()))
                                .texture("layer0", Fate.modRes("item/emiyas_bow_pull_0"))).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.65f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 0)
                        .model(this.withExistingParent(reg.getID().getPath() + "_pull_1", Fate.modRes("item/" + reg.getID().getPath()))
                                .texture("layer0", Fate.modRes("item/emiyas_bow_pull_1"))).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.9f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 0)
                        .model(this.withExistingParent(reg.getID().getPath() + "_pull_2", Fate.modRes("item/" + reg.getID().getPath()))
                                .texture("layer0", Fate.modRes("item/emiyas_bow_pull_2"))).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.05f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_caladbolg_pull_0", Fate.modRes("item/" + reg.getID().getPath()))
                                .texture("layer0", Fate.modRes("item/emiyas_bow_caladbolg_pull_0"))).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.65f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_caladbolg_pull_1", Fate.modRes("item/" + reg.getID().getPath()))
                                .texture("layer0", Fate.modRes("item/emiyas_bow_caladbolg_pull_1"))).end()
                        .override().predicate(ItemModelProps.BOW_PULL_ID, 0.9f)
                        .predicate(ItemModelProps.CALADBOLG_ID, 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_caladbolg_pull_2", Fate.modRes("item/" + reg.getID().getPath()))
                                .texture("layer0", Fate.modRes("item/emiyas_bow_caladbolg_pull_2"))).end()
                        .transforms().transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                        .rotation(-80, 260, -40)
                        .translation(-1, -2, 2.5f)
                        .scale(0.9f, 0.9f, 0.9f)
                        .end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                        .rotation(-80, -280, 40)
                        .translation(-1, -2, 2.5f)
                        .scale(0.9f, 0.9f, 0.9f)
                        .end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                        .rotation(0, -90, 25)
                        .translation(1.13f, 3.2f, 1.13f)
                        .scale(0.68f, 0.68f, 0.68f)
                        .end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                        .rotation(0, 90, -25)
                        .translation(1.13f, 3.2f, 1.13f)
                        .scale(0.68f, 0.68f, 0.68f)
                        .end();
            } else if (reg == FateItems.GAEBOLG || reg == FateItems.GAEBUIDHE
                    || reg == FateItems.GAEDEARG) {
                this.withExistingParent(reg.getID().getPath(), Fate.modRes("item/spear_item"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg == FateItems.ARONDIGHT || reg == FateItems.MONOHOSHI_ZAO || reg == FateItems.STAFF) {
                this.withExistingParent(reg.getID().getPath(), Fate.modRes("item/32x32_weapon"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg.get() instanceof SpawnEgg)
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
            else if (reg == FateItems.INVISEXCALIBUR) {
                this.withExistingParent(reg.getID().getPath(), Fate.modRes("item/32x32_weapon"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg == FateItems.EXCALIBUR) {
                this.withExistingParent(reg.getID().getPath(), Fate.modRes("item/32x32_weapon"))
                        .texture("layer0", Fate.modRes("item/excalibur"))
                        .override().predicate(ItemModelProps.ACTIVE_ID, 1).model(this.getExistingFile(Fate.modRes("excalibur_active")));
            } else if (reg.get() instanceof BlockItem blockItem) {
                this.simpleBlockItem(blockItem.getBlock());
            } else if (reg.get() instanceof SwordItem) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("handheld"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg == FateItems.ICON_0 || reg == FateItems.ICON_1 || reg == FateItems.ICON_2 || reg == FateItems.ICON_3) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", Fate.modRes("item/command_seal_" + sealid));
                sealid++;
            } else if (reg == FateItems.CRYSTAL_RED || reg == FateItems.CRYSTAL_YELLOW || reg == FateItems.CRYSTAL_BLACK || reg == FateItems.CRYSTAL_BLUE || reg == FateItems.CRYSTAL_GREEN) {
                this.withExistingParent(reg.getID().getPath(), Fate.modRes("item/gem_shard"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg == FateItems.ANIMATION_DEBUG) {
                this.singleTexture(reg.getID().getPath(), this.mcLoc("item/generated"), "layer0", ResourceLocation.withDefaultNamespace("item/stick"));
            } else {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            }
        }
    }

    private ResourceLocation itemTexture(ResourceLocation item) {
        return ResourceLocation.fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath());
    }

    private ResourceLocation itemTexture(String texture) {
        return Fate.modRes("item/" + texture);
    }
}
