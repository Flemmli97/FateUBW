package io.github.flemmli97.fate.common.config;

import com.flemmli97.tenshilib.api.config.IConfigListValue;
import com.google.common.collect.Lists;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PotionEffectsConfig implements IConfigListValue<PotionEffectsConfig> {
    private Map<Effect, Pair<Integer, Integer>> potions;
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

    public EffectInstance[] potions() {
        if (this.potions == null) {
            this.potions = new HashMap<>();
            for (String p : this.confVal) {
                String[] sub = p.split(",");
                if (sub.length != 3)
                    continue;
                this.potions.put(ForgeRegistries.POTIONS.getValue(new ResourceLocation(sub[0])), Pair.of(Integer.parseInt(sub[1]), Integer.parseInt(sub[2])));
            }
        }
        EffectInstance[] effects = new EffectInstance[this.potions.size()];
        int i = 0;
        for (Map.Entry<Effect, Pair<Integer, Integer>> entry : this.potions.entrySet()) {
            Pair<Integer, Integer> p = entry.getValue();
            effects[i] = new EffectInstance(entry.getKey(), p.getLeft(), p.getRight());
            i++;
        }
        return effects;
    }
}