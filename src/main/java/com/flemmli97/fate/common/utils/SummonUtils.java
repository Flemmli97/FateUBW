package com.flemmli97.fate.common.utils;

import com.flemmli97.fate.common.blocks.tile.TileAltar;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.grail.GrailWarHandler;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SummonUtils {

    public static void placeSummoningStructure(World world, BlockPos pos, TileAltar tile, Direction facing) {

    }

    public static boolean checkStructure(World world, BlockPos pos, Direction facing) {
        return false;
    }

    public static void removeSummoningStructure(World world, BlockPos pos) {

    }

    public static void summonServant(EntityServant servant, ServerPlayerEntity player, ServerWorld world, BlockPos pos) {
        servant.setOwner(player);
        servant.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
        servant.onInitialSpawn(world, world.getDifficultyForLocation(servant.getBlockPos()), SpawnReason.TRIGGERED, null, null);
        world.addEntity(servant);
        GrailWarHandler.get(world).join(player);
    }

    public static void summonRandomServant(ItemStack stack, ServerPlayerEntity player, BlockPos pos, World world) {

    }
}