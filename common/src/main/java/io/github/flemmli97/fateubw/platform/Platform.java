package io.github.flemmli97.fateubw.platform;

import io.github.flemmli97.fateubw.common.attachment.ItemStackData;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.tenshilib.platform.InitUtil;
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

public interface Platform {

    Platform INSTANCE = InitUtil.getPlatformInstance(Platform.class,
            "io.github.flemmli97.fateubw.fabric.platform.PlatformImpl",
            "io.github.flemmli97.fateubw.forge.platform.PlatformImpl");

    Optional<PlayerData> getPlayerData(Player player);

    /**
     * Using Object cause ItemStack class is final.
     * For mixin casting on fabric
     */
    Optional<ItemStackData> getItemStackData(Object stack);

    boolean canSpawnEvent(Mob entity, LevelAccessor level, double x, double y, double z, BaseSpawner spawner, MobSpawnType spawnReason, SpawnPlacements.Type place);

    float onLivingHurt(LivingEntity entity, DamageSource source, float amount);

    float onLivingDamage(LivingEntity entity, DamageSource source, float amount);

    <T extends CriterionTrigger<?>> T registerCriteriaTrigger(T criterion);

    boolean mobGriefing(Entity entity);

    Item createExcalibur(Item.Properties props);

    Item createEA(Item.Properties props);

    AxeItem createAxe(Tier tier, float baseAttack, float speed, Item.Properties props);

    DamageSource setBypassArmor(DamageSource source);

}
