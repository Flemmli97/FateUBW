package io.github.flemmli97.fateubw.fabric.platform;

import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.common.items.weapons.EnumaElishItem;
import io.github.flemmli97.fateubw.common.items.weapons.ExcaliburItem;
import io.github.flemmli97.fateubw.fabric.common.data.ItemStackDataGet;
import io.github.flemmli97.fateubw.fabric.common.data.PlayerDataGet;
import io.github.flemmli97.fateubw.fabric.mixin.DamageSourceAccessor;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class PlatformImpl implements Platform {

    @Override
    public boolean isDatagen() {
        return false;
    }

    @Override
    public Optional<PlayerData> getPlayerData(Player player) {
        return Optional.ofNullable(((PlayerDataGet) player).fateubw$getData());
    }

    @Override
    public Optional<ItemStackData> getItemStackData(Object stack) {
        if (stack instanceof ItemStackDataGet getter)
            return Optional.ofNullable(getter.fateubw$getData());
        return Optional.empty();
    }

    @Override
    public boolean canSpawnEvent(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason, SpawnPlacements.Type place) {
        return true;
    }

    @Override
    public <T extends CriterionTrigger<?>> T registerCriteriaTrigger(T criterion) {
        return CriteriaTriggers.register(criterion);
    }

    @Override
    public Item createExcalibur(Item.Properties props) {
        return new ExcaliburItem(props);
    }

    @Override
    public Item createEA(Item.Properties props) {
        return new EnumaElishItem(props);
    }

    @Override
    public AxeItem createAxe(Tier tier, float baseAttack, float speed, Item.Properties props) {
        return createNewAxe(tier, baseAttack, speed, props);
    }

    @Override
    public DamageSource setBypassArmor(DamageSource source) {
        return ((DamageSourceAccessor) source).setBypassArmor();
    }

    @Override
    public DamageSource setBypassMagic(DamageSource source) {
        return ((DamageSourceAccessor) source).setBypassMagic();
    }

    @Override
    public AbstractArrow customBowArrow(BowItem item, AbstractArrow def) {
        return def;
    }

    private static AxeItem createNewAxe(Tier tier, float baseAttack, float speed, Item.Properties props) {
        try {
            Constructor<AxeItem> cons = AxeItem.class.getDeclaredConstructor(Tier.class, float.class, float.class, Item.Properties.class);
            cons.setAccessible(true);
            return cons.newInstance(tier, baseAttack, speed, props);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
