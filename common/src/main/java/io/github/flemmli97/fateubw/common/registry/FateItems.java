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
import io.github.flemmli97.fateubw.common.items.weapons.KatanaItem;
import io.github.flemmli97.fateubw.common.items.weapons.MedusaDaggerItem;
import io.github.flemmli97.fateubw.common.items.weapons.SpearItem;
import io.github.flemmli97.fateubw.common.items.weapons.StaffItem;
import io.github.flemmli97.fateubw.common.lib.BuiltinServantClasses;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.tenshilib.common.item.AnimationDebugger;
import io.github.flemmli97.tenshilib.loader.LoaderRegistryAccess;
import io.github.flemmli97.tenshilib.loader.registry.LoaderRegister;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;

import java.util.Random;
import java.util.function.Supplier;

public class FateItems {

    public static final LoaderRegister<Item> ITEMS = LoaderRegistryAccess.INSTANCE.of(Registries.ITEM, Fate.MODID);

    public static final RegistryEntrySupplier<Item, SwordItem> INVISEXCALIBUR = register("invis_excalibur", () -> new SwordItem(ItemTiers.INVIS_EXCALIBUR, 0, -2.4f, new Item.Properties()) {
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryEntrySupplier<Item, ExcaliburItem> EXCALIBUR = register("excalibur", () -> new ExcaliburItem(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, GaeBolgItem> GAEBOLG = register("gae_bolg", () -> new GaeBolgItem(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, SpearItem> GAEDEARG = register("gae_dearg", () -> new SpearItem(ItemTiers.GAE_DEARG, new Item.Properties(), -1.5f, 4));
    public static final RegistryEntrySupplier<Item, SpearItem> GAEBUIDHE = register("gae_buidhe", () -> new SpearItem(ItemTiers.GAE_BUIDHE, new Item.Properties(), -1.5f, 3f));
    public static final RegistryEntrySupplier<Item, KanshouBakuyaItem> KANSHOU = register("kanshou", FateItems::kanshou);
    public static final RegistryEntrySupplier<Item, KanshouBakuyaItem> BAKUYA = register("bakuya", () -> new KanshouBakuyaItem(ItemTiers.KANSHOU_BAKUYA, 0, -2f, new Item.Properties(), KANSHOU));
    public static final RegistryEntrySupplier<Item, ArcherBowItem> ARCHBOW = register("emiyas_bow", () -> new ArcherBowItem(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item, EnumaElishItem> ENUMAELISH = register("enuma_elish", () -> new EnumaElishItem(new Item.Properties()));
    public static final RegistryEntrySupplier<Item, StaffItem> STAFF = register("medeas_staff", () -> new StaffItem(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item, SwordItem> RULE_BREAKER = register("rule_breaker", () -> new SwordItem(ItemTiers.RULE_BREAKER, 0, -2.4f, new Item.Properties()));
    public static final RegistryEntrySupplier<Item, GrimoireItem> GRIMOIRE = register("prelatis_spellbook", () -> new GrimoireItem(new Item.Properties().stacksTo(1)));
    public static final RegistryEntrySupplier<Item, AxeItem> HERACLES_AXE = register("heracles_axe", () -> new AxeItem(ItemTiers.HERACLES_AXE, 0, -3.2f, new Item.Properties()));
    public static final RegistryEntrySupplier<Item, SwordItem> ARONDIGHT = register("arondight", () -> new SwordItem(ItemTiers.ARONDIGHT, 0, -2.4f, new Item.Properties()));
    public static final RegistryEntrySupplier<Item, SwordItem> KUPRIOTS = register("kupriots", () -> new SwordItem(ItemTiers.KUPRIOTS, 0, -2.4f, new Item.Properties()));
    public static final RegistryEntrySupplier<Item, MedusaDaggerItem> MEDUSA_DAGGER = register("medusas_dagger", () -> new MedusaDaggerItem(ItemTiers.DAGGER, 0, -2.0f, new Item.Properties()));
    public static final RegistryEntrySupplier<Item, DaggerItem> ASSASSIN_DAGGER = register("assassin_dagger", () -> new DaggerItem(ItemTiers.ASSASSIN_DAGGER, 0, -1.5f, new Item.Properties()));
    public static final RegistryEntrySupplier<Item, KatanaItem> MONOHOSHI_ZAO = register("monohoshi_zao", () -> new KatanaItem(ItemTiers.KATANA, 0, -2.6f, new Item.Properties()));

    public static final RegistryEntrySupplier<Item, BlockItem> ALTAR = register("summoning_altar", () -> new BlockItem(FateBlocks.ALTAR.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> GEM_ORE = register("gem_ore", () -> new BlockItem(FateBlocks.GEM_ORE.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> ARTIFACT_ORE = register("artifact_ore", () -> new BlockItem(FateBlocks.ARTIFACT_ORE.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> DEEP_SLATE_GEM_ORE = register("deepslate_gem_ore", () -> new BlockItem(FateBlocks.DEEP_SLATE_GEM_ORE.get(), new Item.Properties()));
    public static final RegistryEntrySupplier<Item, BlockItem> DEEP_SLATE_ARTIFACT_ORE = register("deepslate_artifact_ore", () -> new BlockItem(FateBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), new Item.Properties()));

    public static final RegistryEntrySupplier<Item, Item> CHARM_NONE = registerCharm(BuiltinServantClasses.NONE);
    public static final RegistryEntrySupplier<Item, Item> CHARM_SABER = registerCharm(BuiltinServantClasses.SABER);
    public static final RegistryEntrySupplier<Item, Item> CHARM_ARCHER = registerCharm(BuiltinServantClasses.ARCHER);
    public static final RegistryEntrySupplier<Item, Item> CHARM_LANCER = registerCharm(BuiltinServantClasses.LANCER);
    public static final RegistryEntrySupplier<Item, Item> CHARM_CASTER = registerCharm(BuiltinServantClasses.CASTER);
    public static final RegistryEntrySupplier<Item, Item> CHARM_BERSERKER = registerCharm(BuiltinServantClasses.BERSERKER);
    public static final RegistryEntrySupplier<Item, Item> CHARM_RIDER = registerCharm(BuiltinServantClasses.RIDER);
    public static final RegistryEntrySupplier<Item, Item> CHARM_ASSASSIN = registerCharm(BuiltinServantClasses.ASSASSIN);
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

    private static RegistryEntrySupplier<Item, Item> registerCharm(ResourceLocation type) {
        return register("artifact_" + type.getPath(), () -> new Item(new Item.Properties()
                .stacksTo(8)
                .rarity(Rarity.RARE)
                .component(FateDataComponents.CLASS_RELIC.get(), type)));
    }

    private static <T extends Item> RegistryEntrySupplier<Item, T> register(String name, Supplier<T> sup) {
        return register(name, sup, true);
    }

    private static <T extends Item> RegistryEntrySupplier<Item, T> register(String name, Supplier<T> sup, boolean withTab) {
        RegistryEntrySupplier<Item, T> item = register(name, sup);
        if (withTab) {
            FateCreativeTab.addToTab(item);
        }
        return item;
    }

    private static KanshouBakuyaItem kanshou() {
        return new KanshouBakuyaItem(ItemTiers.KANSHOU_BAKUYA, 0, -2f, new Item.Properties(), BAKUYA);
    }
}