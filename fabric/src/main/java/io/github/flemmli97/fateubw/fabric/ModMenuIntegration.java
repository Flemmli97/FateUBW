package io.github.flemmli97.fateubw.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.fabric.common.config.ConfigSpecs;
import io.github.flemmli97.tenshilib.common.config.ClothConfigScreenHelper;

import java.util.List;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ClothConfigScreenHelper.configScreenOf(parent, Fate.MODID, List.of(ConfigSpecs.clientSpec, ConfigSpecs.commonSpec));
    }
}
