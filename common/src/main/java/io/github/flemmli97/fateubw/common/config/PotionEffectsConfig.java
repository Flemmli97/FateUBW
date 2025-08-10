package io.github.flemmli97.fateubw.common.config;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public class PotionEffectsConfig {

    private List<EffectInstance> potions;
    private List<String> confVal;

    public PotionEffectsConfig(List<EffectInstance> potions) {
        this.potions = potions;
    }

    public PotionEffectsConfig read(List<String> config) {
        this.confVal = Lists.newArrayList(config);
        this.potions = null;
        return this;
    }

    public List<String> write() {
        if (this.confVal == null) {
            if (this.potions != null)
                this.confVal = this.potions.stream().map(eff -> String.format("%s,%s,%s", Registry.MOB_EFFECT.getKey(eff.effect), eff.duration, eff.amplifier)).toList();
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

    public record EffectInstance(Holder<MobEffect> effect, int duration, int amplifier) {
    }
}