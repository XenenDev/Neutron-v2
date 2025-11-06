package game;

import com.neutron.engine.*;
import com.neutron.engine.base.*;
import com.neutron.engine.base.interfaces.*;
import com.neutron.engine.func.Collider;
import com.neutron.engine.func.Resource;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;

public class Player extends GameObject implements ObjectRenderer, MouseWheelInput, MouseButtonInput, MouseMovement, Collidable {
    private int x, y;
    private int jumpHeight;
    private float vx, vy;
    private float scale;
    private int score;
    private GameCore gameCore;

    Resource pop = new Resource("res/sound.wav");
    Resource bird = new Resource("res/bird.png");

    public void play(GameCore gameCore) {
        this.x = 40;
        this.y = 100;
        this.vx = 0;
        this.vy = 0;
        this.scale = 1f;
        this.score = 0;
        
        this.gameCore = gameCore;
        this.jumpHeight = 5;
    }

    public void update(GameCore gameCore, float delta) {
        x += (int) vx;
        y += (int) vy;
        vy += 0.2f;

        if (y >= gameCore.HEIGHT || y < -50) {
            this.die();
        }

        if (Input.isKeyDown(KeyEvent.VK_LEFT)) gameCore.getRenderer().moveCameraPos(-3, 0);
        if (Input.isKeyDown(KeyEvent.VK_RIGHT)) gameCore.getRenderer().moveCameraPos(3, 0);
        if (Input.isKeyDown(KeyEvent.VK_UP)) gameCore.getRenderer().moveCameraPos(0, -3);
        if (Input.isKeyDown(KeyEvent.VK_DOWN)) gameCore.getRenderer().moveCameraPos(0, 3);
    }

    private void die() {
        this.x = 40;
        this.y = 100;
        this.vx = 0;
        this.vy = 0;
        this.scale = 1f;
        this.score = 0;

        for (GameObject wall : ObjectHandler.get(Wall.class)) {
            wall.delete();
        }

        for (int i = 0; i < 4; i++) {
            new Wall(250 * (i+1));
        }
    }

    public void render(GameCore gameCore, Renderer r) {
        //r.fillSquare(0, 0, 50, Color.CYAN);
        //r.shadeLight(0, 0, 10, Color.yellow, (x) -> x*x, 200);
        r.drawImage((Image) bird.get(), 0, 0, 50, 50);
    }

    public void mouseWheelMoved(MouseWheelEvent event, Integer scrollAmount) {
        if (0.01 < gameCore.getRenderer().getCameraZoom() && gameCore.getRenderer().getCameraZoom() < 100) {
            if (scrollAmount < 0) gameCore.getRenderer().setCameraZoom(gameCore.getRenderer().getCameraZoom() / 2);
            else gameCore.getRenderer().setCameraZoom(gameCore.getRenderer().getCameraZoom() * 2);
        } else { gameCore.getRenderer().setCameraZoom(1);}
    }

    public void mousePressed(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {
        if (event.getButton() == Input.MOUSE_L_BUTTON) {
            vy = -jumpHeight;
            SoundManager.play(pop, 1.0f, null);
        } else {
            scale = scale == 0.3f ? 1f : 0.3f;
        }
    }

    public Integer getX() {
        return x;
    }
    public Integer getY() {
        return y;
    }
    public Double getScale() {
        return (double) scale;
    }
    public Double getRotation() {
        return vy * 1d;
    }
    public int getZDepth() {
        return 1;
    }

    @Override
    public Float getVx() {
        return vx;
    }

    @Override
    public Float getVy() {
        return vy;
    }

    public int getScore() {
        return score;
    }

    public List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(0, 0, 50, 50, "player"));
    }

    @Override
    public void onEnter(GameObject other, String id) {
        if (id.equals("wall")) this.die();
        if (id.equals("score")) this.score += 1;
    }
}
