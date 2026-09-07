package io.github.flemmli97.fateubw.common.config.value.weapons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.config.CommonConfig;
import io.github.flemmli97.tenshilib.common.utils.ItemUtils;
import io.github.flemmli97.tenshilib.loader.TenshiLibCrossPlat;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class WeaponList {

    private static final boolean DEBUG = false;
    private static WeightedItemStackList WEAPONS;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static ItemStack getRandomWeapon(LivingEntity entity) {
        if (WEAPONS == null) {
            reload();
            if (WEAPONS == null) {
                return ItemStack.EMPTY;
            }
        }
        return WEAPONS.getRandomStack(entity.getRandom(), 1 - Math.clamp(entity.getHealth() / entity.getMaxHealth(), 0, 1) * 100);
    }

    public static void reload() {
        WEAPONS = null;
        MinecraftServer server = TenshiLibCrossPlat.INSTANCE.getCurrentServer();
        if (server != null) {
            initEquip(server.registryAccess());
        }
    }

    private static void initEquip(HolderLookup.Provider provider) {
        List<Pair<ItemScore, ItemStack>> weapons = new ArrayList<>();
        BuiltInRegistries.ITEM.holders().forEach(holder -> {
            if (CommonConfig.weaponProjectiles.isAllowed(holder)) {
                Pair<ItemScore, ItemStack> score = score(holder);
                if (score != null) {
                    weapons.add(score);
                }
            }
        });
        ItemScore.ScoreRange[] comp = ItemScore.composite(weapons.stream().map(Pair::getFirst).toList());
        WEAPONS = new WeightedItemStackList(weapons.stream()
                .map(p -> {
                    float[] normalized = normalizeAndInvertWeight(p.getFirst(), comp[0], comp[1]);
                    return new WeightedItemStack(p.getSecond(), (int) normalized[0], normalized[1]);
                }).toList());
        if (DEBUG) {
            Path path = TenshiLibCrossPlat.INSTANCE.getCurrentServer()
                    .getServerDirectory().resolve("config").resolve(Fate.MODID).resolve("weapons_probability.json");
            DynamicOps<JsonElement> ops = provider.createSerializationContext(provider.createSerializationContext(JsonOps.INSTANCE));
            try {
                if (!Files.exists(path)) {
                    Files.createFile(path);
                }
                JsonWriter wr = GSON.newJsonWriter(Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING));
                GSON.toJson(WEAPONS.asProbability(ops), JsonElement.class, wr);
                wr.close();
            } catch (IOException e) {
                Fate.LOGGER.error("Error writing weapon probability file", e);
            }
        }
    }

    private static float[] normalizeAndInvertWeight(ItemScore itemScore, ItemScore.ScoreRange min, ItemScore.ScoreRange max) {
        double dmgNorm = normalize(itemScore.damage(), min.damage(), max.damage());
        double score = normalize(Math.sqrt(itemScore.durability()), Math.sqrt(min.durability()), Math.sqrt(max.durability())) * 25
                + dmgNorm * dmgNorm * 70
                + normalize(itemScore.enchantmentValue(), min.enchantmentValue(), max.enchantmentValue()) * 5;
        float weight = score != 0 ? (float) (5000 / score) : 0;
        return new float[]{weight, (float) Math.pow(score, 1 / 2.) * 2};
    }

    private static double normalize(double value, double min, double max) {
        return max == min ? 0.0 : Math.clamp((value - min) / (max - min), 0, 1);
    }

    private static Pair<ItemScore, ItemStack> score(Holder<Item> item) {
        double durability = item.value().components().getOrDefault(DataComponents.MAX_DAMAGE, 0);
        ItemStack stack = new ItemStack(item);
        double damage = ItemUtils.attribute(stack, Attributes.ATTACK_DAMAGE, 0, EquipmentSlotGroup.values());
        if (damage <= 1) {
            return null;
        }
        if ((damage > 1) && durability <= 0) {
            durability = 5000;
        }
        if (item.value().components().has(DataComponents.UNBREAKABLE)) {
            durability *= 3;
        }
        return Pair.of(new ItemScore(durability, damage, item.value().getEnchantmentValue()), stack);
    }
}
