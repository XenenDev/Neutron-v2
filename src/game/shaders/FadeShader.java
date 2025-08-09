package game.shaders;

import com.neutron.engine.func.Shader;

import java.awt.*;

public class FadeShader implements Shader {
    public Color shade(int x, int y, float u, float v, int w, int h) {
        return new Color(1, 1, 1, u*u*u);

    }
}
