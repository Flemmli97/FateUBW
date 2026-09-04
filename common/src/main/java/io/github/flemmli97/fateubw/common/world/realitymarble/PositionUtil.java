package io.github.flemmli97.fateubw.common.world.realitymarble;

public class PositionUtil {

    public static final int SPACING = 5000;

    public static int[] spiralCoord(int i) {
        i += 1;
        int ring = (int) Math.ceil((Math.sqrt(i) - 1) / 2);
        int s = Math.max(0, 2 * ring - 1);
        int ringStart = s * s;
        int ringIdx = ring - ringStart;
        // North
        int x, z;
        if (ringIdx < 2 * ring) {
            x = -ring + ringIdx;
            z = -ring;
        } else if (ringIdx < 4 * ring) {
            x = ring;
            z = -ring + (ringIdx - 2 * ring);
        } else if (ringIdx < 6 * ring) {
            x = ring - (ringIdx - 4 * ring);
            z = ring;
        } else {
            x = -ring;
            z = ring - (ringIdx - 6 * ring);
        }
        return new int[]{x, z};
    }
}
