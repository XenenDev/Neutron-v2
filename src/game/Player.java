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

public class Player extends GameObject implements ObjectRenderer, MouseButtonInput, MouseMovement, Collidable {
    private int x, y;
    private int jumpHeight;
    private float vy;
    private float r;
    private float scale;
    private int score;
    private boolean onGround;
    private GameCore gameCore;

    Resource pop = new Resource("res/sound.wav");

    public void play(GameCore gameCore) {
        this.x = 40;
        this.y = 100;
        this.r = 0;
        this.vy = 0;
        this.scale = 1f;
        this.score = 0;

        this.onGround = false;
        
        this.gameCore = gameCore;
        this.jumpHeight = 12;
    }

    public void update(GameCore gameCore, float delta) {
        y += (int) vy;
        vy += 0.5f;

        if (onGround) {
            if (vy > 0) {
                vy = 0;
                y = 452;
            }
            r = 0;
        } else {
            //r += 3.4f * delta;
        }
    }

    private void die() {
        this.x = 40;
        this.y = 100;
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
        r.fillSquare(0, 0, 50, Color.BLACK);
        r.fillSquare(2, 2, 46, Color.CYAN);
    }

    public void mousePressed(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {
        if (event.getButton() == Input.MOUSE_L_BUTTON && onGround) {
            vy -= jumpHeight;
            SoundManager.play(pop, 1.0f, null);
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
        return (double) r;
    }
    public int getZDepth() {
        return 1;
    }

    @Override
    public Float getVx() {
        return 0f;
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
        if (id.equals("ground")) this.onGround = true;
    }

    @Override
    public void onExit(GameObject other, String id) {
        if (id.equals("ground")) {
            System.out.println("SOO");
            this.onGround = false;
        }
        this.onGround = false;
    }
}
