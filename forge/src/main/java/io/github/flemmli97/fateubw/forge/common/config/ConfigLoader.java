package io.github.flemmli97.fateubw.forge.common.config;

import io.github.flemmli97.fateubw.common.config.ClientConfig;
import io.github.flemmli97.fateubw.common.config.CommonConfig;

public class ConfigLoader {

    public static void loadClient() {
        ClientConfig.manaX = ConfigSpecs.CLIENT_CONFIG.manaBarX.get();
        ClientConfig.manaY = ConfigSpecs.CLIENT_CONFIG.manaBarY.get();
        ClientConfig.manaBarPosition = ConfigSpecs.CLIENT_CONFIG.manaBarPosition.get();
    }

    public static void loadCommon() {
        CommonConfig.minPlayer = ConfigSpecs.COMMON_CONFIG.minPlayer.get();
        CommonConfig.maxPlayer = ConfigSpecs.COMMON_CONFIG.maxPlayer.get();
        CommonConfig.grailWarCooldown = ConfigSpecs.COMMON_CONFIG.grailWarCooldown.get();
        CommonConfig.joinTime = ConfigSpecs.COMMON_CONFIG.joinTime.get();
        CommonConfig.allowDuplicateServant = ConfigSpecs.COMMON_CONFIG.allowDuplicateServant.get();
        CommonConfig.allowDuplicateClass = ConfigSpecs.COMMON_CONFIG.allowDuplicateClass.get();
        CommonConfig.fillMissingSlots = ConfigSpecs.COMMON_CONFIG.fillMissingSlots.get();
        CommonConfig.maxServantCircle = ConfigSpecs.COMMON_CONFIG.maxServantCircle.get();
        CommonConfig.servantMinSpawnDelay = ConfigSpecs.COMMON_CONFIG.servantMinSpawnDelay.get();
        CommonConfig.servantMaxSpawnDelay = ConfigSpecs.COMMON_CONFIG.servantMaxSpawnDelay.get();

        CommonConfig.punishTeleport = ConfigSpecs.COMMON_CONFIG.punishTeleport.get();
        CommonConfig.notifyBlacklist = ConfigSpecs.COMMON_CONFIG.notifyBlackList.get();
        CommonConfig.notificationWhitelist = ConfigSpecs.COMMON_CONFIG.whiteList.get();
        CommonConfig.notifyAll = ConfigSpecs.COMMON_CONFIG.notifyAll.get();
        CommonConfig.npBoostEffect.readFromString(ConfigSpecs.COMMON_CONFIG.npBoostEffect.get());

        CommonConfig.babylonScale = ConfigSpecs.COMMON_CONFIG.babylonScale.get().floatValue();
        CommonConfig.babylonWeapons.readFromString(ConfigSpecs.COMMON_CONFIG.babylonWeaponsBlacklist.get());
        CommonConfig.babylonWeapons.setWhiteList(ConfigSpecs.COMMON_CONFIG.babylonWeaponsWhitelist.get());
        CommonConfig.eaDamage = ConfigSpecs.COMMON_CONFIG.eaDamage.get().floatValue();
        CommonConfig.excaliburDamage = ConfigSpecs.COMMON_CONFIG.excaliburDamage.get().floatValue();
        CommonConfig.caladBolgDmg = ConfigSpecs.COMMON_CONFIG.caladBolgDmg.get().floatValue();
        CommonConfig.magicBeam = ConfigSpecs.COMMON_CONFIG.magicBeam.get().floatValue();
        CommonConfig.gaeBolgDmg = ConfigSpecs.COMMON_CONFIG.gaeBolgDmg.get().floatValue();
        CommonConfig.gaeBolgEffect.readFromString(ConfigSpecs.COMMON_CONFIG.gaeBolgEffect.get());

        CommonConfig.excaliburMana = ConfigSpecs.COMMON_CONFIG.excaliburMana.get();
        CommonConfig.eaMana = ConfigSpecs.COMMON_CONFIG.eaMana.get();
        CommonConfig.archerBowMana = ConfigSpecs.COMMON_CONFIG.archerBowMana.get();
        CommonConfig.caladbolgMana = ConfigSpecs.COMMON_CONFIG.caladbolgMana.get();
        CommonConfig.gaeBolgMana = ConfigSpecs.COMMON_CONFIG.gaeBolgMana.get();
        CommonConfig.grimoireMana = ConfigSpecs.COMMON_CONFIG.grimoireMana.get();
        CommonConfig.chainMana = ConfigSpecs.COMMON_CONFIG.chainMana.get();
        CommonConfig.daggerThrowMana = ConfigSpecs.COMMON_CONFIG.daggerThrowMana.get();
        CommonConfig.staffMana = ConfigSpecs.COMMON_CONFIG.staffMana.get();

        CommonConfig.debugAttack = ConfigSpecs.COMMON_CONFIG.debugAttack.get();
    }
}
