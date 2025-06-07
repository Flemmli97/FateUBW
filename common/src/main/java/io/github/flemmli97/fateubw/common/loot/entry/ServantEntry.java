package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.GrailLootSerializer;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.Supplier;

public class ServantEntry extends GrailLootEntry<ServantEntry> {

    public static final Codec<ServantEntry> CODEC = Codec.BOOL.fieldOf("as_loot")
            .xmap(ServantEntry::new, e -> e.loot).codec();

    private final boolean loot;

    public ServantEntry(boolean loot) {
        super(new LootItemCondition[0]);
        this.loot = loot;
    }

    @Override
    public Supplier<LootSerializerType<ServantEntry>> getType() {
        return GrailLootSerializer.SERVANT;
    }

    @Override
    public void accept(ServerPlayer player, LootContext lootContext) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.restoreServant(player, this.loot));
    }
}
