package io.github.flemmli97.fateubw.forge.data;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.items.weapons.ClassSpear;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        int sealid = 1;
        for (RegistryEntrySupplier<Item> reg : ModItems.ITEMS.getEntries()) {
            //Archer bow
            if (reg == ModItems.enumaelish || reg == ModItems.heraclesAxe || reg == ModItems.archbow)
                continue;
            if (reg == ModItems.medusaDagger) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("handheld"))
                        .texture("layer0", this.itemTexture(reg.getID()))
                        .override().predicate(new ResourceLocation(Fate.MODID, "thrown"), 1)
                        .model(this.withExistingParent(reg.getID().getPath() + "_thrown", ModelLocationUtils.decorateItemModelLocation("handheld"))
                                .texture("layer0", this.itemTexture(reg.getID().getPath() + "_thrown"))
                                .transforms().transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT)
                                .rotation(0, 90, -25)
                                .translation(1.13f, 6.3f, 1.13f)
                                .scale(0.68f, 0.68f, 0.68f)
                                .end()
                                .transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
                                .rotation(0, -90, 25)
                                .translation(1.13f, 6.3f, 1.13f)
                                .scale(0.68f, 0.68f, 0.68f)
                                .end()
                                .end());
            } else if (reg.get() instanceof SpawnEgg)
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
            else if (reg == ModItems.excalibur) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("handheld"))
                        .texture("layer0", "fateubw:items/excalibur")
                        .override().predicate(new ResourceLocation(Fate.MODID, "active"), 1).model(this.getExistingFile(new ResourceLocation(Fate.MODID, "active_item_handheld")));
            } else if (reg.get() instanceof BlockItem blockItem) {
                this.withExistingParent(reg.getID().getPath(), new ResourceLocation(blockItem.getBlock().getRegistryName().getNamespace(), "block/" + blockItem.getBlock().getRegistryName().getPath()));
            } else if (reg.get() instanceof SwordItem || reg.get() instanceof ClassSpear || reg == ModItems.staff) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("handheld"))
                        .texture("layer0", this.itemTexture(reg.getID()));
            } else if (reg == ModItems.icon0 || reg == ModItems.icon1 || reg == ModItems.icon2 || reg == ModItems.icon3) {
                this.withExistingParent(reg.getID().getPath(), ModelLocationUtils.decorateItemModelLocation("generated"))
                        .texture("layer0", new ResourceLocation(reg.getID().getNamespace(), "gui/command_seal_" + sealid));
                sealid++;
            } else if (reg == ModItems.crystalFire || reg == ModItems.crystalEarth || reg == ModItems.crystalVoid || reg == ModItems.crystalWater || reg == ModItems.crystalWind) {
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
