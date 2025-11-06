package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.BaseGame;
import com.neutron.engine.func.GraphicsFidelity;

import java.awt.*;

public class Game extends BaseGame {
    public static void main(String[] args) {
        new GameCore(
                new Game(),
                "Neutron V2 Game Engine Test",
                800,
                600,
                "res/icon.png",
                new GraphicsFidelity() {
                    public boolean useGlobalAA() {
                        return true;
                    }
                    public boolean useQualityRendering() {
                        return true;
                    }
                    public boolean useBilinearSampling() {
                        return true;
                    }
                    public boolean useSubPixelFontRendering() {
                        return false;
                    }
                    public boolean useAAForTextOnly() {
                        return false;
                    }
                }
        );
    }

    public void play(GameCore gameCore, Renderer r) {
        new HUD();
        new Player();
        for (int i = 0; i < 4; i++) {
            new Wall(250 * (i+1));
        }

        r.setFont(new Font("Consolas", Font.PLAIN, 25));
    }

    public void update(GameCore gameCore, Renderer r, float delta) {

    }

}
