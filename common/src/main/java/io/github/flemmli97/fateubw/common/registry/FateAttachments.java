package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.attachment.PlayerData;
import io.github.flemmli97.tenshilib.common.attachment.AttachmentType;
import io.github.flemmli97.tenshilib.loader.registry.AttachmentRegister;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class FateAttachments {

    public static final AttachmentRegister.AttachmentRegistry ATTACHMENTS = AttachmentRegister.INSTANCE.of(Fate.MODID);

    public static final Supplier<AttachmentType<Player, PlayerData>> PLAYER_DATA = ATTACHMENTS.register("player_data", AttachmentType.builder(PlayerData::new)
            .transferHandler(((from, targetHolder, wasDead) -> new PlayerData(targetHolder).from(from))));
}
