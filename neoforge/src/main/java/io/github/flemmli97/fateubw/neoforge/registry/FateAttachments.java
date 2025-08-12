package io.github.flemmli97.fateubw.neoforge.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.neoforge.attachment.PlayerDataAttachment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FateAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Fate.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerDataAttachment>> PLAYER_DATA = ATTACHMENT_TYPES.register("player_data", () -> AttachmentType.serializable(PlayerDataAttachment::new).build());
}
