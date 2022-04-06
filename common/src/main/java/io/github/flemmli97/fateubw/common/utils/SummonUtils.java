package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.fateubw.common.blocks.ChalkBlock;
import io.github.flemmli97.fateubw.common.blocks.tile.AltarBlockEntity;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.items.ItemServantCharm;
import io.github.flemmli97.fateubw.common.registry.ModBlocks;
import io.github.flemmli97.fateubw.common.registry.ModEntities;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.List;

public class SummonUtils {

    public static void placeSummoningStructure(ServerLevel world, BlockPos pos, AltarBlockEntity tile, Direction facing) {
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos posNew = pos.offset(x, 0, z);
                    if (!(world.getBlockState(posNew).getBlock() instanceof ChalkBlock))
                        return;
                }
            }
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    world.setBlockAndUpdate(newPos, ModBlocks.chalk.get().defaultBlockState().setValue(ChalkBlock.FACING, facing).setValue(ChalkBlock.POSITION, getChalkPos(x, z, facing)));
                    world.sendParticles(ParticleTypes.CLOUD, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, 1, 0, 0.2, 0, 0);
                }
            }
        tile.setComplete(true);
    }

    public static boolean checkStructure(Level world, BlockPos pos, Direction facing) {
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++)
                for (int y = 0; y < 2; y++) {
                    if (x != 0 || z != 0 || y != 0) {
                        BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                        BlockState state = world.getBlockState(newPos);
                        if (y == 0 && (!(state.getBlock() instanceof ChalkBlock) || state.getValue(ChalkBlock.POSITION) != getChalkPos(x, z, state.getValue(ChalkBlock.FACING))))
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

    public static void removeSummoningStructure(Level world, BlockPos pos) {
        for (int x = -2; x <= 2; x++)
            for (int z = -2; z <= 2; z++) {
                if (x != 0 || z != 0) {
                    BlockPos posNew = pos.offset(x, 0, z);
                    if (world.getBlockState(posNew).getBlock() instanceof ChalkBlock)
                        world.destroyBlock(posNew, false);
                }
            }
        world.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.AMBIENT, 0.4F, 1F);
        world.removeBlockEntity(pos);
        world.destroyBlock(pos, false);
    }

    public static void summonServant(BaseServant servant, ServerPlayer player, ServerLevel world, BlockPos pos) {
        servant.setOwner(player);
        servant.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, Mth.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
        servant.finalizeSpawn(world, world.getCurrentDifficultyAt(servant.blockPosition()), MobSpawnType.TRIGGERED, null, null);
        world.addFreshEntity(servant);
        GrailWarHandler.get(world.getServer()).join(player);
    }

    public static void summonRandomServant(ItemStack stack, ServerPlayer player, BlockPos pos, ServerLevel world) {
        GrailWarHandler handler = GrailWarHandler.get(world.getServer());
        List<EnumServantType> spawnableTypes = Arrays.stream(EnumServantType.values())
                .filter(handler::canSpawnServantClass).toList();
        EnumServantType type;
        if (stack.getItem() instanceof ItemServantCharm charm && player.getRandom().nextFloat() <= 0.6 && handler.canSpawnServantClass(charm.type))
            type = ((ItemServantCharm) stack.getItem()).type;
        else
            type = spawnableTypes.get(world.random.nextInt(spawnableTypes.size()));
        List<RegistryEntrySupplier<EntityType<BaseServant>>> entities = ModEntities.getFromType(type)
                .stream().filter(sup -> handler.canSpawnServantType(sup.getID())).toList();
        if (entities.size() == 0)
            return;
        BaseServant servant = entities.get(world.random.nextInt(entities.size())).get().create(world);
        summonServant(servant, player, world, pos);
    }

    public static BaseServant randomServant(ServerLevel world) {
        GrailWarHandler handler = GrailWarHandler.get(world.getServer());
        List<EnumServantType> spawnableTypes = Arrays.stream(EnumServantType.values())
                .filter(handler::canSpawnServantClass).toList();
        if (spawnableTypes.size() == 0)
            return null;
        EnumServantType type = spawnableTypes.get(world.random.nextInt(spawnableTypes.size()));
        List<RegistryEntrySupplier<EntityType<BaseServant>>> entities = ModEntities.getFromType(type)
                .stream().filter(sup -> handler.canSpawnServantType(sup.getID())).toList();
        if (entities.size() == 0)
            return null;
        return entities.get(world.random.nextInt(entities.size())).get().create(world);
    }
}