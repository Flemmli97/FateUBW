package io.github.flemmli97.fateubw.common.entity.servant;


import io.github.flemmli97.fateubw.common.config.Config;
import io.github.flemmli97.fateubw.common.entity.minions.HassanClone;
import io.github.flemmli97.fateubw.common.entity.servant.ai.HassanAttackGoal;
import io.github.flemmli97.fateubw.common.lib.LibEntities;
import io.github.flemmli97.fateubw.common.registry.ModItems;
import io.github.flemmli97.fateubw.common.utils.EnumServantUpdate;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EntityHassan extends BaseServant {

    public final HassanAttackGoal attackAI = new HassanAttackGoal(this);
    private static final AnimatedAction npAttack = new AnimatedAction(20, 1, "np");
    private static final AnimatedAction[] anims = {AnimatedAction.vanillaAttack, npAttack};

    private final AnimationHandler<EntityHassan> animationHandler = new AnimationHandler<>(this, anims);

    private Set<UUID> copies = new HashSet<>();

    public EntityHassan(EntityType<? extends EntityHassan> entityType, Level world) {
        super(entityType, world, LibEntities.hassan + ".hogou");
        if (world != null && !world.isClientSide)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.assassinDagger.get()));
    }

    @Override
    public boolean canUse(AnimatedAction anim, AttackType type) {
        if (type == AttackType.NP)
            return anim.getID().equals(npAttack.getID());
        return anim.getID().equals("vanilla");
    }

    @Override
    public boolean canUseNP() {
        return super.canUseNP() && this.copies.isEmpty();
    }

    @Override
    public AnimationHandler<EntityHassan> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
    }

    public void removeCopy(HassanClone copy) {
        this.copies.remove(copy.getUUID());
    }

    public boolean addCopy(HassanClone copy) {
        if (this.copies.size() < Config.Common.hassanCopies) {
            this.copies.add(copy.getUUID());
            return true;
        }
        return false;
    }

    public List<HassanClone> gatherCopies() {
        ArrayList<HassanClone> list = new ArrayList<>();
        for (HassanClone e : this.level.getEntitiesOfClass(HassanClone.class, this.getBoundingBox().inflate(32))) {
            if (this.copies.contains(e.getUUID())) {
                e.setOriginal(this);
                list.add(e);
            }
        }
        return list;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.dead && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void attackWithNP() {
        if (this.gatherCopies().isEmpty()) {
            this.copies.clear();
            for (int i = 0; i < Config.Common.hassanCopies; i++) {
                HassanClone hassan = new HassanClone(this.level, this);
                hassan.moveTo(this.getX(), this.getY(), this.getZ(), Mth.wrapDegrees(this.level.random.nextFloat() * 360.0F), 0.0F);
                hassan.finalizeSpawn((ServerLevelAccessor) this.level, this.level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                this.level.addFreshEntity(hassan);
                this.addCopy(hassan);
            }
            this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 3600, 1, true, false));
            this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2, true, false));
            if (this.getTarget() instanceof Mob)
                ((Mob) this.getTarget()).setTarget(null);
            this.revealServant();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ListTag copies = new ListTag();
        this.copies.forEach(hassan -> copies.add(NbtUtils.createUUID(hassan)));
        tag.put("Copies", copies);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        tag.getList("Copies", Tag.TAG_INT_ARRAY).forEach(nbt -> this.copies.add(NbtUtils.loadUUID(nbt)));
    }
}