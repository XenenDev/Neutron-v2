package com.neutron.engine.func;

/**
 * Defines anchor points for screen-space coordinate positioning.
 * <p>
 * When using screen coordinates in the Renderer, coordinates can be relative to
 * one of 9 anchor points on the screen. This is useful for UI elements that need
 * to stay positioned relative to screen edges or corners regardless of resolution.
 * </p>
 * <pre>
 * ┌─────────────────────────────────────┐
 * │ TOP_LEFT    TOP_CENTER    TOP_RIGHT │
 * │                                     │
 * │ CENTER_LEFT   CENTER   CENTER_RIGHT │
 * │                                     │
 * │ BOTTOM_LEFT BOTTOM_CENTER BOTTOM_RIGHT│
 * └─────────────────────────────────────┘
 * </pre>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * renderer.setUseScreenCoordinates(true);
 * renderer.setScreenAnchor(ScreenAnchor.TOP_LEFT);
 * renderer.drawText("Score: 100", 10, 30, Color.WHITE); // 10px from left, 30px from top
 *
 * renderer.setScreenAnchor(ScreenAnchor.BOTTOM_RIGHT);
 * renderer.drawText("Lives: 3", -100, -20, Color.WHITE); // 100px from right, 20px from bottom
 * </pre>
 */
public enum ScreenAnchor {
    /**
     * Anchor to the top-left corner of the screen (default).
     * Coordinates are offset from (0, 0).
     */
    TOP_LEFT(0f, 0f),

    /**
     * Anchor to the top-center of the screen.
     * Coordinates are offset from (width/2, 0).
     */
    TOP_CENTER(0.5f, 0f),

    /**
     * Anchor to the top-right corner of the screen.
     * Coordinates are offset from (width, 0).
     */
    TOP_RIGHT(1f, 0f),

    /**
     * Anchor to the center-left of the screen.
     * Coordinates are offset from (0, height/2).
     */
    CENTER_LEFT(0f, 0.5f),

    /**
     * Anchor to the center of the screen.
     * Coordinates are offset from (width/2, height/2).
     */
    CENTER(0.5f, 0.5f),

    /**
     * Anchor to the center-right of the screen.
     * Coordinates are offset from (width, height/2).
     */
    CENTER_RIGHT(1f, 0.5f),

    /**
     * Anchor to the bottom-left corner of the screen.
     * Coordinates are offset from (0, height).
     */
    BOTTOM_LEFT(0f, 1f),

    /**
     * Anchor to the bottom-center of the screen.
     * Coordinates are offset from (width/2, height).
     */
    BOTTOM_CENTER(0.5f, 1f),

    /**
     * Anchor to the bottom-right corner of the screen.
     * Coordinates are offset from (width, height).
     */
    BOTTOM_RIGHT(1f, 1f);

    private final float xFactor;
    private final float yFactor;

    ScreenAnchor(float xFactor, float yFactor) {
        this.xFactor = xFactor;
        this.yFactor = yFactor;
    }

    /**
     * Calculates the X offset for this anchor point given the screen width.
     *
     * @param screenWidth the width of the screen
     * @return the X offset from the screen origin
     */
    public int getXOffset(int screenWidth) {
        return (int) (screenWidth * xFactor);
    }

    /**
     * Calculates the Y offset for this anchor point given the screen height.
     *
     * @param screenHeight the height of the screen
     * @return the Y offset from the screen origin
     */
    public int getYOffset(int screenHeight) {
        return (int) (screenHeight * yFactor);
    }

    /**
     * Gets the horizontal factor (0.0 = left, 0.5 = center, 1.0 = right).
     *
     * @return the horizontal factor
     */
    public float getXFactor() {
        return xFactor;
    }

    /**
     * Gets the vertical factor (0.0 = top, 0.5 = center, 1.0 = bottom).
     *
     * @return the vertical factor
     */
    public float getYFactor() {
        return yFactor;
    }
}
