package io.github.flemmli97.fateubw.fabric.platform;

import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.items.weapons.ItemEA;
import io.github.flemmli97.fateubw.common.items.weapons.ItemExcalibur;
import io.github.flemmli97.fateubw.fabric.common.data.ItemStackDataGet;
import io.github.flemmli97.fateubw.fabric.common.data.PlayerDataGet;
import io.github.flemmli97.fateubw.fabric.mixin.AxeAccessor;
import io.github.flemmli97.fateubw.fabric.mixin.CriteriaTriggerAccessor;
import io.github.flemmli97.fateubw.fabric.mixin.DamageSourceAccessor;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;

public class PlatformImpl implements Platform {

    @Override
    public Optional<PlayerData> getPlayerData(Player player) {
        return Optional.ofNullable(((PlayerDataGet) player).getData());
    }

    @Override
    public Optional<ItemStackData> getItemStackData(Object stack) {
        if (stack instanceof ItemStackDataGet getter)
            return Optional.ofNullable(getter.getData());
        return Optional.empty();
    }

    @Override
    public boolean canSpawnEvent(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason, SpawnPlacements.Type place) {
        return true;
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        return amount;
    }

    @Override
    public float onLivingDamage(LivingEntity entity, DamageSource source, float amount) {
        return amount;
    }

    @Override
    public <T extends CriterionTrigger<?>> T registerCriteriaTrigger(T criterion) {
        return CriteriaTriggerAccessor.registerCriteria(criterion);
    }

    @Override
    public boolean mobGriefing(Entity entity) {
        return false;
    }

    @Override
    public Item createExcalibur(Item.Properties props) {
        return new ItemExcalibur(props);
    }

    @Override
    public Item createEA(Item.Properties props) {
        return new ItemEA(props);
    }

    @Override
    public AxeItem createAxe(Tier tier, float baseAttack, float speed, Item.Properties props) {
        return AxeAccessor.inst(tier, baseAttack, speed, props);
    }

    @Override
    public DamageSource setBypassArmor(DamageSource source) {
        return ((DamageSourceAccessor) source).setBypassArmor();
    }
}
