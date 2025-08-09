package game.shaders;

import com.neutron.engine.func.Shader;
import com.neutron.engine.func.Vector2;

import java.awt.*;

public class CircleShader implements Shader {

    public Color shade(int x, int y, float u, float v, int w, int h) {
        double dist = Vector2.distance(x, y, w/2f, h/2f);
        float size = 50, offset = 10; //set offset = 1 for good anti-aliased look.

        if (dist >= size && dist <= size + offset) {
            float brightness = (float) (size+offset - dist)/offset;
            return new Color(brightness, brightness, brightness);
        } else if (dist < size) {
            return new Color(1f, 1f, 1f);
        }
        return new Color(0, 0, 0);
    }
}
