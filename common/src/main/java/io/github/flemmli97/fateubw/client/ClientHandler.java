package io.github.flemmli97.fateubw.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.fateubw.client.screen.CommandScreen;
import io.github.flemmli97.fateubw.client.screen.HolyGrailScreen;
import io.github.flemmli97.fateubw.client.screen.ManaBar;
import io.github.flemmli97.fateubw.client.screen.SpawnEggScreen;
import io.github.flemmli97.fateubw.client.screen.TeamScreen;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.common.network.C2STeamMessage;
import io.github.flemmli97.fateubw.common.network.S2CServantGui;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class ClientHandler {

    private static ManaBar manaBar;
    public static KeyMapping gui;
    public static KeyMapping special;
    public static KeyMapping boost;
    public static KeyMapping target;

    public static int clientTick;

    public static ManaBar getManaBar() {
        if (manaBar == null) {
            manaBar = new ManaBar(Minecraft.getInstance());
        }
        return manaBar;
    }

    public static Player clientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static float getPartialTicks() {
        return Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
    }

    public static void displayCommandGui(S2CServantGui.ServantMetaData data, boolean open) {
        if (Minecraft.getInstance().screen instanceof CommandScreen teamGui) {
            teamGui.update(data);
        } else if (open)
            Minecraft.getInstance().setScreen(new CommandScreen(data));
        else
            LoaderNetwork.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.ActionType.CLOSE, data.entityId()));
    }

    public static void openGrailGui(Map<ResourceLocation, Component> rewards) {
        Minecraft.getInstance().setScreen(new HolyGrailScreen(rewards));
    }

    public static void openSpawneggGui(InteractionHand hand) {
        Minecraft.getInstance().setScreen(new SpawnEggScreen(hand));
    }

    public static void openTeamGui(boolean open, GrailTeam.ClientTeamInfo info) {
        if (Minecraft.getInstance().screen instanceof TeamScreen teamScreen) {
            teamScreen.update(info, true);
        } else if (open)
            Minecraft.getInstance().setScreen(new TeamScreen(info));
        else
            LoaderNetwork.INSTANCE.sendToServer(new C2STeamMessage(C2STeamMessage.Type.CLOSE, ""));
    }

    public static void translateRider(PoseStack poseStack, LivingEntity entity, Entity rider) {
        Vec3 attach = rider.getVehicleAttachmentPoint(entity);
        float scale = entity.getScale();
        poseStack.scale(1 / scale, 1 / scale, 1 / scale);
        poseStack.translate(attach.x(), attach.y(), attach.z());
    }
}
