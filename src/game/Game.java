package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.BaseGame;
import com.neutron.engine.func.GraphicsFidelity;

import java.awt.*;

public class Game extends BaseGame {

    private final LevelGenerator levelGenerator = new LevelGenerator();

    public static void main(String[] args) {
        new GameCore(
                new Game(),
                "Neutron V2 Game Engine Demo",
                1000,
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
        //r.setRenderColliders(true);
        r.setFont(new Font("Consolas", Font.PLAIN, 25));

        new HUD();
        new Player(this);
        new Ground();

        restart();
    }

    public void restart() {
        levelGenerator.generate();
    }

    public void update(GameCore gameCore, Renderer r, float delta) {
        levelGenerator.update(delta);
    }

}
