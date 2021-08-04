package io.github.flemmli97.fate.common.capability;

import com.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.fate.common.entity.EntityDaggerHook;
import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import io.github.flemmli97.fate.network.PacketHandler;
import io.github.flemmli97.fate.network.S2CCommandSeals;
import io.github.flemmli97.fate.network.S2CMana;
import io.github.flemmli97.fate.network.S2CPlayerCap;
import io.github.flemmli97.fate.network.S2CServantSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.UUID;
import java.util.function.Predicate;

public class PlayerCap implements ICapabilitySerializable<CompoundNBT> {

    private static final Predicate<EntityServant> notDead = t -> !t.getShouldBeDead();
    private final LazyOptional<PlayerCap> instance = LazyOptional.of(() -> this);

    private int currentMana = 0;

    private EntityServant servant;
    private UUID servantUUID = null;
    private CompoundNBT savedServant;

    private int commandSeals = 0;

    private EntityDaggerHook currentDagger;

    public PlayerCap() {
    }

    public void setMana(PlayerEntity player, int mana) {
        this.currentMana = Math.min(mana, 100);
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CMana(this), (ServerPlayerEntity) player);
    }

    public void addMana(PlayerEntity player, int amount) {
        this.setMana(player, this.currentMana + amount);
    }

    public int getMana() {
        return this.currentMana;
    }

    public boolean useMana(PlayerEntity player, int amount) {
        boolean flag = this.currentMana > amount;
        if (flag) {
            this.currentMana -= amount;
            if (player instanceof ServerPlayerEntity)
                PacketHandler.sendToClient(new S2CMana(this), (ServerPlayerEntity) player);
        }
        return flag;
    }

    public EntityServant getServant(PlayerEntity player) {
        if (this.servant == null && this.servantUUID != null) {
            EntityServant servant = EntityUtil.findFromUUID(EntityServant.class, player.world, this.servantUUID, notDead);
            if (servant != null)
                this.setServant(player, servant);
        }
        return this.servant;
    }

    public UUID getServantUUID() {
        return this.servantUUID;
    }

    public void setServant(PlayerEntity player, EntityServant servant) {
        this.servant = servant;
        if (servant != null)
            this.servantUUID = servant.getUniqueID();
        else
            this.servantUUID = null;
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CServantSync(servant), (ServerPlayerEntity) player);
    }

    public ITextComponent getServantName() {
        if (this.servant != null) {
            return this.servant.getName();
        }
        return new TranslationTextComponent("fate.servant.none");
    }

    public void saveServant(PlayerEntity player) {
        if (this.getServant(player) != null) {
            CompoundNBT nbt = new CompoundNBT();
            this.getServant(player).writeUnlessRemoved(nbt);
            this.savedServant = nbt;
            this.savedServant.remove("Pos");
            this.savedServant.remove("Motion");
            this.savedServant.remove("Rotation");
            this.savedServant.remove("UUIDMost");
            this.savedServant.remove("UUIDLeast");
            //Vanilla-fix incompability
            this.savedServant.remove("VFAABB");
        }
    }

    public void restoreServant(PlayerEntity player) {
        if (this.savedServant != null && !player.world.isRemote) {
            Entity e = EntityType.loadEntityAndExecute(this.savedServant, player.world, entity -> entity);
            if (e != null) {
                Vector3d look = player.getLookVec();
                e.setPosition(player.getPosX() + look.x, player.getPosY(), player.getPosZ() + look.z);
                player.world.addEntity(e);
            }
        }
    }

    public int getCommandSeals() {
        return this.commandSeals;
    }

    public boolean useCommandSeal(PlayerEntity player) {
        boolean flag = this.commandSeals > 0;
        if (flag) {
            this.commandSeals--;
            if (player instanceof ServerPlayerEntity)
                PacketHandler.sendToClient(new S2CCommandSeals(this), (ServerPlayerEntity) player);
        }
        return flag;
    }

    public void setCommandSeals(PlayerEntity player, int amount) {
        this.commandSeals = Math.min(amount, 3);
        if (player instanceof ServerPlayerEntity)
            PacketHandler.sendToClient(new S2CCommandSeals(this), (ServerPlayerEntity) player);
    }

    public void setThrownDagger(EntityDaggerHook hook) {
        this.currentDagger = hook;
    }

    public EntityDaggerHook getThrownDagger() {
        if (this.currentDagger != null && this.currentDagger.isAlive())
            return this.currentDagger;
        return null;
    }

    public CompoundNBT writeToNBT(CompoundNBT compound) {
        compound.putInt("Mana", this.currentMana);
        compound.putInt("CommandSeal", this.commandSeals);
        if (this.servant != null)
            compound.putUniqueId("ServantUUID", this.servant.getUniqueID());
        if (this.savedServant != null)
            compound.put("SavedServant", this.savedServant);
        return compound;
    }

    public void readFromNBT(CompoundNBT compound) {
        this.currentMana = compound.getInt("Mana");
        this.commandSeals = compound.getInt("CommandSeal");
        if (compound.contains("ServantUUID"))
            this.servantUUID = compound.getUniqueId("ServantUUID");
        if (compound.contains("SavedServant"))
            this.savedServant = compound.getCompound("SavedServant");
    }

    public void handleClientUpdatePacket(S2CPlayerCap pkt) {
        this.currentMana = pkt.manaValue;
        this.commandSeals = pkt.commandSeals;
        this.servantUUID = pkt.servantUUID;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return CapabilityInsts.PlayerCap.orEmpty(cap, this.instance);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.writeToNBT(new CompoundNBT());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.readFromNBT(nbt);
    }
}