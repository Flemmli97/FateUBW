package io.github.flemmli97.fateubw.common.loot.entry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.fateubw.common.loot.GrailLootEntry;
import io.github.flemmli97.fateubw.common.loot.LootSerializerType;
import io.github.flemmli97.fateubw.common.registry.FateGrailLootSerializer;
import io.github.flemmli97.fateubw.platform.Platform;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;
import java.util.function.Supplier;

public class ServantEntry extends GrailLootEntry<ServantEntry> {

    public static final MapCodec<ServantEntry> CODEC = Codec.BOOL.fieldOf("as_loot")
            .xmap(ServantEntry::new, e -> e.loot);

    private final boolean loot;

    public ServantEntry(boolean loot) {
        super(List.of());
        this.loot = loot;
    }

    @Override
    public Supplier<LootSerializerType<ServantEntry>> getType() {
        return FateGrailLootSerializer.SERVANT;
    }

    @Override
    public void accept(ServerPlayer player, LootContext lootContext) {
        Platform.INSTANCE.getPlayerData(player).restoreServant(this.loot);
    }
}
