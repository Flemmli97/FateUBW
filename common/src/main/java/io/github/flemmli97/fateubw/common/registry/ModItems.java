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
    public static final List<RegistryEntrySupplier<Item>> charms = new ArrayList<>();

    public static final RegistryEntrySupplier<Item> invisexcalibur = ITEMS.register("invis_excalibur", () -> new SwordItem(ItemTiers.invis_excalibur, 0, -2.4f, new Item.Properties()) {
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryEntrySupplier<Item> excalibur = ITEMS.register("excalibur", () -> Platform.INSTANCE.createExcalibur(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> gaebolg = ITEMS.register("gae_bolg", () -> new ItemGaeBolg(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> gaedearg = ITEMS.register("gae_dearg", () -> new ClassSpear(ItemTiers.gae_dearg, new Item.Properties().tab(Fate.TAB), -1.5f, 3.5f));
    public static final RegistryEntrySupplier<Item> gaebuidhe = ITEMS.register("gae_buidhe", () -> new ClassSpear(ItemTiers.gae_buidhe, new Item.Properties().tab(Fate.TAB), -1.5f, 3.5f));
    public static final RegistryEntrySupplier<Item> kanshou = ITEMS.register("kanshou", ModItems::kanshou);
    public static final RegistryEntrySupplier<Item> bakuya = ITEMS.register("bakuya", () -> new ItemKanshouBakuya(ItemTiers.kanshouBakuya, 0, -2f, new Item.Properties().tab(Fate.TAB), kanshou));
    public static final RegistryEntrySupplier<Item> archbow = ITEMS.register("emiyas_bow", () -> new ItemArcherBow(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> enumaelish = ITEMS.register("enuma_elish", () -> Platform.INSTANCE.createEA(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> staff = ITEMS.register("medeas_staff", () -> new ItemStaff(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> ruleBreaker = ITEMS.register("rule_breaker", () -> new SwordItem(ItemTiers.ruleBreaker, 0, -2.4f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> grimoire = ITEMS.register("prelatis_spellbook", () -> new ItemGrimoire(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> heraclesAxe = ITEMS.register("heracles_axe", () -> Platform.INSTANCE.createAxe(ItemTiers.heraclesAxe, 0, -2.9f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> arondight = ITEMS.register("arondight", () -> new SwordItem(ItemTiers.arondight, 0, -2.4f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> kupriots = ITEMS.register("kupriots", () -> new SwordItem(ItemTiers.kupriots, 0, -2.4f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> medusaDagger = ITEMS.register("medusas_dagger", () -> new ItemMedusaDagger(ItemTiers.dagger, 0, -2.0f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> assassinDagger = ITEMS.register("assassin_dagger", () -> new ItemDagger(ItemTiers.assassinDagger, 0, -1.8f, new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> katana = ITEMS.register("katana", () -> new ItemKatana(ItemTiers.katana, 0, -2.6f, new Item.Properties().tab(Fate.TAB)));

    public static final RegistryEntrySupplier<Item> altar = ITEMS.register("altar", () -> new BlockItem(ModBlocks.altar.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> gemOre = ITEMS.register("gem_ore", () -> new BlockItem(ModBlocks.gemOre.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> artifactOre = ITEMS.register("artifact_ore", () -> new BlockItem(ModBlocks.artifactOre.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> deepSlateGemOre = ITEMS.register("deepslate_gem_ore", () -> new BlockItem(ModBlocks.deepSlateGemOre.get(), new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> deepSlateArtifactOre = ITEMS.register("deepslate_artifact_ore", () -> new BlockItem(ModBlocks.deepSlateArtifactOre.get(), new Item.Properties().tab(Fate.TAB)));

    public static final RegistryEntrySupplier<Item> charmNone = registercharm(EnumServantType.NOTASSIGNED);
    public static final RegistryEntrySupplier<Item> charmSaber = registercharm(EnumServantType.SABER);
    public static final RegistryEntrySupplier<Item> charmArcher = registercharm(EnumServantType.ARCHER);
    public static final RegistryEntrySupplier<Item> charmLancer = registercharm(EnumServantType.LANCER);
    public static final RegistryEntrySupplier<Item> charmCaster = registercharm(EnumServantType.CASTER);
    public static final RegistryEntrySupplier<Item> charmBerserker = registercharm(EnumServantType.BERSERKER);
    public static final RegistryEntrySupplier<Item> charmRider = registercharm(EnumServantType.RIDER);
    public static final RegistryEntrySupplier<Item> charmAssassin = registercharm(EnumServantType.ASSASSIN);
    public static final RegistryEntrySupplier<Item> crystalFire = ITEMS.register("gem_shard_fire", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> crystalWind = ITEMS.register("gem_shard_wind", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> crystalEarth = ITEMS.register("gem_shard_earth", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> crystalWater = ITEMS.register("gem_shard_water", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> crystalVoid = ITEMS.register("gem_shard_void", () -> new Item(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> crystalCluster = ITEMS.register("gem_cluster", () -> new ItemCrystal(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> chalk = ITEMS.register("chalk", () -> new ItemChalk(new Item.Properties().tab(Fate.TAB).defaultDurability(32)));
    public static final RegistryEntrySupplier<Item> manaBottle = ITEMS.register("mana_bottle", () -> new ItemManaBottle(new Item.Properties().tab(Fate.TAB)));
    public static final RegistryEntrySupplier<Item> grail = ITEMS.register("holy_grail", () -> new ItemHolyGrail(new Item.Properties().tab(Fate.TAB)));

    public static final RegistryEntrySupplier<Item> icon0 = ITEMS.register("icon_0", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> icon1 = ITEMS.register("icon_1", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> icon2 = ITEMS.register("icon_2", () -> new Item(new Item.Properties()));
    public static final RegistryEntrySupplier<Item> icon3 = ITEMS.register("icon_3", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> randomIcon = () -> {
        int i = new Random().nextInt(4);
        switch (i) {
            case 0:
                return icon0.get();
            case 1:
                return icon1.get();
            case 2:
                return icon2.get();
        }
        return icon3.get();
    };

    private static RegistryEntrySupplier<Item> registercharm(EnumServantType type) {
        String name = type == EnumServantType.NOTASSIGNED ? "servant_artifact" : "servant_artifact_" + type.getLowercase();
        RegistryEntrySupplier<Item> item = ITEMS.register(name, () -> new ItemServantCharm(type, new Item.Properties().tab(Fate.TAB)));
        charms.add(item);
        return item;
    }

    private static ItemKanshouBakuya kanshou() {
        return new ItemKanshouBakuya(ItemTiers.kanshouBakuya, 0, -2f, new Item.Properties().tab(Fate.TAB), bakuya);
    }
}