package io.github.flemmli97.fate.common.entity.servant;

import com.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.fate.common.config.Config;
import io.github.flemmli97.fate.common.entity.EntityHassanCopy;
import io.github.flemmli97.fate.common.entity.servant.ai.HassanAttackGoal;
import io.github.flemmli97.fate.common.registry.ModItems;
import io.github.flemmli97.fate.common.utils.EnumServantUpdate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class EntityHassan extends EntityServant {

    public final HassanAttackGoal attackAI = new HassanAttackGoal(this);
    private static final AnimatedAction npAttack = new AnimatedAction(20, 1, "np");
    private static final AnimatedAction[] anims = new AnimatedAction[]{AnimatedAction.vanillaAttack, npAttack};

    private Set<UUID> copies = new HashSet<>();

    public EntityHassan(EntityType<? extends EntityHassan> entityType, World world) {
        super(entityType, world, "hassan.hogou");
        if (world != null && !world.isRemote)
            this.goalSelector.addGoal(0, this.attackAI);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.assassinDagger.get()));
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
    public AnimatedAction[] getAnimations() {
        return anims;
    }

    @Override
    public void updateAI(EnumServantUpdate behaviour) {
        super.updateAI(behaviour);
        if (this.commandBehaviour == EnumServantUpdate.STAY)
            this.goalSelector.removeGoal(this.attackAI);
        else
            this.goalSelector.addGoal(0, this.attackAI);
    }

    public void removeCopy(EntityHassanCopy copy) {
        this.copies.remove(copy.getUniqueID());
    }

    public boolean addCopy(EntityHassanCopy copy) {
        if (this.copies.size() < Config.Common.hassanCopies) {
            this.copies.add(copy.getUniqueID());
            return true;
        }
        return false;
    }

    public List<EntityHassanCopy> gatherCopies() {
        ArrayList<EntityHassanCopy> list = new ArrayList<>();
        for (EntityHassanCopy e : this.world.getEntitiesWithinAABB(EntityHassanCopy.class, this.getBoundingBox().grow(32))) {
            if (this.copies.contains(e.getUniqueID())) {
                e.setOriginal(this);
                list.add(e);
            }
        }
        return list;
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        super.damageEntity(damageSrc, damageAmount);
        if (!this.dead && this.getHealth() < 0.5 * this.getMaxHealth()) {
            this.canUseNP = true;
        }
    }

    public void attackWithNP() {
        if (this.gatherCopies().isEmpty()) {
            this.copies.clear();
            for (int i = 0; i < Config.Common.hassanCopies; i++) {
                EntityHassanCopy hassan = new EntityHassanCopy(this.world, this);
                hassan.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), MathHelper.wrapDegrees(this.world.rand.nextFloat() * 360.0F), 0.0F);
                hassan.onInitialSpawn((IServerWorld) this.world, this.world.getDifficultyForLocation(this.getPosition()), SpawnReason.MOB_SUMMONED, null, null);
                this.world.addEntity(hassan);
                this.addCopy(hassan);
            }
            this.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 3600, 1, true, false));
            this.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 100, 2, true, false));
            if (this.getAttackTarget() instanceof MobEntity)
                ((MobEntity) this.getAttackTarget()).setAttackTarget(null);
            this.revealServant();
        }
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        ListNBT copies = new ListNBT();
        this.copies.forEach(hassan -> copies.add(NBTUtil.func_240626_a_(hassan)));
        tag.put("Copies", copies);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        tag.getList("Copies", Constants.NBT.TAG_INT_ARRAY).forEach(nbt -> this.copies.add(NBTUtil.readUniqueId(nbt)));
    }
}