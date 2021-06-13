package io.github.flemmli97.fate.common.registry;

import io.github.flemmli97.fate.Fate;
import io.github.flemmli97.fate.common.items.ItemChalk;
import io.github.flemmli97.fate.common.items.ItemCrystal;
import io.github.flemmli97.fate.common.items.ItemHolyGrail;
import io.github.flemmli97.fate.common.items.ItemManaBottle;
import io.github.flemmli97.fate.common.items.ItemServantCharm;
import io.github.flemmli97.fate.common.items.weapons.ClassSpear;
import io.github.flemmli97.fate.common.items.weapons.ItemArcherBow;
import io.github.flemmli97.fate.common.items.weapons.ItemDagger;
import io.github.flemmli97.fate.common.items.weapons.ItemEA;
import io.github.flemmli97.fate.common.items.weapons.ItemExcalibur;
import io.github.flemmli97.fate.common.items.weapons.ItemGaeBolg;
import io.github.flemmli97.fate.common.items.weapons.ItemGrimoire;
import io.github.flemmli97.fate.common.items.weapons.ItemKatana;
import io.github.flemmli97.fate.common.items.weapons.ItemStaff;
import io.github.flemmli97.fate.common.lib.ItemTiers;
import io.github.flemmli97.fate.common.utils.EnumServantType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fate.MODID);
    public static final List<RegistryObject<Item>> charms = new ArrayList<>();

    public static final RegistryObject<Item> invisexcalibur = ITEMS.register("invis_excalibur", () -> new SwordItem(ItemTiers.invis_excalibur, 0, -2.4f, new Item.Properties()) {
        @Override
        public boolean hasEffect(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryObject<Item> excalibur = ITEMS.register("excalibur", () -> new ItemExcalibur(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> gaebolg = ITEMS.register("gae_bolg", () -> new ItemGaeBolg(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> gaedearg = ITEMS.register("gae_dearg", () -> new ClassSpear(ItemTiers.gae_dearg, new Item.Properties().group(Fate.TAB), -1.5f, 3.5f));
    public static final RegistryObject<Item> gaebuidhe = ITEMS.register("gae_buidhe", () -> new ClassSpear(ItemTiers.gae_buidhe, new Item.Properties().group(Fate.TAB), -1.5f, 3.5f));
    public static final RegistryObject<Item> kanshou = ITEMS.register("kanshou", () -> new SwordItem(ItemTiers.kanshou, 0, -2f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> bakuya = ITEMS.register("bakuya", () -> new SwordItem(ItemTiers.bakuya, 0, -2f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> archbow = ITEMS.register("archer_bow", () -> new ItemArcherBow(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> enumaelish = ITEMS.register("enuma_elish", () -> new ItemEA(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> staff = ITEMS.register("staff", () -> new ItemStaff(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> ruleBreaker = ITEMS.register("rule_breaker", () -> new SwordItem(ItemTiers.ruleBreaker, 0, -2.4f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> grimoire = ITEMS.register("prelati", () -> new ItemGrimoire(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> heraclesAxe = ITEMS.register("heracles_axe", () -> new AxeItem(ItemTiers.heraclesAxe, 0, -2.9f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> arondight = ITEMS.register("arondight", () -> new SwordItem(ItemTiers.arondight, 0, -2.4f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> kupriots = ITEMS.register("kupriots", () -> new SwordItem(ItemTiers.kupriots, 0, -2.4f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> medusaDagger = ITEMS.register("dagger", () -> new ItemDagger(ItemTiers.dagger, 0, -2.0f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> assassinDagger = ITEMS.register("assassin_dagger", () -> new ItemDagger(ItemTiers.assassinDagger, 0, -1.8f, new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> katana = ITEMS.register("katana", () -> new ItemKatana(ItemTiers.katana, 0, -2.6f, new Item.Properties().group(Fate.TAB)));

    public static final RegistryObject<Item> altar = ITEMS.register("altar", () -> new BlockItem(ModBlocks.altar.get(), new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> crystalOre = ITEMS.register("crystal_ore", () -> new BlockItem(ModBlocks.crystalOre.get(), new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> charmOre = ITEMS.register("charm_ore", () -> new BlockItem(ModBlocks.charmOre.get(), new Item.Properties().group(Fate.TAB)));

    public static final RegistryObject<Item> charmNone = registercharm(EnumServantType.NOTASSIGNED);
    public static final RegistryObject<Item> charmSaber = registercharm(EnumServantType.SABER);
    public static final RegistryObject<Item> charmArcher = registercharm(EnumServantType.ARCHER);
    public static final RegistryObject<Item> charmLancer = registercharm(EnumServantType.LANCER);
    public static final RegistryObject<Item> charmCaster = registercharm(EnumServantType.CASTER);
    public static final RegistryObject<Item> charmBerserker = registercharm(EnumServantType.BERSERKER);
    public static final RegistryObject<Item> charmRider = registercharm(EnumServantType.RIDER);
    public static final RegistryObject<Item> charmAssassin = registercharm(EnumServantType.ASSASSIN);
    public static final RegistryObject<Item> crystalFire = ITEMS.register("gem_shard_fire", () -> new Item(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> crystalWind = ITEMS.register("gem_shard_wind", () -> new Item(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> crystalEarth = ITEMS.register("gem_shard_earth", () -> new Item(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> crystalWater = ITEMS.register("gem_shard_water", () -> new Item(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> crystalVoid = ITEMS.register("gem_shard_void", () -> new Item(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> crystalCluster = ITEMS.register("gem_cluster", () -> new ItemCrystal(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> chalk = ITEMS.register("chalk", () -> new ItemChalk(new Item.Properties().group(Fate.TAB).defaultMaxDamage(32)));
    public static final RegistryObject<Item> manaBottle = ITEMS.register("mana_bottle", () -> new ItemManaBottle(new Item.Properties().group(Fate.TAB)));
    public static final RegistryObject<Item> grail = ITEMS.register("holy_grail", () -> new ItemHolyGrail(new Item.Properties().group(Fate.TAB)));

    public static final RegistryObject<Item> icon0 = ITEMS.register("icon_0", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> icon1 = ITEMS.register("icon_1", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> icon2 = ITEMS.register("icon_2", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> icon3 = ITEMS.register("icon_3", () -> new Item(new Item.Properties()));

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

    private static RegistryObject<Item> registercharm(EnumServantType type) {
        RegistryObject<Item> item = ITEMS.register("servant_medallion_" + type.getLowercase(), () -> new ItemServantCharm(type, new Item.Properties().group(Fate.TAB)));
        charms.add(item);
        return item;
    }
}