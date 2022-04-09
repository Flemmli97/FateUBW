package io.github.flemmli97.fateubw.common.attachment;

import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CCommandSeals;
import io.github.flemmli97.fateubw.common.network.S2CMana;
import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.Predicate;

public class PlayerData {

    private static final Predicate<BaseServant> notDead = t -> !t.isDeadOrDying();

    private int currentMana, commandSeals = 0;

    private BaseServant servant;
    private UUID servantUUID = null;

    private CompoundTag savedServant;

    private ChainDagger currentDagger;

    public PlayerData() {
    }

    public void setMana(Player player, int mana) {
        this.currentMana = Math.min(mana, 100);
        if (player instanceof ServerPlayer serverPlayer)
            NetworkCalls.INSTANCE.sendToClient(new S2CMana(this), serverPlayer);
    }

    public void addMana(Player player, int amount) {
        this.setMana(player, this.currentMana + amount);
    }

    public int getMana() {
        return this.currentMana;
    }

    public boolean useMana(Player player, int amount) {
        boolean flag = this.currentMana >= amount;
        if (flag) {
            this.currentMana -= amount;
            if (player instanceof ServerPlayer serverPlayer)
                NetworkCalls.INSTANCE.sendToClient(new S2CMana(this), serverPlayer);
        }
        return flag;
    }

    public BaseServant getServant(Player player) {
        return this.getServant(player.level);
    }

    private BaseServant getServant(Level level) {
        if (this.servant != null) {
            if (this.servant.isAlive())
                return this.servant;
            this.setServant(null);
        }
        if (this.servantUUID != null) {
            BaseServant servant = EntityUtil.findFromUUID(BaseServant.class, level, this.servantUUID, notDead);
            if (servant != null) {
                if (!servant.isAlive())
                    this.setServant(null);
                else
                    this.setServant(servant);
            }
        }
        return this.servant;
    }

    public UUID getServantUUID() {
        return this.servantUUID;
    }

    public void setServant(BaseServant servant) {
        this.servant = servant;
        this.servantUUID = servant == null ? null : servant.getUUID();
    }

    public Component getServantName(Level level) {
        if (this.getServant(level) != null) {
            return this.getServant(level).getName();
        }
        return new TranslatableComponent("fate.servant.none");
    }

    public void saveServant(Player player) {
        if (this.getServant(player) != null) {
            CompoundTag nbt = new CompoundTag();
            this.getServant(player).saveAsPassenger(nbt);
            this.savedServant = nbt;
            this.savedServant.remove("Pos");
            this.savedServant.remove("Motion");
            this.savedServant.remove("Rotation");
            this.savedServant.remove("UUIDMost");
            this.savedServant.remove("UUIDLeast");
        }
    }

    public void restoreServant(Player player) {
        if (this.savedServant != null && !player.level.isClientSide) {
            Entity e = EntityType.loadEntityRecursive(this.savedServant, player.level, entity -> entity);
            if (e != null) {
                Vec3 look = player.getLookAngle();
                e.setPos(player.getX() + look.x, player.getY(), player.getZ() + look.z);
                player.level.addFreshEntity(e);
                this.savedServant = null;
            }
        }
    }

    public int getCommandSeals() {
        return this.commandSeals;
    }

    public boolean useCommandSeal(Player player) {
        boolean flag = this.commandSeals > 0;
        if (flag) {
            this.commandSeals--;
            if (player instanceof ServerPlayer serverPlayer)
                NetworkCalls.INSTANCE.sendToClient(new S2CCommandSeals(this), serverPlayer);
        }
        return flag;
    }

    public void setCommandSeals(Player player, int amount) {
        this.commandSeals = Math.min(amount, 3);
        if (player instanceof ServerPlayer serverPlayer)
            NetworkCalls.INSTANCE.sendToClient(new S2CCommandSeals(this), serverPlayer);
    }

    public void setThrownDagger(ChainDagger hook) {
        this.currentDagger = hook;
    }

    public ChainDagger getThrownDagger() {
        if (this.currentDagger != null && this.currentDagger.isAlive())
            return this.currentDagger;
        return null;
    }

    public CompoundTag writeToNBT(CompoundTag compound) {
        compound.putInt("Mana", this.currentMana);
        compound.putInt("CommandSeal", this.commandSeals);
        if (this.servantUUID != null)
            compound.putUUID("ServantUUID", this.servantUUID);
        if (this.savedServant != null)
            compound.put("SavedServant", this.savedServant);
        return compound;
    }

    public void readFromNBT(CompoundTag compound) {
        this.currentMana = compound.getInt("Mana");
        this.commandSeals = compound.getInt("CommandSeal");
        if (compound.hasUUID("ServantUUID"))
            this.servantUUID = compound.getUUID("ServantUUID");
        if (compound.contains("SavedServant"))
            this.savedServant = compound.getCompound("SavedServant");
    }

    public void from(PlayerData other) {
        this.currentMana = other.currentMana;
        this.commandSeals = other.commandSeals;
        this.servantUUID = other.servantUUID;
        this.savedServant = other.savedServant;
    }

    public void handleClientUpdatePacket(S2CPlayerCap pkt) {
        this.currentMana = pkt.manaValue;
        this.commandSeals = pkt.commandSeals;
        this.servantUUID = pkt.servantUUID;
    }
}