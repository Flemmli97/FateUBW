package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.fateubw.Fate;
import io.github.flemmli97.fateubw.common.items.ItemChalk;
import io.github.flemmli97.fateubw.common.items.ItemCrystal;
import io.github.flemmli97.fateubw.common.items.ItemHolyGrail;
import io.github.flemmli97.fateubw.common.items.ItemManaBottle;
import io.github.flemmli97.fateubw.common.items.ItemServantCharm;
import io.github.flemmli97.fateubw.common.items.weapons.ClassSpear;
import io.github.flemmli97.fateubw.common.items.weapons.ItemArcherBow;
import io.github.flemmli97.fateubw.common.items.weapons.ItemDagger;
import io.github.flemmli97.fateubw.common.items.weapons.ItemGaeBolg;
import io.github.flemmli97.fateubw.common.items.weapons.ItemGrimoire;
import io.github.flemmli97.fateubw.common.items.weapons.ItemKanshouBakuya;
import io.github.flemmli97.fateubw.common.items.weapons.ItemKatana;
import io.github.flemmli97.fateubw.common.items.weapons.ItemMedusaDagger;
import io.github.flemmli97.fateubw.common.items.weapons.ItemStaff;
import io.github.flemmli97.fateubw.common.lib.ItemTiers;
import io.github.flemmli97.fateubw.common.utils.EnumServantType;
import io.github.flemmli97.fateubw.platform.Platform;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import io.github.flemmli97.tenshilib.platform.registry.PlatformRegistry;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ModItems {

    public static final PlatformRegistry<Item> ITEMS = PlatformUtils.INSTANCE.of(Registry.ITEM_REGISTRY, Fate.MODID);
    public static final List<RegistryEntrySupplier<Item>> CHARMS = new ArrayList<>();

    public static final RegistryEntrySupplier<Item> INVISEXCALIBUR = ITEMS.register("invis_excalibur", () -> new SwordItem(ItemTiers.INVIS_EXCALIBUR, 0, -2.4f, new Item.Properties()) {
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryEntrySupplier<Item> EXCALIBUR = ITEMS.register("excalibur", () -> Platform.INSTANCE.createExcalibur(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> GAEBOLG = ITEMS.register("gae_bolg", () -> new ItemGaeBolg(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> GAEDEARG = ITEMS.register("gae_dearg", () -> new ClassSpear(ItemTiers.GAE_DEARG, new Item.Properties().tab(Fate.TAB), -1.5f, 3.5f));
    public static final RegistryEntrySupplier<Item> GAEBUIDHE = ITEMS.register("gae_buidhe", () -> new ClassSpear(ItemTiers.GAE_BUIDHE, new Item.Properties().tab(Fate.TAB), -1.5f, 3.5f));
    public static final RegistryEntrySupplier<Item> KANSHOU = ITEMS.register("kanshou", ModItems::kanshou);
    public static final RegistryEntrySupplier<Item> BAKUYA = ITEMS.register("bakuya", () -> new ItemKanshouBakuya(ItemTiers.KANSHOU_BAKUYA, 0, -2f, new Item.Properties().tab(Fate.TAB), KANSHOU));
    public static final RegistryEntrySupplier<Item> ARCHBOW = ITEMS.register("emiyas_bow", () -> new ItemArcherBow(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> ENUMAELISH = ITEMS.register("enuma_elish", () -> Platform.INSTANCE.createEA(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> STAFF = ITEMS.register("medeas_staff", () -> new ItemStaff(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> RULE_BREAKER = ITEMS.register("rule_breaker", () -> new SwordItem(ItemTiers.RULE_BREAKER, 0, -2.4f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> GRIMOIRE = ITEMS.register("prelatis_spellbook", () -> new ItemGrimoire(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> HERACLES_AXE = ITEMS.register("heracles_axe", () -> Platform.INSTANCE.createAxe(ItemTiers.HERACLES_AXE, 0, -2.9f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> ARONDIGHT = ITEMS.register("arondight", () -> new SwordItem(ItemTiers.ARONDIGHT, 0, -2.4f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> KUPRIOTS = ITEMS.register("kupriots", () -> new SwordItem(ItemTiers.KUPRIOTS, 0, -2.4f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> MEDUSA_DAGGER = ITEMS.register("medusas_dagger", () -> new ItemMedusaDagger(ItemTiers.DAGGER, 0, -2.0f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> ASSASSIN_DAGGER = ITEMS.register("assassin_dagger", () -> new ItemDagger(ItemTiers.ASSASSIN_DAGGER, 0, -1.8f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> KATANA = ITEMS.register("katana", () -> new ItemKatana(ItemTiers.KATANA, 0, -2.6f, new Item.Properties().tab(Fate.TAB)));

    public static final RegistryEntrySupplier<Item> ALTAR = ITEMS.register("altar", () -> new BlockItem(ModBlocks.ALTAR.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> GEM_ORE = ITEMS.register("gem_ore", () -> new BlockItem(ModBlocks.GEM_ORE.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> ARTIFACT_ORE = ITEMS.register("artifact_ore", () -> new BlockItem(ModBlocks.ARTIFACT_ORE.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> DEEP_SLATE_GEM_ORE = ITEMS.register("deepslate_gem_ore", () -> new BlockItem(ModBlocks.DEEP_SLATE_GEM_ORE.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> DEEP_SLATE_ARTIFACT_ORE = ITEMS.register("deepslate_artifact_ore", () -> new BlockItem(ModBlocks.DEEP_SLATE_ARTIFACT_ORE.get(), new Item.Properties().tab(Fate.TAB)));

    public static final RegistryEntrySupplier<Item> CHARM_NONE = registercharm(EnumServantType.NOTASSIGNED);
    public static final RegistryEntrySupplier<Item> CHARM_SABER = registercharm(EnumServantType.SABER);
    public static final RegistryEntrySupplier<Item> CHARM_ARCHER = registercharm(EnumServantType.ARCHER);
    public static final RegistryEntrySupplier<Item> CHARM_LANCER = registercharm(EnumServantType.LANCER);
    public static final RegistryEntrySupplier<Item> CHARM_CASTER = registercharm(EnumServantType.CASTER);
    public static final RegistryEntrySupplier<Item> CHARM_BERSERKER = registercharm(EnumServantType.BERSERKER);
    public static final RegistryEntrySupplier<Item> CHARM_RIDER = registercharm(EnumServantType.RIDER);
    public static final RegistryEntrySupplier<Item> CHARM_ASSASSIN = registercharm(EnumServantType.ASSASSIN);
    public static final RegistryEntrySupplier<Item> CRYSTAL_RED = ITEMS.register("gem_shard_red", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> CRYSTAL_GREEN = ITEMS.register("gem_shard_green", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> CRYSTAL_YELLOW = ITEMS.register("gem_shard_yellow", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> CRYSTAL_BLUE = ITEMS.register("gem_shard_blue", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> CRYSTAL_BLACK = ITEMS.register("gem_shard_black", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> CRYSTAL_CLUSTER = ITEMS.register("gem_cluster", () -> new ItemCrystal(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> CHALK = ITEMS.register("chalk", () -> new ItemChalk(new Item.Properties().tab(Fate.TAB).defaultDurability(32)));
    public static final RegistryEntrySupplier<Item> MANA_BOTTLE = ITEMS.register("mana_bottle", () -> new ItemManaBottle(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> GRAIL = ITEMS.register("holy_grail", () -> new ItemHolyGrail(new Item.Properties().tab(Fate.TAB)));

    public static final RegistryEntrySupplier<Item> ICON_0 = ITEMS.register("icon_0", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> ICON_1 = ITEMS.register("icon_1", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> ICON_2 = ITEMS.register("icon_2", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> ICON_3 = ITEMS.register("icon_3", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> RANDOM_ICON = () -> {
        int i = new Random().nextInt(4);
        return switch (i) {
            case 0 -> ICON_0.get();
            case 1 -> ICON_1.get();
            case 2 -> ICON_2.get();
            default -> ICON_3.get();
        };
    };

    private static RegistryEntrySupplier<Item> registercharm(EnumServantType type) {
        String name = type == EnumServantType.NOTASSIGNED ? "artifact" : "artifact_" + type.getLowercase();
        RegistryEntrySupplier<Item> item = ITEMS.register(name, () -> new ItemServantCharm(type, new Item.Properties().tab(Fate.TAB)));
        CHARMS.add(item);
        return item;
    }

    private static ItemKanshouBakuya kanshou() {
        return new ItemKanshouBakuya(ItemTiers.KANSHOU_BAKUYA, 0, -2f, new Item.Properties().tab(Fate.TAB), BAKUYA);
    }
}