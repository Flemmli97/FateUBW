package io.github.flemmli97.fateubw.common.registry;

import io.github.flemmli97.tenshilib.TenshiLib;

public class FateRegistration {

    public static void registerContent() {
        TenshiLib.registerSyncedRegistry();
        FateActivities.ACTIVITIES.registerContent();
        FateAttributes.ATTRIBUTES.registerContent();
        FateBlocks.BLOCK_ENTITIES.registerContent();
        FateBlocks.BLOCKS.registerContent();
        FateCreativeTab.TABS.registerContent();
        FateCriterionTriggers.TRIGGERS.registerContent();
        FateDataComponents.DATA_COMPONENTS.registerContent();
        FateEntities.ENTITIES.registerContent();
        FateGrailLootSerializer.LOOT_FUNCTION.registerContent();
        FateGrailLootSerializer.SERIALIZER.register().registerContent();
        FateItems.ITEMS.registerContent();
        FateItemSubPredicates.SUB_PREDICATES.registerContent();
        FateMemoryTypes.MEMORIES.registerContent();
        FateMobEffects.EFFECTS.registerContent();
        FateParticles.PARTICLES.registerContent();
        FateSounds.SOUND_EVENTS.registerContent();
    }
}
