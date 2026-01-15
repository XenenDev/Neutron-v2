package game.shaders;

import com.neutron.engine.func.Shader;

import java.awt.*;

public class GradientShader implements Shader {

    private final Color topColor;
    private final Color bottomColor;

    public GradientShader(Color topColor, Color bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
    }

    @Override
    public Color shade(int x, int y, float u, float v, int w, int h) {
        // v goes from 0 (top) to 1 (bottom)
        // Interpolate between top and bottom colors
        float r = topColor.getRed() / 255f + (bottomColor.getRed() / 255f - topColor.getRed() / 255f) * v;
        float g = topColor.getGreen() / 255f + (bottomColor.getGreen() / 255f - topColor.getGreen() / 255f) * v;
        float b = topColor.getBlue() / 255f + (bottomColor.getBlue() / 255f - topColor.getBlue() / 255f) * v;
        
        return new Color(
            Math.max(0f, Math.min(1f, r)),
            Math.max(0f, Math.min(1f, g)),
            Math.max(0f, Math.min(1f, b))
        );
    }
}
