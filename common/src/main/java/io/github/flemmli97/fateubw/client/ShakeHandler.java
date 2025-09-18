package io.github.flemmli97.fateubw.client;

import io.github.flemmli97.fateubw.common.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

import java.util.Random;
import java.util.function.Consumer;

public class ShakeHandler {

    public static int shakeTick;
    public static float shakeStrength = 2;
    private static final Random RANDOM = new Random();

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

    public static void renderShaking(float yaw, float pitch, float roll, float partialTicks,
                                     Consumer<Float> setYaw, Consumer<Float> setPitch, Consumer<Float> setRoll) {
        int t = ShakeHandler.shakeTick;
        if (t <= 0)
            return;
        float strengthPitch = ShakeHandler.shakeStrength * ClientConfig.screenShakeIntensity;
        float strengthRoll = ShakeHandler.shakeStrength * ClientConfig.screenShakeIntensity;
        setPitch.accept(pitch + RANDOM.nextFloat(-1, 1) * strengthPitch);
        setRoll.accept(roll + RANDOM.nextFloat(-1, 1) * strengthRoll);
    }
}
