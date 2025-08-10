package io.github.flemmli97.fateubw.common.registry;

import com.mojang.serialization.Codec;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.components.ServantSpawneggData;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;

import java.util.UUID;

public class FateDataComponents {

    public static final LoaderRegister<DataComponentType<?>> DATA_COMPONENTS = LoaderRegistryAccess.INSTANCE.of(Registries.DATA_COMPONENT_TYPE, Fate.MODID);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ResourceLocation>> CLASS_RELIC = register("class_relice", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ResourceLocation>> SERVANT_RELIC = register("servant_relic", ResourceLocation.CODEC, ResourceLocation.STREAM_CODEC);

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> GLOWING_ITEM = register("glowing_item", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<Unit>> CORRUPTED_ITEM = register("corrupted_item", Codec.unit(Unit.INSTANCE), StreamCodec.unit(Unit.INSTANCE));

    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<ServantSpawneggData>> SERVANT_EGG_DATA = register("servant_egg_data", ServantSpawneggData.CODEC, ServantSpawneggData.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<UUID>> SELECTED_UUID = register("selected_uuid", UUIDUtil.CODEC, UUIDUtil.STREAM_CODEC);
    public static final RegistryEntrySupplier<DataComponentType<?>, DataComponentType<String>> SELECTED_ANIMATION = register("selected_animation", Codec.STRING, ByteBufCodecs.STRING_UTF8);

    private static <T> RegistryEntrySupplier<DataComponentType<?>, DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return DATA_COMPONENTS.register(name, () -> DataComponentType.<T>builder().persistent(codec).networkSynchronized(streamCodec).build());
    }
}
