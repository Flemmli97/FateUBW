package io.github.flemmli97.fateubw.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class ShakeHandler {

    public static int shakeTick;
    public static float shakeStrength = 2;

    public static void shakeScreen(Vec3 pos, double maxDist, int time, float strength) {
        Vec3 player = Minecraft.getInstance().player.position();
        if (pos.distanceToSqr(player) > maxDist * maxDist)
            return;
        shakeScreen(time, strength);
    }

    public static void shakeScreen(int time, float strength) {
        if (shakeTick < time) {
            shakeTick = time;
            shakeStrength = strength;
        }
    }

    public static void renderShaking(Camera camera, float yaw, float pitch, float roll, float partialTicks,
                                     Consumer<Float> setYaw, Consumer<Float> setPitch, Consumer<Float> setRoll) {
        int t = ShakeHandler.shakeTick;
        if (t <= 0)
            return;
        float strengthPitch = ShakeHandler.shakeStrength;
        float strengthRoll = ShakeHandler.shakeStrength;
        float pT = t * 24 - partialTicks;
        setPitch.accept(pitch + Mth.sin(pT * 2) * strengthPitch);
        setRoll.accept(roll + Mth.sin(pT) * strengthRoll);
    }
}
