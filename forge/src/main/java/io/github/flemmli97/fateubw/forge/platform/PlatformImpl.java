package io.github.flemmli97.fateubw.forge.platform;

import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.fateubw.forge.common.capability.CapabilityInsts;
import io.github.flemmli97.fateubw.forge.common.item.EAForge;
import io.github.flemmli97.fateubw.forge.common.item.ExcaliburForge;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.advancements.CriteriaTriggers;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.Event;

import java.util.Optional;

public class PlatformImpl implements Platform {

    @Override
    public Optional<PlayerData> getPlayerData(Player player) {
        return player.getCapability(CapabilityInsts.PLAYERCAP).resolve();
    }

    @Override
    public Optional<ItemStackData> getItemStackData(Object stack) {
        if (stack instanceof ItemStack itemStack)
            return itemStack.getCapability(CapabilityInsts.ITEMSTACKCAP).resolve();
        return Optional.empty();
    }

    @Override
    public boolean canSpawnEvent(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason, SpawnPlacements.Type place) {
        Event.Result canSpawn = net.minecraftforge.event.ForgeEventFactory.canEntitySpawn(entity, level, x, y, z, spawner, spawnReason);
        return canSpawn == Event.Result.ALLOW ||
                (canSpawn == Event.Result.DEFAULT && (NaturalSpawner.isSpawnPositionOk(place, level, entity.blockPosition(), entity.getType()) && entity.checkSpawnObstruction(level)));
    }

    @Override
    public float onLivingHurt(LivingEntity entity, DamageSource source, float amount) {
        return ForgeHooks.onLivingHurt(entity, source, amount);
    }

    @Override
    public float onLivingDamage(LivingEntity entity, DamageSource source, float amount) {
        return ForgeHooks.onLivingDamage(entity, source, amount);
    }

    @Override
    public <T extends CriterionTrigger<?>> T registerCriteriaTrigger(T criterion) {
        return CriteriaTriggers.register(criterion);
    }

    @Override
    public boolean mobGriefing(Entity entity) {
        return ForgeEventFactory.getMobGriefingEvent(entity.level, entity);
    }

    @Override
    public Item createExcalibur(Item.Properties props) {
        return new ExcaliburForge(props);
    }

    @Override
    public Item createEA(Item.Properties props) {
        return new EAForge(props);
    }

    @Override
    public AxeItem createAxe(Tier tier, float baseAttack, float speed, Item.Properties props) {
        return new AxeItem(tier, baseAttack, speed, props);
    }

    @Override
    public DamageSource setBypassArmor(DamageSource source) {
        return source.bypassArmor();
    }
}
