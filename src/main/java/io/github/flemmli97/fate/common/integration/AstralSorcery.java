package io.github.flemmli97.fate.common.integration;

import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import io.github.flemmli97.fate.Fate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;

import java.util.Collection;
import java.util.stream.Collectors;

public class AstralSorcery {

    private static final String grailPerkID = "grail_perk";

    public static void grantFreePerkPoint(ServerPlayerEntity player) {
        int i = 0;
        String id = grailPerkID + "_" + i;
        while (!ResearchManager.grantFreePerkPoint(player, new ResourceLocation(Fate.MODID, id))) {
            i++;
            id = grailPerkID + "_" + i;
        }
    }

    public static void resetPerkPoints(ServerPlayerEntity player) {
        Collection<ResourceLocation> ress = ResearchHelper.getProgress(player, LogicalSide.SERVER).getPerkData()
                .getFreePointTokens().stream().filter(res -> res.getNamespace().equals(Fate.MODID)).collect(Collectors.toList());
        ress.forEach(res -> ResearchManager.revokeFreePoint(player, res));
    }
}
