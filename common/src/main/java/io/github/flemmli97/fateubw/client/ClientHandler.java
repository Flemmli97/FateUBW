package io.github.flemmli97.fateubw.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.authlib.GameProfile;
import io.github.flemmli97.fateubw.client.gui.CommandGui;
import io.github.flemmli97.fateubw.client.gui.GuiHolyGrail;
import io.github.flemmli97.fateubw.client.gui.ManaBar;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientHandler {

    private static ManaBar manaBar;
    public static KeyMapping gui;
    public static KeyMapping special;
    public static KeyMapping boost;
    public static KeyMapping target;
    public static List<GameProfile> grailPlayers = ImmutableList.of();
    public static Set<GameProfile> pending = ImmutableSet.of();
    public static Set<GameProfile> requests = ImmutableSet.of();
    public static Set<GameProfile> truce = ImmutableSet.of();
    public static int clientTick;

    private static final Comparator<GameProfile> sortName = Comparator.comparing(GameProfile::getName);

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
        return Minecraft.getInstance().isPaused() ? 0 : Minecraft.getInstance().getFrameTime();
    }

    public static void displayCommandGui() {
        Minecraft.getInstance().setScreen(new CommandGui());
    }

    public static void openGrailGui(Map<ResourceLocation, String> rewards) {
        Minecraft.getInstance().setScreen(new GuiHolyGrail(rewards));
    }

    public static void grailData(Set<GameProfile> set) {
        grailPlayers = ImmutableList.copyOf(set.stream().sorted(sortName).filter(prof -> prof.getId().equals(Minecraft.getInstance().player.getUUID())).iterator());
    }

    public static void truceData(Set<GameProfile> t, Set<GameProfile> p, Set<GameProfile> r) {
        truce = ImmutableSet.copyOf(t.stream().sorted(sortName).iterator());
        pending = ImmutableSet.copyOf(p.stream().sorted(sortName).iterator());
        requests = ImmutableSet.copyOf(r.stream().sorted(sortName).iterator());
    }
}
