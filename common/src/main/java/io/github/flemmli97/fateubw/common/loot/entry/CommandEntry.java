package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootCodecs;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class CommandEntry extends GrailLootEntry<CommandEntry> {

    public static final MapCodec<CommandEntry> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                    Codec.STRING.fieldOf("command").forGetter(d -> d.command),
                    LootCodecs.LOOT_ITEM_CONDITION.listOf().optionalFieldOf("conditions").forGetter(d -> d.conditions.length == 0 ? Optional.empty() : Optional.of(Arrays.stream(d.conditions).toList()))
            ).apply(inst, (command, cond) -> new CommandEntry(command, cond.map(l -> l.toArray(l.toArray(new LootItemCondition[0]))).orElse(new LootItemCondition[0])))
    );

    private final String command;

    public CommandEntry(String command, LootItemCondition... conditions) {
        super(conditions);
        this.command = command;
    }

    @Override
    public Supplier<LootSerializerType<CommandEntry>> getType() {
        return FateGrailLootSerializer.COMMAND;
    }

    @Override
    public void accept(ServerPlayer player, LootContext context) {
        player.getServer().getCommands().performPrefixedCommand(player.createCommandSourceStack().withPermission(2), this.command);
    }
}