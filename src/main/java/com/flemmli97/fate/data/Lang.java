package com.flemmli97.fate.data;

import com.flemmli97.fate.Fate;
import com.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class Lang extends LanguageProvider {

    public Lang(DataGenerator gen) {
        super(gen, Fate.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (SpawnEgg egg : SpawnEgg.getEggs())
            this.add(egg, "%s" + " Spawn Egg");
    }
}
