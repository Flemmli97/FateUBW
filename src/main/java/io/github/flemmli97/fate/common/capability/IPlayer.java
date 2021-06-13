package io.github.flemmli97.fate.common.capability;

import io.github.flemmli97.fate.common.entity.servant.EntityServant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

public interface IPlayer {

    //Mana
    int getMana();

    void setMana(PlayerEntity player, int f);

    void addMana(PlayerEntity player, int amount);

    boolean useMana(PlayerEntity player, int amount);

    //Servant

    EntityServant getServant(PlayerEntity player);

    void setServant(PlayerEntity player, EntityServant servant);

    ITextComponent getServantName();

    void saveServant(PlayerEntity player);

    void restoreServant(PlayerEntity player);

    //Command seals

    int getCommandSeals();

    boolean useCommandSeal(PlayerEntity player);

    void setCommandSeals(PlayerEntity player, int amount);

    //NBT

    CompoundNBT writeToNBT(CompoundNBT compound);

    void readFromNBT(CompoundNBT compound);
}
