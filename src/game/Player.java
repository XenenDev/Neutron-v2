package game;

import com.neutron.engine.*;
import com.neutron.engine.base.*;
import com.neutron.engine.base.interfaces.*;
import com.neutron.engine.func.Collider;
import com.neutron.engine.func.Resource;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.List;

public class Player extends GameObject implements ObjectRenderer, MouseButtonInput, MouseMovement, KeyboardInput, Collidable {
    private float x, y;
    private int jumpHeight;
    private float vy;
    private float r;
    private float scale;
    private int score;
    private int groundContacts;  // Count of ground blocks we're touching
    private int queueJump;
    private int groundHeight;
    private final Game game;
    private GameCore gc;

    Resource pop = new Resource("res/sound.wav");

    public Player(Game game) {
        super();
        this.game = game;
    }

    public void play(GameCore gameCore) {
        this.x = 300;
        this.y = 400;
        this.r = 0;
        this.vy = 0;
        this.scale = 1f;
        this.score = 0;
        this.groundHeight = 500;

        this.groundContacts = 0;
        this.queueJump = 0;
        
        this.jumpHeight = 16;
        this.gc = gameCore;
    }

    public void update(GameCore gameCore, float delta) {
        y += vy * delta;
        vy += 1f * delta;

        boolean onGround = groundContacts > 0;

        // Snap to ground if we've fallen past it (prevents falling through)
        if (onGround && y > groundHeight - 50) {
            y = groundHeight - 50;
        }

        queueJump = Math.max(0, (int) (queueJump - 10f*delta));

        if (Input.isKeyDown(Input.SPACE) || Input.isMouseDown(Input.MOUSE_L_BUTTON)) queueJump = 100;

        if (onGround) {
            vy = 0;
            y = groundHeight - 50;
            r = 0;

            if (queueJump > 0) {
                groundContacts = 0;  // Clear all contacts when jumping
                vy -= jumpHeight;
                SoundManager.play(pop, 1.0f, null);
                queueJump = 0;
            }
        } else {
            r += 3.7f * delta;
        }
    }

    private void die() {
        play(gc);
        game.restart();
    }

    public void render(GameCore gameCore, Renderer r) {
        r.fillSquare(0, 0, 50, r.color(0,0,0));
        r.fillSquare(2, 2, 46, r.color(0,255,255));
    }

    public void mousePressed(MouseEvent event, Integer x, Integer y, Boolean isOffWindow) {
        if (event.getButton() == Input.MOUSE_L_BUTTON) queueJump = 100;
    }

    public void keyPressed(Input input, KeyEvent event, Integer key) {
        if (key == Input.SPACE) queueJump = 100;
        if (key == Input.S) game.restart();
    }

    public Integer getX() {
        return (int) x;
    }
    public Integer getY() {
        return (int) y;
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

    @Override
    public Double getPivotX() { return 25d; }

    @Override
    public Double getPivotY() { return 25d; }

    public int getScore() {
        return score;
    }

    public List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(0, 0, 50, 50, "player"));
    }

    @Override
    public void onEnter(GameObject other, String id) {
        if (id.equals("death")) this.die();
        if (id.equals("ground")) {
            int otherY = ((Transform) other).getY();
            // Use the highest ground we're touching
            if (groundContacts == 0 || otherY < this.groundHeight) {
                this.groundHeight = otherY;
            }
            this.groundContacts++;
            // Snap position and stop falling
            this.y = groundHeight - 50;
            this.vy = 0;
        }
    }

    @Override
    public void onExit(GameObject other, String id) {
        if (id.equals("ground")) {
            this.groundContacts = Math.max(0, this.groundContacts - 1);
            if (this.groundContacts == 0) {
                this.groundHeight = 500;
            }
        }
    }
}
