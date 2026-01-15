package game;

import com.neutron.engine.GameCore;
import com.neutron.engine.ObjectHandler;
import com.neutron.engine.Renderer;
import com.neutron.engine.base.BaseGame;
import com.neutron.engine.func.GraphicsFidelity;
import game.primitives.Block;
import game.primitives.Spike;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends BaseGame {

    private final List<Long> obstacleIds = new ArrayList<>();

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
        for (long id : obstacleIds) {
            ObjectHandler.removeObjectById(id);
        }
        obstacleIds.clear();

        for (int x = 0; x <= 30; x++) {
            double type = Math.random();
            double spawn_chance = 0.6;
            double block_chance = 1;
            if (type < spawn_chance) {
                if (type < (block_chance * spawn_chance)) {
                    obstacleIds.add(new Block(12+x, 9, 5).getId());
                } else {
                    obstacleIds.add(new Spike(12+x, 9, 5).getId());
                }
            }
        }
    }

    public void update(GameCore gameCore, Renderer r, float delta) {

    }

}
