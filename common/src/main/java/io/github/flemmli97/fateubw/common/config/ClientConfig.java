package io.github.flemmli97.fateubw.common.config;

public class ClientConfig {

    public static int manaX = 8;
    public static int manaY = 8;
    public static DisplayPosition manaBarPosition = DisplayPosition.BOTTOMLEFT;

    public static float screenShakeIntensity = 1;

    public enum DisplayPosition {

        TOPLEFT,
        TOPMIDDLE,
        TOPRIGHT,
        MIDDLELEFT,
        MIDDLERIGHT,
        BOTTOMLEFT,
        BOTTOMRIGHT;

        public int positionX(int screenWidth, int componentWidth, int componentX) {
            return switch (this) {
                case TOPLEFT, MIDDLELEFT, BOTTOMLEFT -> componentX;
                case TOPMIDDLE, MIDDLERIGHT -> (int) (screenWidth * 0.5 - componentWidth * 0.5) + componentX;
                case TOPRIGHT, BOTTOMRIGHT -> screenWidth - componentWidth - componentX;
            };
        }

        public int positionY(int screenHeight, int componentHeight, int componentY) {
            return switch (this) {
                case TOPLEFT, TOPMIDDLE, TOPRIGHT -> componentY;
                case MIDDLELEFT, MIDDLERIGHT -> (int) (screenHeight * 0.5 - componentHeight * 0.5) + componentY;
                case BOTTOMLEFT, BOTTOMRIGHT -> screenHeight - componentHeight - componentY;
            };
        }
    }
}
