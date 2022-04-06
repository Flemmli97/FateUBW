package io.github.flemmli97.fateubw.common.config;

import com.google.common.collect.Lists;
import io.github.flemmli97.tenshilib.api.config.IConfigListValue;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionEffectsConfig implements IConfigListValue<PotionEffectsConfig> {
    private Map<MobEffect, Pair<Integer, Integer>> potions;
    private List<String> confVal = new ArrayList<>();

    @Override
    public PotionEffectsConfig readFromString(List<String> s) {
        this.confVal = Lists.newArrayList(s);
        this.potions = null;
        return this;
    }

    @Override
    public List<String> writeToString() {
        return Lists.newArrayList(this.confVal);
    }

    public static String usage() {
        return "<registry name>,<duration>,<amplifier>";
    }

    public MobEffectInstance[] potions() {
        if (this.potions == null) {
            this.potions = new HashMap<>();
            for (String p : this.confVal) {
                String[] sub = p.split(",");
                if (sub.length != 3)
                    continue;
                this.potions.put(PlatformUtils.INSTANCE.effects().getFromId(new ResourceLocation(sub[0])), Pair.of(Integer.parseInt(sub[1]), Integer.parseInt(sub[2])));
            }
        }
        MobEffectInstance[] effects = new MobEffectInstance[this.potions.size()];
        int i = 0;
        for (Map.Entry<MobEffect, Pair<Integer, Integer>> entry : this.potions.entrySet()) {
            Pair<Integer, Integer> p = entry.getValue();
            effects[i] = new MobEffectInstance(entry.getKey(), p.getLeft(), p.getRight());
            i++;
        }
        return effects;
    }
}