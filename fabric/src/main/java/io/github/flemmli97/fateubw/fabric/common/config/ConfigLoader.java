package io.github.flemmli97.fateubw.fabric.common.config;

import io.github.flemmli97.fateubw.common.config.Config;

public class ConfigLoader {

    public static void loadClient() {
        Config.Client.manaX = ConfigSpecs.CLIENT_CONFIG.manaBarX.get();
        Config.Client.manaY = ConfigSpecs.CLIENT_CONFIG.manaBarY.get();
    }

    public static void loadCommon() {
        Config.Common.minPlayer = ConfigSpecs.COMMON_CONFIG.minPlayer.get();
        Config.Common.maxPlayer = ConfigSpecs.COMMON_CONFIG.maxPlayer.get();
        Config.Common.joinTime = ConfigSpecs.COMMON_CONFIG.joinTime.get();
        Config.Common.rewardDelay = ConfigSpecs.COMMON_CONFIG.rewardDelay.get();
        Config.Common.charmSpawnRate = ConfigSpecs.COMMON_CONFIG.charmSpawnRate.get();
        Config.Common.gemSpawnRate = ConfigSpecs.COMMON_CONFIG.gemSpawnRate.get();
        Config.Common.allowDuplicateServant = ConfigSpecs.COMMON_CONFIG.allowDuplicateServant.get();
        Config.Common.allowDuplicateClass = ConfigSpecs.COMMON_CONFIG.allowDuplicateClass.get();
        Config.Common.fillMissingSlots = ConfigSpecs.COMMON_CONFIG.fillMissingSlots.get();
        Config.Common.maxServantCircle = ConfigSpecs.COMMON_CONFIG.maxServantCircle.get();
        Config.Common.servantMinSpawnDelay = ConfigSpecs.COMMON_CONFIG.servantMinSpawnDelay.get();
        Config.Common.servantMaxSpawnDelay = ConfigSpecs.COMMON_CONFIG.servantMaxSpawnDelay.get();
        Config.Common.punishTeleport = ConfigSpecs.COMMON_CONFIG.punishTeleport.get();
        Config.Common.notifyBlackList = ConfigSpecs.COMMON_CONFIG.notifyBlackList.get();
        Config.Common.notificationWhitelist = ConfigSpecs.COMMON_CONFIG.whiteList.get();
        Config.Common.notifyAll = ConfigSpecs.COMMON_CONFIG.notifyAll.get();
        Config.Common.npBoostEffect.readFromString(ConfigSpecs.COMMON_CONFIG.npBoostEffect.get());

        Config.Common.lancelotReflectChance = ConfigSpecs.COMMON_CONFIG.lancelotReflectChance.get().floatValue();
        Config.Common.hassanCopies = ConfigSpecs.COMMON_CONFIG.hassanCopies.get();
        //Minions
        Config.Common.gillesMinionDuration = ConfigSpecs.COMMON_CONFIG.gillesMinionDuration.get();
        Config.Common.gillesMinionAmount = ConfigSpecs.COMMON_CONFIG.gillesMinionAmount.get();
        Config.Common.smallMonsterDamage = ConfigSpecs.COMMON_CONFIG.smallMonsterDamage.get().floatValue();
        Config.Common.babylonScale = ConfigSpecs.COMMON_CONFIG.babylonScale.get().floatValue();
        Config.Common.babylonWeapons.readFromString(ConfigSpecs.COMMON_CONFIG.babylonWeaponsBlacklist.get());
        Config.Common.babylonWeapons.setWhiteList(ConfigSpecs.COMMON_CONFIG.babylonWeaponsWhitelist.get());
        Config.Common.eaDamage = ConfigSpecs.COMMON_CONFIG.eaDamage.get().floatValue();
        Config.Common.excaliburDamage = ConfigSpecs.COMMON_CONFIG.excaliburDamage.get().floatValue();
        Config.Common.caladBolgDmg = ConfigSpecs.COMMON_CONFIG.caladBolgDmg.get().floatValue();
        Config.Common.magicBeam = ConfigSpecs.COMMON_CONFIG.magicBeam.get().floatValue();
        Config.Common.gaeBolgDmg = ConfigSpecs.COMMON_CONFIG.gaeBolgDmg.get().floatValue();
        Config.Common.gaeBolgEffect.readFromString(ConfigSpecs.COMMON_CONFIG.gaeBolgEffect.get());
        Config.Common.gordiusHealth = ConfigSpecs.COMMON_CONFIG.gordiusHealth.get();
        Config.Common.gordiusDmg = ConfigSpecs.COMMON_CONFIG.gordiusDmg.get().floatValue();
        Config.Common.pegasusHealth = ConfigSpecs.COMMON_CONFIG.pegasusHealth.get();
        Config.Common.pegasusDamage = ConfigSpecs.COMMON_CONFIG.pegasusDamage.get().floatValue();
        Config.Common.medeaCircleSpan = ConfigSpecs.COMMON_CONFIG.medeaCircleSpan.get();
        Config.Common.medeaCircleRange = ConfigSpecs.COMMON_CONFIG.medeaCircleRange.get().floatValue();

        Config.Common.excaliburMana = ConfigSpecs.COMMON_CONFIG.excaliburMana.get();
        Config.Common.eaMana = ConfigSpecs.COMMON_CONFIG.eaMana.get();
        Config.Common.archerBowMana = ConfigSpecs.COMMON_CONFIG.archerBowMana.get();
        Config.Common.caladbolgMana = ConfigSpecs.COMMON_CONFIG.caladbolgMana.get();
        Config.Common.gaeBolgMana = ConfigSpecs.COMMON_CONFIG.gaeBolgMana.get();
        Config.Common.grimoireMana = ConfigSpecs.COMMON_CONFIG.grimoireMana.get();
        Config.Common.chainMana = ConfigSpecs.COMMON_CONFIG.chainMana.get();
        Config.Common.daggerThrowMana = ConfigSpecs.COMMON_CONFIG.daggerThrowMana.get();

        Config.Common.debugAttack = ConfigSpecs.COMMON_CONFIG.debugAttack.get();
    }
}
