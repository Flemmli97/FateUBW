package io.github.flemmli97.fateubw.common.attachment;

import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CCommandSeals;
import io.github.flemmli97.fateubw.common.network.S2CMana;
import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class PlayerData {

    private static final Predicate<BaseServant> NOT_DEAD = t -> !t.isDeadOrDying();

    private int currentMana, commandSeals = 0;

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

    public void saveServant(ServerPlayer player) {
        //TODO: Needs rework
        GrailWarHandler tracker = GrailWarHandler.get(player.getServer());
        if (tracker.getServant(player) != null) {
            CompoundTag nbt = new CompoundTag();
            tracker.getServant(player).saveAsPassenger(nbt);
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
        if (this.savedServant != null)
            compound.put("SavedServant", this.savedServant);
        return compound;
    }

    public void readFromNBT(CompoundTag compound) {
        this.currentMana = compound.getInt("Mana");
        this.commandSeals = compound.getInt("CommandSeal");
        if (compound.contains("SavedServant"))
            this.savedServant = compound.getCompound("SavedServant");
    }

    public void from(PlayerData other) {
        this.currentMana = other.currentMana;
        this.commandSeals = other.commandSeals;
        this.savedServant = other.savedServant;
    }

    public void handleClientUpdatePacket(S2CPlayerCap pkt) {
        this.currentMana = pkt.manaValue;
        this.commandSeals = pkt.commandSeals;
    }
}