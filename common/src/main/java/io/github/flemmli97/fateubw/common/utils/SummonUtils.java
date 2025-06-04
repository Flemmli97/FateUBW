package io.github.flemmli97.fateubw.common.utils;

import io.github.flemmli97.fateubw.common.blocks.ChalkBlock;
import io.github.flemmli97.fateubw.common.blocks.tile.AltarBlockEntity;
import io.github.flemmli97.fateubw.common.datapack.DatapackHandler;
import io.github.flemmli97.fateubw.common.datapack.EntityPropsManager;
import io.github.flemmli97.fateubw.common.entity.servant.BaseServant;
import io.github.flemmli97.fateubw.common.items.ItemServantCharm;
import io.github.flemmli97.fateubw.common.world.GrailWarHandler;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SummonUtils {

    public static void placeSummoningStructure(ServerLevel world, BlockPos pos, AltarBlockEntity altar, Direction facing) {
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
                    world.removeBlock(newPos, false);
                    world.sendParticles(ParticleTypes.CLOUD, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, 1, 0, 0.2, 0, 0);
                }
            }
        altar.setComplete(true);
    }

    public static void removeSummoningStructure(Level world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.AMBIENT, 0.4F, 1F);
        world.removeBlockEntity(pos);
        world.destroyBlock(pos, false);
    }

    public static void summonServant(BaseServant servant, ServerPlayer player, ServerLevel level) {
        servant.setOwner(player);
        servant.finalizeSpawn(level, level.getCurrentDifficultyAt(servant.blockPosition()), MobSpawnType.TRIGGERED, null, null);
        level.addFreshEntity(servant);
        GrailWarHandler.get(level.getServer()).join(player, servant);
    }

    public static boolean summonRandomServant(ItemStack stack, ServerPlayer player, BlockPos pos, ServerLevel level) {
        GrailWarHandler handler = GrailWarHandler.get(level.getServer());
        ResourceLocation servantClass = null;
        if (stack.getItem() instanceof ItemServantCharm charm && player.getRandom().nextFloat() <= 0.6 && handler.canSpawnServantClass(charm.type))
            servantClass = ((ItemServantCharm) stack.getItem()).type;
        BaseServant servant = randomServant(level, Vec3.atCenterOf(pos), player.position(), servantClass);
        if (servant != null)
            summonServant(servant, player, level);
        return servant != null;
    }

    public static BaseServant randomServant(ServerLevel level, Vec3 pos, @Nullable Vec3 lookTarget, @Nullable ResourceLocation servantType) {
        GrailWarHandler handler = GrailWarHandler.get(level.getServer());
        if (servantType == null || !handler.canSpawnServantClass(servantType)) {
            List<ResourceLocation> spawnableTypes = DatapackHandler.SERVANT_PROPS.getServantClasses().stream()
                    .filter(handler::canSpawnServantClass).toList();
            if (spawnableTypes.isEmpty())
                return null;
            servantType = spawnableTypes.get(level.random.nextInt(spawnableTypes.size()));
        }
        List<EntityPropsManager.EntityTypeAndID> entities = DatapackHandler.SERVANT_PROPS.getServantsFromClass(level, servantType)
                .stream().filter(entry -> handler.canSpawnServantType(entry.id())).toList();
        if (entities.isEmpty())
            return null;
        BaseServant servant = entities.get(level.random.nextInt(entities.size())).type().create(level);
        servant.moveTo(pos.x(), pos.y(), pos.z(), level.random.nextFloat() * 360.0F, 0);
        if (lookTarget != null)
            servant.lookAt(EntityAnchorArgument.Anchor.EYES, lookTarget);
        return servant;
    }
}