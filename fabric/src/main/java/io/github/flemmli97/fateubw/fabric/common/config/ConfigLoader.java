package io.github.flemmli97.fateubw.fabric.common.config;

import io.github.flemmli97.fateubw.common.config.Config;

import java.util.Map;

public class ConfigLoader {

    public static void loadClient() {
        Config.Client.manaX = ConfigSpecs.clientConf.manaBarX.get();
        Config.Client.manaY = ConfigSpecs.clientConf.manaBarY.get();
    }

    public static void loadCommon() {
        Config.Common.minPlayer = ConfigSpecs.commonConf.minPlayer.get();
        Config.Common.maxPlayer = ConfigSpecs.commonConf.maxPlayer.get();
        Config.Common.joinTime = ConfigSpecs.commonConf.joinTime.get();
        Config.Common.rewardDelay = ConfigSpecs.commonConf.rewardDelay.get();
        Config.Common.charmSpawnRate = ConfigSpecs.commonConf.charmSpawnRate.get();
        Config.Common.gemSpawnRate = ConfigSpecs.commonConf.gemSpawnRate.get();
        Config.Common.allowDuplicateServant = ConfigSpecs.commonConf.allowDuplicateServant.get();
        Config.Common.allowDuplicateClass = ConfigSpecs.commonConf.allowDuplicateClass.get();
        Config.Common.fillMissingSlots = ConfigSpecs.commonConf.fillMissingSlots.get();
        Config.Common.maxServantCircle = ConfigSpecs.commonConf.maxServantCircle.get();
        Config.Common.servantMinSpawnDelay = ConfigSpecs.commonConf.servantMinSpawnDelay.get();
        Config.Common.servantMaxSpawnDelay = ConfigSpecs.commonConf.servantMaxSpawnDelay.get();
        Config.Common.punishTeleport = ConfigSpecs.commonConf.punishTeleport.get();
        Config.Common.notifyBlackList = ConfigSpecs.commonConf.notifyBlackList.get();
        Config.Common.whiteList = ConfigSpecs.commonConf.whiteList.get();
        Config.Common.notifyAll = ConfigSpecs.commonConf.notifyAll.get();
        Config.Common.npBoostEffect.readFromString(ConfigSpecs.commonConf.npBoostEffect.get());

        Config.Common.attributes.clear();
        for (Map.Entry<String, ServantConfSpec> e : ConfigSpecs.commonConf.attributes.entrySet())
            Config.Common.attributes.put(e.getKey(), ServantConfSpec.read(e.getValue()));
        Config.Common.lancelotReflectChance = ConfigSpecs.commonConf.lancelotReflectChance.get().floatValue();
        Config.Common.hassanCopies = ConfigSpecs.commonConf.hassanCopies.get();
        //Minions
        Config.Common.gillesMinionDuration = ConfigSpecs.commonConf.gillesMinionDuration.get();
        Config.Common.gillesMinionAmount = ConfigSpecs.commonConf.gillesMinionAmount.get();
        Config.Common.smallMonsterDamage = ConfigSpecs.commonConf.smallMonsterDamage.get().floatValue();
        Config.Common.babylonScale = ConfigSpecs.commonConf.babylonScale.get().floatValue();
        Config.Common.eaDamage = ConfigSpecs.commonConf.eaDamage.get().floatValue();
        Config.Common.excaliburDamage = ConfigSpecs.commonConf.excaliburDamage.get().floatValue();
        Config.Common.caladBolgDmg = ConfigSpecs.commonConf.caladBolgDmg.get().floatValue();
        Config.Common.magicBeam = ConfigSpecs.commonConf.magicBeam.get().floatValue();
        Config.Common.gaeBolgDmg = ConfigSpecs.commonConf.gaeBolgDmg.get().floatValue();
        Config.Common.gaeBolgEffect.readFromString(ConfigSpecs.commonConf.gaeBolgEffect.get());
        Config.Common.gordiusHealth = ConfigSpecs.commonConf.gordiusHealth.get();
        Config.Common.gordiusDmg = ConfigSpecs.commonConf.gordiusDmg.get().floatValue();
        Config.Common.pegasusHealth = ConfigSpecs.commonConf.pegasusHealth.get();
        Config.Common.medeaCircleSpan = ConfigSpecs.commonConf.medeaCircleSpan.get();
        Config.Common.medeaCircleRange = ConfigSpecs.commonConf.medeaCircleRange.get().floatValue();
        Config.Common.hassanCopyProps = ServantConfSpec.read(ConfigSpecs.commonConf.hassanCopyProps);
    }
}
