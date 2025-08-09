package game.shaders;

import com.neutron.engine.func.Shader;

import java.awt.*;

public class UVShader implements Shader {
    public Color shade(int x, int y, float u, float v, int w, int h) {
        return new Color(u, v, 0);
    }
}
