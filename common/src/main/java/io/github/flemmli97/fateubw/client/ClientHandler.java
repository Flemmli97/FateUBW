package io.github.flemmli97.fateubw.client;

import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fateubw.client.gui.CommandGui;
import io.github.flemmli97.fateubw.client.gui.GuiHolyGrail;
import io.github.flemmli97.fateubw.client.gui.ManaBar;
import io.github.flemmli97.fateubw.client.gui.SpawnEggScreen;
import io.github.flemmli97.fateubw.client.gui.TeamGui;
import io.github.flemmli97.fateubw.common.network.C2SServantCommand;
import io.github.flemmli97.fateubw.common.network.C2STeamMessage;
import io.github.flemmli97.fateubw.common.network.S2CServantGui;
import io.github.flemmli97.fateubw.common.world.GrailTeam;
import io.github.flemmli97.fateubw.platform.NetworkCalls;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.Map;

public class ClientHandler {

    private static ManaBar manaBar;
    public static KeyMapping gui;
    public static KeyMapping special;
    public static KeyMapping boost;
    public static KeyMapping target;

    public static int clientTick;

    private static boolean paused;
    private static float pausedPartial;

    private static final Comparator<GameProfile> SORT_NAME = Comparator.comparing(GameProfile::getName);

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
        boolean isPaused = Minecraft.getInstance().isPaused();
        if (isPaused && !paused) {
            pausedPartial = Minecraft.getInstance().getFrameTime();
        }
        paused = isPaused;
        return isPaused ? pausedPartial : Minecraft.getInstance().getFrameTime();
    }

    public static void displayCommandGui(S2CServantGui.ServantMetaData data, boolean open) {
        if (Minecraft.getInstance().screen instanceof CommandGui teamGui) {
            teamGui.update(data);
        } else if (open)
            Minecraft.getInstance().setScreen(new CommandGui(data));
        else
            NetworkCalls.INSTANCE.sendToServer(new C2SServantCommand(C2SServantCommand.Type.CLOSE, data.entityId()));
    }

    public static void openGrailGui(Map<ResourceLocation, Component> rewards) {
        Minecraft.getInstance().setScreen(new GuiHolyGrail(rewards));
    }

    public static void openSpawneggGui(InteractionHand hand) {
        Minecraft.getInstance().setScreen(new SpawnEggScreen(hand));
    }

    public static void openTeamGui(boolean open, GrailTeam.ClientTeamInfo info) {
        if (Minecraft.getInstance().screen instanceof TeamGui teamGui) {
            teamGui.update(info, true);
        } else if (open)
            Minecraft.getInstance().setScreen(new TeamGui(info));
        else
            NetworkCalls.INSTANCE.sendToServer(new C2STeamMessage(C2STeamMessage.Type.CLOSE, ""));
    }
}
