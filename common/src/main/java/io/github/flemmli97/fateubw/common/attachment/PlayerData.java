package io.github.flemmli97.fateubw.common.attachment;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.fateubw.common.entity.misc.ChainDagger;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.network.S2CCommandSeals;
import io.github.flemmli97.fateubw.common.network.S2CMana;
import io.github.flemmli97.fateubw.common.network.S2CPlayerCap;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class PlayerData {

    private int currentMana, commandSeals = 0;
    private int manaRegenCooldown = 100;
    private float manaRegenAccel = 1;

    private Pair<EntityType<?>, CompoundTag> savedServant;

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
            this.manaRegenAccel = 1;
            this.manaRegenCooldown = 160;
            if (player instanceof ServerPlayer serverPlayer)
                NetworkCalls.INSTANCE.sendToClient(new S2CMana(this), serverPlayer);
        }
        return flag;
    }

    public void tick(ServerPlayer player) {
        if (--this.manaRegenCooldown <= 0) {
            this.addMana(player, 1);
            this.manaRegenCooldown = (int) (120 / this.manaRegenAccel);
            this.manaRegenAccel = Math.min(this.manaRegenAccel + 0.5f, 10);
        }
    }

    public void saveServant(BaseServant servant) {
        if (servant != null) {
            servant.stopRiding();
            servant.ejectPassengers();
            CompoundTag nbt = new CompoundTag();
            servant.saveWithoutId(nbt);
            nbt.remove("Pos");
            nbt.remove("Motion");
            nbt.remove("Rotation");
            nbt.remove("UUID");
            this.savedServant = Pair.of(servant.getType(), nbt);
        }
    }

    public void restoreServant(Player player, boolean loot) {
        if (this.savedServant != null && (player.level instanceof ServerLevel serverLevel)) {
            if (loot) {
                ResourceLocation lootId = this.savedServant.getFirst().getDefaultLootTable();
                LootTable lootTable = serverLevel.getServer().getLootTables().get(lootId);
                LootContext.Builder builder = this.createLootContext(player);
                lootTable.getRandomItems(builder.create(LootContextParamSets.ENTITY), player::spawnAtLocation);
                this.savedServant = null;
            } else {
                Entity entity = this.savedServant.getFirst().create(serverLevel);
                if (entity instanceof BaseServant servant) {
                    servant.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(servant.blockPosition()), MobSpawnType.TRIGGERED, null, null);
                    entity.load(this.savedServant.getSecond());
                    Vec3 look = player.getLookAngle();
                    entity.setPos(player.getX() + look.x, player.getY(), player.getZ() + look.z);
                    servant.setOwner(player);
                    serverLevel.addFreshEntity(entity);
                    this.savedServant = null;
                }
            }
        }
    }

    private LootContext.Builder createLootContext(Player player) {
        DamageSource source = DamageSource.playerAttack(player);
        return new LootContext.Builder((ServerLevel) player.level).withRandom(player.getRandom())
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withParameter(LootContextParams.ORIGIN, player.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                .withOptionalParameter(LootContextParams.KILLER_ENTITY, source.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, source.getDirectEntity())
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
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
        if (this.savedServant != null) {
            compound.putString("SavedServantType", Registry.ENTITY_TYPE.getKey(this.savedServant.getFirst()).toString());
            compound.put("SavedServant", this.savedServant.getSecond());
        }
        return compound;
    }

    public void readFromNBT(CompoundTag compound) {
        this.currentMana = compound.getInt("Mana");
        this.commandSeals = compound.getInt("CommandSeal");
        if (compound.contains("SavedServantType")) {
            this.savedServant = Pair.of(Registry.ENTITY_TYPE.get(new ResourceLocation(compound.getString("SavedServantType"))),
                    compound.getCompound("SavedServant"));
        }
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