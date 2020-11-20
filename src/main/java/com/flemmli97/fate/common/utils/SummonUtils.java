package com.flemmli97.fate.common.utils;

import com.flemmli97.fate.common.blocks.BlockChalkLine;
import com.flemmli97.fate.common.blocks.tile.TileAltar;
import com.flemmli97.fate.common.entity.servant.EntityServant;
import com.flemmli97.fate.common.items.ItemServantCharm;
import com.flemmli97.fate.common.registry.ModBlocks;
import com.flemmli97.fate.common.registry.ModEntities;
import com.flemmli97.fate.common.world.GrailWarHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;

public class SummonUtils {

    public static void placeSummoningStructure(ServerWorld world, BlockPos pos, TileAltar tile, Direction facing) {
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos posNew = pos.add(x, 0, z);
                    if (!(world.getBlockState(posNew).getBlock() instanceof BlockChalkLine))
                        return;
                }
            }
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    world.setBlockState(newPos, ModBlocks.chalks.get(getChalkPos(x, z, facing)).get().getDefaultState().with(BlockChalkLine.FACING, facing));
                    world.spawnParticle(ParticleTypes.CLOUD, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, 1, 0, 0.2, 0, 0);
                }
            }
        tile.setComplete(true);
    }

    public static boolean checkStructure(World world, BlockPos pos, Direction facing) {
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                for (int y = 0; y < 2; y++) {
                    if (x != 0 || z != 0 || y != 0) {
                        BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        BlockState state = world.getBlockState(newPos);
                        if (y == 0 && (!(state.getBlock() instanceof BlockChalkLine) || ((BlockChalkLine) state.getBlock()).pos != getChalkPos(x, z, state.get(BlockChalkLine.FACING))))
                            return false;
                        else if (y == 1 && state.getBlock() != Blocks.AIR) {
                            return false;
                        }
                    }
                }
        return true;
    }

    private static EnumPositionChalk getChalkPos(int x, int z, Direction facing) {
        //default to north
        int column = x + 2;
        int row = z + 2;
        if (facing == Direction.NORTH) {
            column = x + 2;
            row = z + 2;
        } else if (facing == Direction.WEST) {
            column = -z + 2;
            row = x + 2;
        } else if (facing == Direction.SOUTH) {
            column = -x + 2;
            row = -z + 2;
        } else if (facing == Direction.EAST) {
            column = z + 2;
            row = -x + 2;
        }
        return EnumPositionChalk.fromPos(column + row * 5);
    }

    public static void removeSummoningStructure(World world, BlockPos pos) {
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos posNew = pos.add(x, 0, z);
                    if (world.getBlockState(posNew).getBlock() instanceof BlockChalkLine)
                        world.destroyBlock(posNew, false);
                }
            }
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 0.4F, 1F);
        world.removeTileEntity(pos);
        world.destroyBlock(pos, false);
    }

    public static void summonServant(EntityServant servant, ServerPlayerEntity player, ServerWorld world, BlockPos pos) {
        servant.setOwner(player);
        servant.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
        servant.onInitialSpawn(world, world.getDifficultyForLocation(servant.getBlockPos()), SpawnReason.TRIGGERED, null, null);
        world.addEntity(servant);
        GrailWarHandler.get(world).join(player);
    }

    public static void summonRandomServant(ItemStack stack, ServerPlayerEntity player, BlockPos pos, ServerWorld world) {
        EnumServantType type = EnumServantType.values()[player.getRNG().nextInt(EnumServantType.values().length - 1)];
        if (stack != null && player.getRNG().nextFloat() <= 0.6 && stack.getItem() instanceof ItemServantCharm)
            type = ((ItemServantCharm) stack.getItem()).type;
        List<RegistryObject<EntityType<EntityServant>>> entities = ModEntities.getFromType(type);
        if (entities.size() == 0)
            return;
        EntityServant entity = entities.get(player.getRNG().nextInt(entities.size())).get().create(world);
        if (GrailWarHandler.get(world).canSpawnServant(entity))
            summonServant(entity, player, world, pos);
        else
            summonRandomServant(stack, player, pos, world);
    }

    public static EntityServant randomServant(ServerWorld world) {
        EnumServantType type = EnumServantType.values()[world.rand.nextInt(EnumServantType.values().length - 1)];
        List<RegistryObject<EntityType<EntityServant>>> entities = ModEntities.getFromType(type);
        if (entities.size() == 0)
            return null;
        EntityServant entity = entities.get(world.rand.nextInt(entities.size())).get().create(world);
        if (GrailWarHandler.get(world).canSpawnServant(entity))
            return entity;
        else
            return randomServant(world);
    }
}