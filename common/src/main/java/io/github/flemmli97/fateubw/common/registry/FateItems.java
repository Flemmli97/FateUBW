package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.items.ChalkItem;
import io.github.flemmli97.fateubw.common.items.CrystalItem;
import io.github.flemmli97.fateubw.common.items.HolyGrailItem;
import io.github.flemmli97.fateubw.common.items.ManaBottleItem;
import io.github.flemmli97.fateubw.common.items.weapons.ArcherBowItem;
import io.github.flemmli97.fateubw.common.items.weapons.DaggerItem;
import io.github.flemmli97.fateubw.common.items.weapons.EnumaElishItem;
import io.github.flemmli97.fateubw.common.items.weapons.ExcaliburItem;
import io.github.flemmli97.fateubw.common.items.weapons.GaeBolgItem;
import io.github.flemmli97.fateubw.common.items.weapons.GrimoireItem;
import io.github.flemmli97.fateubw.common.items.weapons.KanshouBakuyaItem;
import io.github.flemmli97.fateubw.common.items.weapons.MedeasStaffItem;
import io.github.flemmli97.fateubw.common.items.weapons.MedusasDaggerItem;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.common.lib.LibAttributeModifiers;
import io.github.flemmli97.tenshilib.common.item.AnimationDebugger;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.Random;
import java.util.function.Supplier;

public class FateItems {

    public static final LoaderRegister<Item> ITEMS = LoaderRegistryAccess.INSTANCE.of(Registries.ITEM, Fate.MODID);

    public static final RegistryEntrySupplier<Item, SwordItem> INVISEXCALIBUR = register("invis_excalibur", () -> new SwordItem(ItemTiers.INVIS_EXCALIBUR, new Item.Properties()
            .attributes(createAttributes(ItemTiers.INVIS_EXCALIBUR, -2.4f))
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)), false);
    public static final RegistryEntrySupplier<Item, ExcaliburItem> EXCALIBUR = register("excalibur", () -> new ExcaliburItem(new Item.Properties().attributes(createAttributes(ItemTiers.EXCALIBUR, -2.4f))));
    public static final RegistryEntrySupplier<Item, GaeBolgItem> GAEBOLG = register("gae_bolg", () -> new GaeBolgItem(new Item.Properties().attributes(createAttributes(ItemTiers.GAE_BOLG, -2, 5))));
    public static final RegistryEntrySupplier<Item, TieredItem> GAEDEARG = register("gae_dearg", () -> new TieredItem(ItemTiers.GAE_DEARG, new Item.Properties().attributes(createAttributes(ItemTiers.GAE_DEARG, -1.5f, 5))));
    public static final RegistryEntrySupplier<Item, TieredItem> GAEBUIDHE = register("gae_buidhe", () -> new TieredItem(ItemTiers.GAE_BUIDHE, new Item.Properties().attributes(createAttributes(ItemTiers.GAE_BUIDHE, -1.5f, 4))));
    public static final RegistryEntrySupplier<Item, KanshouBakuyaItem> KANSHOU = register("kanshou", FateItems::kanshou);
    public static final RegistryEntrySupplier<Item, KanshouBakuyaItem> BAKUYA = register("bakuya", () -> new KanshouBakuyaItem(ItemTiers.KANSHOU_BAKUYA, new Item.Properties().attributes(createAttributes(ItemTiers.KANSHOU_BAKUYA, -2f)), () -> KANSHOU));
    public static final RegistryEntrySupplier<Item, ArcherBowItem> EMIYAS_BOW = register("emiyas_bow", () -> new ArcherBowItem(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item, EnumaElishItem> ENUMAELISH = register("enuma_elish", () -> new EnumaElishItem(new Item.Properties().attributes(createAttributes(ItemTiers.ENUMA_ELISH, -2.4f))));
    public static final RegistryEntrySupplier<Item, MedeasStaffItem> STAFF = register("medeas_staff", () -> new MedeasStaffItem(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item, SwordItem> RULE_BREAKER = register("rule_breaker", () -> new SwordItem(ItemTiers.RULE_BREAKER, new Item.Properties().attributes(createAttributes(ItemTiers.RULE_BREAKER, -2.4f))));
    public static final RegistryEntrySupplier<Item, GrimoireItem> GRIMOIRE = register("prelatis_spellbook", () -> new GrimoireItem(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item, AxeItem> HERACLES_AXE = register("heracles_axe", () -> new AxeItem(ItemTiers.HERACLES_AXE, new Item.Properties().attributes(createAttributes(ItemTiers.HERACLES_AXE, -3.2f))));
    public static final RegistryEntrySupplier<Item, SwordItem> ARONDIGHT = register("arondight", () -> new SwordItem(ItemTiers.ARONDIGHT, new Item.Properties().attributes(createAttributes(ItemTiers.ARONDIGHT, -2.4f))));
    public static final RegistryEntrySupplier<Item, SwordItem> KUPRIOTS = register("kupriots", () -> new SwordItem(ItemTiers.KUPRIOTS, new Item.Properties().attributes(createAttributes(ItemTiers.KUPRIOTS, -2.4f))));
    public static final RegistryEntrySupplier<Item, MedusasDaggerItem> MEDUSA_DAGGER = register("medusas_dagger", () -> new MedusasDaggerItem(ItemTiers.MEDUSAS_DAGGER, new Item.Properties().attributes(createAttributes(ItemTiers.MEDUSAS_DAGGER, -2))));
    public static final RegistryEntrySupplier<Item, DaggerItem> ASSASSIN_DAGGER = register("assassin_dagger", () -> new DaggerItem(ItemTiers.ASSASSIN_DAGGER, new Item.Properties().attributes(createAttributes(ItemTiers.ASSASSIN_DAGGER, -1.5f))));
    public static final RegistryEntrySupplier<Item, SwordItem> MONOHOSHI_ZAO = register("monohoshi_zao", () -> new SwordItem(ItemTiers.MONOHOSHI_ZAO, new Item.Properties().attributes(createAttributes(ItemTiers.MONOHOSHI_ZAO, -2.6f, 4.5))));
    public static final RegistryEntrySupplier<Item, SwordItem> AESTUS_ESTUS = register("aestus_estus", () -> new SwordItem(ItemTiers.AESTUS_ESTUS, new Item.Properties().attributes(createAttributes(ItemTiers.AESTUS_ESTUS, -2.4f))));

    public static final RegistryEntrySupplier<Item, BlockItem> ALTAR = register("summoning_altar", () -> new BlockItem(FateBlocks.ALTAR.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> GEM_ORE = register("gem_ore", () -> new BlockItem(FateBlocks.GEM_ORE.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> ARTIFACT_ORE = register("artifact_ore", () -> new BlockItem(FateBlocks.ARTIFACT_ORE.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> DEEP_SLATE_GEM_ORE = register("deepslate_gem_ore", () -> new BlockItem(FateBlocks.DEEP_SLATE_GEM_ORE.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> DEEP_SLATE_ARTIFACT_ORE = register("deepslate_artifact_ore", () -> new BlockItem(FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), new Item.Properties()));

    public static final RegistryEntrySupplier<Item, Item> ARTIFACT_SABER = registerArtifact(BuiltinServantClasses.SABER);
    public static final RegistryEntrySupplier<Item, Item> ARTIFACT_ARCHER = registerArtifact(BuiltinServantClasses.ARCHER);
    public static final RegistryEntrySupplier<Item, Item> ARTIFACT_LANCER = registerArtifact(BuiltinServantClasses.LANCER);
    public static final RegistryEntrySupplier<Item, Item> ARTIFACT_CASTER = registerArtifact(BuiltinServantClasses.CASTER);
    public static final RegistryEntrySupplier<Item, Item> ARTIFACT_BERSERKER = registerArtifact(BuiltinServantClasses.BERSERKER);
    public static final RegistryEntrySupplier<Item, Item> ARTIFACT_RIDER = registerArtifact(BuiltinServantClasses.RIDER);
    public static final RegistryEntrySupplier<Item, Item> ARTIFACT_ASSASSIN = registerArtifact(BuiltinServantClasses.ASSASSIN);
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_RED = register("gem_shard_red", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_GREEN = register("gem_shard_green", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_YELLOW = register("gem_shard_yellow", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_BLUE = register("gem_shard_blue", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, Item> CRYSTAL_BLACK = register("gem_shard_black", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, CrystalItem> MANA_GEM = register("mana_gem", () -> new CrystalItem(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, ChalkItem> CHALK = register("chalk", () -> new ChalkItem(new Item.Properties().durability(32)));
    public static final RegistryEntrySupplier<Item, ManaBottleItem> MANA_BOTTLE = register("mana_bottle", () -> new ManaBottleItem(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, HolyGrailItem> GRAIL = register("holy_grail", () -> new HolyGrailItem(new Item.Properties()));

    public static final RegistryEntrySupplier<Item, Item> ICON_0 = register("icon_0", () -> new Item(new Item.Properties()), false);
    public static final RegistryEntrySupplier<Item, Item> ICON_1 = register("icon_1", () -> new Item(new Item.Properties()), false);
    public static final RegistryEntrySupplier<Item, Item> ICON_2 = register("icon_2", () -> new Item(new Item.Properties()), false);
    public static final RegistryEntrySupplier<Item, Item> ICON_3 = register("icon_3", () -> new Item(new Item.Properties()), false);
    public static final RegistryEntrySupplier<Item, AnimationDebugger> ANIMATION_DEBUG = register("animation_debugger", () -> new AnimationDebugger(
            new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), FateDataComponents.SELECTED_UUID, FateDataComponents.SELECTED_ANIMATION), false);

    public static final Supplier<Item> RANDOM_ICON = () -> {
        int i = new Random().nextInt(4);
        return switch (i) {
            case 0 -> ICON_0.get();
            case 1 -> ICON_1.get();
            case 2 -> ICON_2.get();
            default -> ICON_3.get();
        };
    };

    private static RegistryEntrySupplier<Item, Item> registerArtifact(ResourceLocation type) {
        return register("artifact_" + type.getPath(), () -> new Item(new Item.Properties()
                .stacksTo(8)
                .rarity(Rarity.RARE)
                .component(FateDataComponents.CLASS_RELIC.get(), type)));
    }

    private static <T extends Item> RegistryEntrySupplier<Item, T> register(String name, Supplier<T> sup) {
        return register(name, sup, true);
    }

    private static <T extends Item> RegistryEntrySupplier<Item, T> register(String name, Supplier<T> sup, boolean withTab) {
        RegistryEntrySupplier<Item, T> item = ITEMS.register(name, sup);
        if (withTab) {
            FateCreativeTab.addToTab(item);
        }
        return item;
    }

    private static KanshouBakuyaItem kanshou() {
        return new KanshouBakuyaItem(ItemTiers.KANSHOU_BAKUYA, new Item.Properties().attributes(createAttributes(ItemTiers.KANSHOU_BAKUYA, -2f)), () -> BAKUYA);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, double attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, double attackSpeed, double range) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(LibAttributeModifiers.BASE_ATTACK_RANGE_ID, range - 3.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }
}