package io.github.flemmli97.fate.data;

import com.flemmli97.tenshilib.common.item.SpawnEgg;
import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.registry.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Fate.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> reg : ModItems.ITEMS.getEntries()) {
            if (reg.get() instanceof SpawnEgg)
                this.withExistingParent(reg.getId().getPath(), ModelsResourceUtil.func_240224_b_("template_spawn_egg"));
            else if (reg == ModItems.excalibur)
                this.withExistingParent(reg.getId().getPath(), ModelsResourceUtil.func_240224_b_("handheld"))
                        .texture("layer0", "fateubw:items/excalibur")
                        .override().predicate(new ResourceLocation(Fate.MODID, "active"), 1).model(this.getExistingFile(new ResourceLocation(Fate.MODID, "active_item_handheld")));
        }
    }
}
