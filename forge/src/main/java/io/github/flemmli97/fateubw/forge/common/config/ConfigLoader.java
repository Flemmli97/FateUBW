package io.github.flemmli97.fateubw.forge.common.config;

import io.github.flemmli97.fateubw.common.config.Config;

public class ConfigLoader {

    public static void loadClient() {
        Config.Client.manaX = ConfigSpecs.CLIENT_CONF.manaBarX.get();
        Config.Client.manaY = ConfigSpecs.CLIENT_CONF.manaBarY.get();
    }

    public static void loadCommon() {
        Config.Common.minPlayer = ConfigSpecs.COMMON_CONF.minPlayer.get();
        Config.Common.maxPlayer = ConfigSpecs.COMMON_CONF.maxPlayer.get();
        Config.Common.joinTime = ConfigSpecs.COMMON_CONF.joinTime.get();
        Config.Common.rewardDelay = ConfigSpecs.COMMON_CONF.rewardDelay.get();
        Config.Common.charmSpawnRate = ConfigSpecs.COMMON_CONF.charmSpawnRate.get();
        Config.Common.gemSpawnRate = ConfigSpecs.COMMON_CONF.gemSpawnRate.get();
        Config.Common.allowDuplicateServant = ConfigSpecs.COMMON_CONF.allowDuplicateServant.get();
        Config.Common.allowDuplicateClass = ConfigSpecs.COMMON_CONF.allowDuplicateClass.get();
        Config.Common.fillMissingSlots = ConfigSpecs.COMMON_CONF.fillMissingSlots.get();
        Config.Common.maxServantCircle = ConfigSpecs.COMMON_CONF.maxServantCircle.get();
        Config.Common.servantMinSpawnDelay = ConfigSpecs.COMMON_CONF.servantMinSpawnDelay.get();
        Config.Common.servantMaxSpawnDelay = ConfigSpecs.COMMON_CONF.servantMaxSpawnDelay.get();
        Config.Common.punishTeleport = ConfigSpecs.COMMON_CONF.punishTeleport.get();
        Config.Common.notifyBlackList = ConfigSpecs.COMMON_CONF.notifyBlackList.get();
        Config.Common.notificationWhitelist = ConfigSpecs.COMMON_CONF.whiteList.get();
        Config.Common.notifyAll = ConfigSpecs.COMMON_CONF.notifyAll.get();
        Config.Common.npBoostEffect.readFromString(ConfigSpecs.COMMON_CONF.npBoostEffect.get());

        Config.Common.lancelotReflectChance = ConfigSpecs.COMMON_CONF.lancelotReflectChance.get().floatValue();
        Config.Common.hassanCopies = ConfigSpecs.COMMON_CONF.hassanCopies.get();
        //Minions
        Config.Common.gillesMinionDuration = ConfigSpecs.COMMON_CONF.gillesMinionDuration.get();
        Config.Common.gillesMinionAmount = ConfigSpecs.COMMON_CONF.gillesMinionAmount.get();
        Config.Common.smallMonsterDamage = ConfigSpecs.COMMON_CONF.smallMonsterDamage.get().floatValue();
        Config.Common.babylonScale = ConfigSpecs.COMMON_CONF.babylonScale.get().floatValue();
        Config.Common.eaDamage = ConfigSpecs.COMMON_CONF.eaDamage.get().floatValue();
        Config.Common.excaliburDamage = ConfigSpecs.COMMON_CONF.excaliburDamage.get().floatValue();
        Config.Common.caladBolgDmg = ConfigSpecs.COMMON_CONF.caladBolgDmg.get().floatValue();
        Config.Common.magicBeam = ConfigSpecs.COMMON_CONF.magicBeam.get().floatValue();
        Config.Common.gaeBolgDmg = ConfigSpecs.COMMON_CONF.gaeBolgDmg.get().floatValue();
        Config.Common.gaeBolgEffect.readFromString(ConfigSpecs.COMMON_CONF.gaeBolgEffect.get());
        Config.Common.gordiusHealth = ConfigSpecs.COMMON_CONF.gordiusHealth.get();
        Config.Common.gordiusDmg = ConfigSpecs.COMMON_CONF.gordiusDmg.get().floatValue();
        Config.Common.pegasusHealth = ConfigSpecs.COMMON_CONF.pegasusHealth.get();
        Config.Common.pegasusDamage = ConfigSpecs.COMMON_CONF.pegasusDamage.get().floatValue();
        Config.Common.medeaCircleSpan = ConfigSpecs.COMMON_CONF.medeaCircleSpan.get();
        Config.Common.medeaCircleRange = ConfigSpecs.COMMON_CONF.medeaCircleRange.get().floatValue();

        Config.Common.debugAttack = ConfigSpecs.COMMON_CONF.debugAttack.get();
    }
}
