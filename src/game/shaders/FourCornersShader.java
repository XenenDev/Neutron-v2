package game.shaders;

import com.neutron.engine.func.Shader;
import com.neutron.engine.func.Vector2;

import java.awt.*;

public class FourCornersShader implements Shader {

    public Color shade(int x, int y, float u, float v, int w, int h) {
        Color col1 = new Color(0, 184, 255); // top-left
        Color col2 = new Color(208, 0, 255); // top-right
        Color col3 = new Color(115, 255, 0); // bottom-left
        Color col4 = new Color(248, 97, 3); // bottom-right

        Color int1 = new Color(
                (int) Vector2.sqrtLerp(col1.getRed(), col2.getRed(), u),
                (int) Vector2.sqrtLerp(col1.getGreen(), col2.getGreen(), u),
                (int) Vector2.sqrtLerp(col1.getBlue(), col2.getBlue(), u)
        );

        Color int2 = new Color(
                (int) Vector2.sqrtLerp(col3.getRed(), col4.getRed(), u),
                (int) Vector2.sqrtLerp(col3.getGreen(), col4.getGreen(), u),
                (int) Vector2.sqrtLerp(col3.getBlue(), col4.getBlue(), u)
        );

        return new Color(
                (int) Vector2.sqrtLerp(int1.getRed(), int2.getRed(), v),
                (int) Vector2.sqrtLerp(int1.getGreen(), int2.getGreen(), v),
                (int) Vector2.sqrtLerp(int1.getBlue(), int2.getBlue(), v)
        );
    }
}
