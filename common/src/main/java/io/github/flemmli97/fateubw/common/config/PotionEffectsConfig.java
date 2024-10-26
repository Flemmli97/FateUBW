package io.github.flemmli97.fateubw.common.config;

import com.google.common.collect.Lists;
import io.github.flemmli97.tenshilib.api.config.IConfigListValue;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class PotionEffectsConfig implements IConfigListValue<PotionEffectsConfig> {

    private List<EffectInstance> potions = new ArrayList<>();
    private List<String> confVal;

    public PotionEffectsConfig() {
    }

    public PotionEffectsConfig(List<EffectInstance> potions) {
        this.potions = potions;
    }

    @Override
    public PotionEffectsConfig readFromString(List<String> s) {
        this.confVal = Lists.newArrayList(s);
        this.potions = null;
        return this;
    }

    @Override
    public List<String> writeToString() {
        if (this.confVal == null) {
            if (this.potions != null)
                this.confVal = this.potions.stream().map(eff -> String.format("%s,%s,%s", Registry.MOB_EFFECT.getKey(eff.effect), eff.amplifier, eff.duration)).toList();
            else
                this.confVal = new ArrayList<>();
        }
        return Lists.newArrayList(this.confVal);
    }

    public static String usage() {
        return "<registry name>,<duration>,<amplifier>";
    }

    public MobEffectInstance[] potions() {
        if (this.potions == null) {
            if (this.confVal == null)
                return new MobEffectInstance[0];
            this.potions = new ArrayList<>();
            for (String p : this.confVal) {
                String[] sub = p.split(",");
                if (sub.length != 3)
                    continue;
                this.potions.add(new EffectInstance(Registry.MOB_EFFECT.get(new ResourceLocation(sub[0])),
                        Integer.parseInt(sub[1]), Integer.parseInt(sub[2])));
            }
        }
        MobEffectInstance[] effects = new MobEffectInstance[this.potions.size()];
        int i = 0;
        for (EffectInstance effectInstance : this.potions) {
            effects[i] = new MobEffectInstance(effectInstance.effect(), effectInstance.duration(), effectInstance.amplifier());
            i++;
        }
        return effects;
    }

    public record EffectInstance(MobEffect effect, int duration, int amplifier) {
    }
}