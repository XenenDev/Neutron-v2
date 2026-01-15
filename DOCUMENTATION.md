# Neutron v2 Engine Documentation

Comprehensive documentation for the Neutron v2 Game Engine.

## Table of Contents

1. [Engine Architecture](#engine-architecture)
2. [Core Systems](#core-systems)
3. [Game Objects](#game-objects)
4. [Rendering System](#rendering-system)
5. [Collision System](#collision-system)
6. [Input System](#input-system)
7. [Audio System](#audio-system)
8. [Resource Management](#resource-management)
9. [Best Practices](#best-practices)

---

## Engine Architecture

### Overview

Neutron v2 follows a component-based architecture where game objects implement interfaces to gain specific capabilities. The engine uses a fixed timestep game loop running at 60 updates per second with variable rendering.

### Core Components

```
┌─────────────────┐
│    GameCore     │ ◄─── Main game loop
└────────┬────────┘
         │
         ├─► Renderer ◄─── Graphics pipeline
         ├─► Window ◄───── Window management
         ├─► Input ◄────── Input handling
         │
         ▼
┌─────────────────┐
│  ObjectHandler  │ ◄─── Object lifecycle
└────────┬────────┘
         │
         ├─► CollisionManager
         ├─► SoundManager
         └─► ResourceManager
```

### Game Loop

The engine uses a fixed timestep loop:

```
1. Calculate delta time
2. Update game logic (60 FPS)
3. Update all game objects
4. Check collisions
5. Clear renderer
6. Render all objects (sorted by Z-depth)
7. Render UI objects
8. Display frame
```

**Key Features:**
- **Fixed Updates**: Game logic runs at consistent 60 FPS
- **Variable Rendering**: Renders as fast as possible
- **Delta Time**: Passed to update methods for frame-independent behavior

---

## Core Systems

### GameCore

The main game loop and engine controller.

**Key Methods:**
- `start()` - Starts the game loop
- `stop()` - Stops the game loop
- `getRenderer()` - Returns the renderer instance
- `getFrameTimeMs()` - Gets frame time in milliseconds
- `getFPS()` - Gets current frames per second

**Usage:**
```java
GameCore gameCore = new GameCore(
    myGame,              // Your BaseGame implementation
    "Window Title",      // Window title
    800,                 // Width
    600,                 // Height
    "res/icon.png",      // Icon path
    graphicsFidelity     // Graphics settings
);
```

### BaseGame

Abstract class that your main game extends.

**Required Methods:**
- `play(GameCore, Renderer)` - Called once at game start for initialization
- `update(GameCore, Renderer, float delta)` - Called every frame for game-wide logic

**Example:**
```java
public class MyGame extends BaseGame {
    @Override
    public void play(GameCore gameCore, Renderer r) {
        // Initialize game objects
        new Player();
        new Level();
    }
    
    @Override
    public void update(GameCore gameCore, Renderer r, float delta) {
        // Global game logic (HUD updates, game state management, etc.)
    }
}
```

### ObjectHandler

Manages all game objects in the engine.

**Key Features:**
- Automatic object registration and lifecycle management
- Interface-based system for rendering, collision, and input
- Unique ID assignment for each object
- Deferred operations to prevent concurrent modification

**Key Methods:**
- `add(GameObject)` - Adds an object (called automatically by GameObject constructor)
- `remove(GameObject)` - Removes an object
- `getById(long id)` - Gets object by unique ID
- `exists(long id)` - Checks if object exists
- `get(Class<T>)` - Gets all objects of a specific type
- `updateInterfaces(Class<?>, String, Object...)` - Calls method on all objects implementing interface
- `queueInterfaceUpdate(...)` - Queues interface update for next frame

**Example:**
```java
// Get all enemies
List<GameObject> enemies = ObjectHandler.get(Enemy.class);

// Get object by ID
GameObject obj = ObjectHandler.getById(someId);

// Delete an object
someObject.delete(); // Calls ObjectHandler.remove internally
```

---

## Game Objects

### GameObject Base Class

All game entities extend `GameObject`.

**Lifecycle:**
1. **Constructor** - Object created, automatically added to ObjectHandler
2. **play(GameCore)** - Called once when object is ready
3. **update(GameCore, float delta)** - Called every frame
4. **delete()** - Removes object from engine

**Basic Example:**
```java
public class Enemy extends GameObject {
    private int health;
    
    public Enemy() {
        super(); // Automatically registers with ObjectHandler
    }
    
    @Override
    public void play(GameCore gameCore) {
        health = 100;
    }
    
    @Override
    public void update(GameCore gameCore, float delta) {
        if (health <= 0) {
            this.delete();
        }
    }
}
```

### Component Interfaces

Objects gain functionality by implementing interfaces:

#### ObjectRenderer
For renderable game objects in the world.

```java
public interface ObjectRenderer {
    void render(GameCore gameCore, Renderer r);
    Integer getX();        // World X position
    Integer getY();        // World Y position
    Double getScale();     // Scale factor
    Double getRotation();  // Rotation in degrees
    int getZDepth();       // Rendering order (higher = front)
}
```

#### UIObjectRenderer
For UI elements rendered in screen coordinates.

```java
public interface UIObjectRenderer {
    void renderUI(GameCore gameCore, Renderer r);
}
```

#### Collidable
For objects with collision detection.

```java
public interface Collidable {
    List<Collider> getColliders();
    void onEnter(GameObject other, String colliderId);
    void duringCollision(GameObject other, float delta);
    void onExit(GameObject other);
}
```

#### Movable
For objects with velocity.

```java
public interface Movable {
    Float getVx(); // X velocity
    Float getVy(); // Y velocity
}
```

#### KeyboardInput
For keyboard event handling.

```java
public interface KeyboardInput {
    void keyPressed(Input input, KeyEvent e, Integer keyCode);
    void keyReleased(Input input, KeyEvent e, Integer keyCode);
}
```

#### MouseButtonInput
For mouse button events.

```java
public interface MouseButtonInput {
    void mousePressed(MouseEvent e, Integer x, Integer y, Boolean isOffWindow);
    void mouseReleased(MouseEvent e, Integer x, Integer y, Boolean isOffWindow);
}
```

#### MouseMovement
For mouse movement tracking.

```java
public interface MouseMovement {
    void mouseMoved(MouseEvent e, Integer x, Integer y, Boolean isOffWindow);
    void mouseDragged(MouseEvent e, Integer x, Integer y, Boolean isOffWindow);
}
```

#### MouseWheelInput
For scroll wheel events.

```java
public interface MouseWheelInput {
    void mouseWheelMoved(MouseWheelEvent e, Integer scrollAmount);
}
```

---

## Rendering System

### Renderer

The main rendering class with extensive drawing capabilities.

### Coordinate Modes

**World Coordinates** (default):
- Affected by camera position and zoom
- Used for game objects in the world

**Screen Coordinates**:
- Fixed to screen, unaffected by camera
- Used for UI elements

Switch modes:
```java
renderer.setUseScreenCoordinates(true);  // Screen mode
renderer.setUseScreenCoordinates(false); // World mode
```

### Screen Anchors

When using screen coordinates, you can set an anchor point to position elements relative to any of 9 screen positions:

```
┌─────────────────────────────────────┐
│ TOP_LEFT    TOP_CENTER    TOP_RIGHT │
│                                     │
│ CENTER_LEFT   CENTER   CENTER_RIGHT │
│                                     │
│ BOTTOM_LEFT BOTTOM_CENTER BOTTOM_RIGHT│
└─────────────────────────────────────┘
```

**Setting the anchor:**
```java
renderer.setUseScreenCoordinates(true);

// Top-left corner (default behavior)
renderer.setScreenAnchor(ScreenAnchor.TOP_LEFT);
renderer.drawText("Score: 100", 10, 30, Color.WHITE);

// Bottom-right corner (use negative coordinates to move inward)
renderer.setScreenAnchor(ScreenAnchor.BOTTOM_RIGHT);
renderer.drawText("Lives: 3", -100, -20, Color.WHITE);

// Center of screen
renderer.setScreenAnchor(ScreenAnchor.CENTER);
renderer.drawText("PAUSED", -30, 0, Color.WHITE);
```

**Key Points:**
- Default anchor is `TOP_LEFT` (same as previous behavior)
- Anchor only affects screen coordinates (not world coordinates)
- Use negative values to move inward from right/bottom anchors
- Perfect for responsive UI that adapts to different screen sizes

### Drawing Methods

#### Shapes
```java
// Rectangles
renderer.fillRect(x, y, width, height, color);
renderer.drawRect(x, y, width, height, color);
renderer.fillSquare(x, y, length, color);
renderer.drawSquare(x, y, length, color);

// Circles/Ovals
renderer.fillCircle(x, y, radius, color);
renderer.drawCircle(x, y, radius, color);
renderer.fillOval(x, y, width, height, color);
renderer.drawOval(x, y, width, height, color);

// Lines
renderer.drawLine(x1, y1, x2, y2, color);
```

#### Images
```java
// Basic
renderer.drawImage(image, x, y);
renderer.drawImage(image, x, y, width, height);
renderer.drawImage(image, x, y, scale);

// With background color
renderer.drawImage(image, x, y, bgColor);
renderer.drawImage(image, x, y, width, height, bgColor);
```

#### Text
```java
renderer.setFont(new Font("Arial", Font.BOLD, 24));
renderer.drawText("Hello World", x, y, Color.WHITE);
```

#### Custom Shaders
```java
Shader myShader = (px, py, u, v, w, h) -> {
    // px, py: pixel coordinates
    // u, v: normalized coordinates (0.0 to 1.0)
    // w, h: width and height
    return new Color(r, g, b, a);
};

renderer.shade(x, y, width, height, myShader);
```

### Camera System

```java
// Position
renderer.setCameraPos(x, y);
renderer.moveCameraPos(dx, dy);
int camX = renderer.getCameraX();
int camY = renderer.getCameraY();

// Zoom
renderer.setCameraZoom(2.0);  // 2x zoom in
renderer.setCameraZoom(0.5);  // 2x zoom out
double zoom = renderer.getCameraZoom();
```

### Graphics Quality

Configure rendering quality via `GraphicsFidelity`:

```java
new GraphicsFidelity() {
    public boolean useGlobalAA() {
        return true;  // Anti-aliasing for all graphics
    }
    
    public boolean useQualityRendering() {
        return true;  // Higher quality at cost of performance
    }
    
    public boolean useBilinearSampling() {
        return true;  // Smooth image scaling
    }
    
    public boolean useSubPixelFontRendering() {
        return true;  // LCD subpixel text rendering
    }
    
    public boolean useAAForTextOnly() {
        return false; // AA only for text, not shapes
    }
}
```

### Alpha/Transparency

```java
renderer.setAlpha(0.5f);  // 50% transparent
renderer.fillRect(x, y, w, h, Color.RED);
renderer.setAlpha(1.0f);  // Reset to opaque
```

### Line Width

```java
renderer.setLineWidth(5);
renderer.drawLine(x1, y1, x2, y2, Color.BLACK);
```

---

## Collision System

### Overview

The CollisionManager handles collision detection between objects implementing `Collidable`.

### Collider Types

#### RectangleCollider
```java
new Collider.RectangleCollider(x, y, width, height, "myId");
```

#### CircleCollider
```java
new Collider.CircleCollider(x, y, radius, "myId");
```

### Collision Detection

**Two-Phase Detection:**
1. **Discrete Check**: Standard intersection test
2. **Swept AABB**: For fast-moving rectangles (when implementing `Movable`)

### Implementing Collision

```java
public class Player extends GameObject implements Collidable, Transform, Movable {
    private int x, y;
    private float vx, vy;
    
    @Override
    public List<Collider> getColliders() {
        return List.of(
            new Collider.RectangleCollider(0, 0, 50, 50, "body"),
            new Collider.CircleCollider(25, 25, 10, "hitbox")
        );
    }
    
    @Override
    public void onEnter(GameObject other, String colliderId) {
        // Called once when collision starts
        if (other instanceof Enemy && colliderId.equals("body")) {
            System.out.println("Hit by enemy!");
        }
    }
    
    @Override
    public void duringCollision(GameObject other, float delta) {
        // Called every frame while colliding
    }
    
    @Override
    public void onExit(GameObject other) {
        // Called once when collision ends
    }
    
    // Transform interface (required for collision)
    @Override
    public Integer getX() { return x; }
    
    @Override
    public Integer getY() { return y; }
    
    @Override
    public Double getScale() { return 1.0; }
    
    @Override
    public Double getRotation() { return 0.0; }
    
    // Movable interface (for swept AABB)
    @Override
    public Float getVx() { return vx; }
    
    @Override
    public Float getVy() { return vy; }
}
```

### Collision Tags

Collider IDs help identify which part of an object was hit:

```java
@Override
public List<Collider> getColliders() {
    return List.of(
        new Collider.RectangleCollider(0, 0, 50, 100, "body"),
        new Collider.CircleCollider(25, -10, 15, "head"),
        new Collider.RectangleCollider(0, 100, 50, 10, "feet")
    );
}

@Override
public void onEnter(GameObject other, String colliderId) {
    switch (colliderId) {
        case "head" -> System.out.println("Headshot!");
        case "feet" -> System.out.println("Can jump!");
        case "body" -> System.out.println("Body hit");
    }
}
```

---

## Input System

### Keyboard Input

**Static Methods:**
```java
// Check if key is currently pressed
if (Input.isKeyDown(KeyEvent.VK_SPACE)) {
    player.jump();
}

if (Input.isKeyDown(KeyEvent.VK_W)) {
    player.moveForward();
}
```

**Event-Based:**
```java
public class Player extends GameObject implements KeyboardInput {
    @Override
    public void keyPressed(Input input, KeyEvent e, Integer keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            jump();
        }
    }
    
    @Override
    public void keyReleased(Input input, KeyEvent e, Integer keyCode) {
        if (keyCode == KeyEvent.VK_SPACE) {
            stopJump();
        }
    }
}
```

### Mouse Input

**Static Methods:**
```java
// Mouse position (screen coordinates)
int mouseX = Input.getMouseX();
int mouseY = Input.getMouseY();

// Mouse position (world coordinates)
int worldX = Input.getMouseXinWorld();
int worldY = Input.getMouseYinWorld();

// Mouse buttons
if (Input.isMouseDown(Input.MOUSE_L_BUTTON)) {
    fire();
}

// Mouse state
boolean onScreen = Input.isMouseOnScreen();
```

**Mouse Buttons:**
- `Input.MOUSE_L_BUTTON` - Left mouse button
- `Input.MOUSE_M_BUTTON` - Middle mouse button
- `Input.MOUSE_R_BUTTON` - Right mouse button

**Event-Based:**
```java
public class Button extends GameObject implements MouseButtonInput, MouseMovement {
    @Override
    public void mousePressed(MouseEvent e, Integer x, Integer y, Boolean isOffWindow) {
        if (e.getButton() == Input.MOUSE_L_BUTTON) {
            onClick();
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e, Integer x, Integer y, Boolean isOffWindow) {
        if (isMouseOver(x, y)) {
            setHighlight(true);
        }
    }
}
```

### Window Focus

```java
public class MyObject extends GameObject implements WindowFocusListener {
    @Override
    public void focusGained() {
        // Resume game
    }
    
    @Override
    public void focusLost() {
        // Pause game
    }
}

// Static check
if (Input.isWindowFocused()) {
    // Window has focus
}
```

---

## Audio System

### SoundManager

Manages audio playback with overlapping sound support.

### Playing Sounds

```java
Resource sound = new Resource("res/explosion.wav");

// Basic playback
SoundManager.play(sound, 1.0f, null);

// With volume (0.0 to 1.0)
SoundManager.play(sound, 0.5f, null);

// With tags for control
SoundManager.play(sound, 1.0f, null, "sfx", "explosion");
```

### Volume Control

```java
// Master volume (affects all sounds)
SoundManager.setMainVolume(0.8f);  // 80% volume
float vol = SoundManager.getMainVolume();
```

### Stopping Sounds

```java
// Stop by tag
SoundManager.stopByTag("music");

// Stop by multiple tags
SoundManager.stopByTags("sfx", "ambient");

// Stop all sounds
SoundManager.stopAll();
```

### Sound Tags

Use tags to organize and control sounds:

```java
// Play with tags
SoundManager.play(bgMusic, 1.0f, null, "music", "background");
SoundManager.play(footstep, 0.3f, null, "sfx", "player");
SoundManager.play(ambient, 0.2f, null, "sfx", "ambient");

// Stop only music
SoundManager.stopByTag("music");

// Stop all SFX but keep music
SoundManager.stopByTag("sfx");
```

### Audio Effects

Create custom audio effects:

```java
AudioEffect reverb = (sound) -> {
    // Modify sound PCM data
    byte[] data = sound.getRawData();
    // Apply effect...
};

SoundManager.play(sound, 1.0f, reverb, "sfx");
```

---

## Resource Management

### Resource Class

Wrapper for loading and managing game resources.

### Creating Resources

```java
// Automatically detects type based on extension
Resource image = new Resource("res/player.png");
Resource sound = new Resource("res/jump.wav");
```

### Using Resources

```java
// Get the underlying object
Image img = (Image) image.get();
ResourceManager.Sound snd = (ResourceManager.Sound) sound.get();

// Draw image
renderer.drawImage(img, x, y);

// Play sound
SoundManager.play(sound, 1.0f, null);
```

### Resource Types

```java
ResourceType type = resource.getType();

if (type == ResourceType.IMAGE) {
    // Handle image
} else if (type == ResourceType.SOUND) {
    // Handle sound
}
```

### Resource Lifecycle

```java
// Resources are automatically cached
Resource img1 = new Resource("res/player.png");
Resource img2 = new Resource("res/player.png"); // Reuses cached version

// Manual unload
resource.unload();           // Unloads this reference
resource.unload(true);       // Fully removes from cache
```

### Supported Formats

**Images:**
- PNG, JPG, JPEG, BMP, GIF

**Audio:**
- WAV, MP3, OGG, FLAC, AAC

---

## Best Practices

### 1. Object Lifecycle

```java
// ✓ Good: Clean initialization in play()
public void play(GameCore gameCore) {
    this.x = 100;
    this.y = 100;
    this.health = 100;
}

// ✗ Bad: Complex logic in constructor
public MyObject() {
    super();
    // Don't do heavy initialization here
    // GameCore isn't ready yet
}
```

### 2. Resource Loading

```java
// ✓ Good: Load once, reuse
private static final Resource SPRITE = new Resource("res/sprite.png");

public void render(GameCore gc, Renderer r) {
    r.drawImage((Image) SPRITE.get(), x, y);
}

// ✗ Bad: Loading every frame
public void render(GameCore gc, Renderer r) {
    Resource sprite = new Resource("res/sprite.png"); // Don't do this!
    r.drawImage((Image) sprite.get(), x, y);
}
```

### 3. Collision Performance

```java
// ✓ Good: Minimal colliders
public List<Collider> getColliders() {
    return List.of(
        new Collider.RectangleCollider(0, 0, 50, 50, "body")
    );
}

// ✗ Bad: Too many colliders
public List<Collider> getColliders() {
    List<Collider> colliders = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
        colliders.add(new Collider.CircleCollider(i, i, 5, "part" + i));
    }
    return colliders;
}
```

### 4. Update Logic

```java
// ✓ Good: Use delta for frame independence
public void update(GameCore gc, float delta) {
    x += velocity * delta;
}

// ✗ Bad: Fixed increments (frame-dependent)
public void update(GameCore gc, float delta) {
    x += velocity; // Speed depends on frame rate
}
```

### 5. Object Deletion

```java
// ✓ Good: Delete when done
public void update(GameCore gc, float delta) {
    if (health <= 0) {
        this.delete();
    }
}

// ✗ Bad: Just hiding
public void update(GameCore gc, float delta) {
    if (health <= 0) {
        visible = false; // Still updating every frame!
    }
}
```

### 6. UI vs World Objects

```java
// ✓ Good: UI objects use screen coordinates
public class HUD extends GameObject implements UIObjectRenderer {
    public void renderUI(GameCore gc, Renderer r) {
        // Screen coordinates - unaffected by camera
        r.drawText("Score: " + score, 10, 30, Color.WHITE);
    }
}

// ✗ Bad: World object for UI
public class HUD extends GameObject implements ObjectRenderer {
    public void render(GameCore gc, Renderer r) {
        // World coordinates - moves with camera!
        r.drawText("Score: " + score, 10, 30, Color.WHITE);
    }
}
```

### 7. Camera Usage

```java
// ✓ Good: Smooth camera follow
public void update(GameCore gc, float delta) {
    Renderer r = gc.getRenderer();
    int targetX = player.getX() - gc.WIDTH / 2;
    int targetY = player.getY() - gc.HEIGHT / 2;
    
    int dx = (targetX - r.getCameraX()) / 10;
    int dy = (targetY - r.getCameraY()) / 10;
    
    r.moveCameraPos(dx, dy);
}

// ✗ Bad: Instant camera snap
public void update(GameCore gc, float delta) {
    Renderer r = gc.getRenderer();
    r.setCameraPos(player.getX(), player.getY()); // Jarring movement
}
```

### 8. Performance Tips

- **Minimize object creation** in update/render loops
- **Use object pooling** for frequently created/destroyed objects
- **Batch similar rendering** operations
- **Limit colliders** to essential hitboxes
- **Cache calculations** that don't change every frame
- **Use Z-depth efficiently** - don't create unnecessary layers

---

## Advanced Topics

### Custom Shaders

Create visual effects with pixel shaders:

```java
// Gradient shader
Shader gradient = (px, py, u, v, w, h) -> {
    int r = (int) (255 * u);
    int g = (int) (255 * v);
    int b = 128;
    return new Color(r, g, b);
};

renderer.shade(x, y, 200, 200, gradient);
```

### Vector Math

Use Vector2 for 2D calculations:

```java
Vector2 position = new Vector2(100, 100);
Vector2 velocity = new Vector2(5, -3);

position.add(velocity);
float distance = position.distance(targetPos);
position.normalize();
```

### Scene Management

Create separate scenes:

```java
public class MenuScene extends Scene {
    @Override
    public void play(GameCore gc, Renderer r) {
        new MenuButton("Start Game");
        new MenuButton("Options");
    }
}

// Switch scenes (implement your own scene manager)
currentScene.cleanup();
currentScene = new GameScene();
currentScene.play(gameCore, renderer);
```

---

## Troubleshooting

### Common Issues

**Objects not rendering:**
- Ensure object implements `ObjectRenderer`
- Check Z-depth values
- Verify object is within camera view
- Check if `render()` method is being called

**Collisions not working:**
- Implement `Collidable` AND `Transform` interfaces
- Verify colliders are correctly positioned
- Check collision callback methods are implemented
- Ensure objects are registered (check `play()` is called)

**Input not responding:**
- Check window has focus
- Verify interface is implemented correctly
- Test with static `Input.isKeyDown()` first
- Check for typos in method names

**Sounds not playing:**
- Verify file path is correct
- Check audio file format is supported
- Ensure volume is not zero
- Check for exceptions in console

**Performance issues:**
- Reduce number of game objects
- Minimize colliders per object
- Optimize rendering (reduce shader usage)
- Profile your update() methods
- Consider object pooling

---

**Next Steps:**
- See [EXAMPLES.md](EXAMPLES.md) for practical code examples
- See [API_REFERENCE.md](API_REFERENCE.md) for complete API documentation
- See [CONTRIBUTING.md](CONTRIBUTING.md) to contribute to the engine
