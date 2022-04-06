package io.github.flemmli97.fateubw.common.integration;

import net.minecraft.server.level.ServerPlayer;

public class AstralSorcery {

    private static final String grailPerkID = "grail_perk";

    public static void grantFreePerkPoint(ServerPlayer player) {
        /*int i = 0;
        String id = grailPerkID + "_" + i;
        while (!ResearchManager.grantFreePerkPoint(player, new ResourceLocation(Fate.MODID, id))) {
            i++;
            id = grailPerkID + "_" + i;
        }*/
    }

    public static void resetPerkPoints(ServerPlayer player) {
        /*Collection<ResourceLocation> ress = ResearchHelper.getProgress(player, LogicalSide.SERVER).getPerkData()
                .getFreePointTokens().stream().filter(res -> res.getNamespace().equals(Fate.MODID)).collect(Collectors.toList());
        ress.forEach(res -> ResearchManager.revokeFreePoint(player, res));*/
    }
}
