# Neutron v2 API Reference

Complete API reference for the Neutron v2 Game Engine.

## Table of Contents

1. [Core Classes](#core-classes)
2. [Base Classes](#base-classes)
3. [Interfaces](#interfaces)
4. [Utility Classes](#utility-classes)
5. [Managers](#managers)

---

## Core Classes

### GameCore

Main game loop and engine controller.

#### Constructor
```java
public GameCore(Game game, String title, int width, int height, 
                String iconPath, GraphicsFidelity gq)
```
- **Parameters:**
  - `game` - Your BaseGame implementation
  - `title` - Window title
  - `width` - Window width in pixels
  - `height` - Window height in pixels
  - `iconPath` - Path to window icon image
  - `gq` - Graphics quality settings

#### Fields
```java
public final int WIDTH    // Window width
public final int HEIGHT   // Window height
```

#### Methods

**start()**
```java
public void start()
```
Starts the game loop. Called automatically by constructor.

**stop()**
```java
public void stop()
```
Stops the game loop and exits the application.

**getRenderer()**
```java
public Renderer getRenderer()
```
Returns the Renderer instance.
- **Returns:** The Renderer object

**getFrameTimeMs()**
```java
public float getFrameTimeMs()
```
Gets the time taken to process the last frame in milliseconds.
- **Returns:** Frame time in milliseconds

**getFPS()**
```java
public float getFPS()
```
Gets the current frames per second.
- **Returns:** Current FPS

---

### Renderer

Graphics rendering system with 2D drawing capabilities.

#### Fields
```java
public final Graphics2D graphics   // Direct access to Graphics2D
public final int WIDTH             // Renderer width
public final int HEIGHT            // Renderer height
public final int CENTER_X          // Center X coordinate
public final int CENTER_Y          // Center Y coordinate
```

#### Shape Drawing

**fillRect()**
```java
public void fillRect(int x, int y, int w, int h, Color color)
```
Draws a filled rectangle.

**drawRect()**
```java
public void drawRect(int x, int y, int w, int h, Color color)
```
Draws a rectangle outline.

**fillSquare()**
```java
public void fillSquare(int x, int y, int length, Color color)
```
Draws a filled square.

**drawSquare()**
```java
public void drawSquare(int x, int y, int length, Color color)
```
Draws a square outline.

**fillOval()**
```java
public void fillOval(int x, int y, int w, int h, Color color)
```
Draws a filled oval.

**drawOval()**
```java
public void drawOval(int x, int y, int w, int h, Color color)
```
Draws an oval outline.

**fillCircle()**
```java
public void fillCircle(int x, int y, int radius, Color color)
```
Draws a filled circle.

**drawCircle()**
```java
public void drawCircle(int x, int y, int radius, Color color)
```
Draws a circle outline.

**drawLine()**
```java
public void drawLine(int x1, int y1, int x2, int y2, Color color)
```
Draws a line between two points.

#### Image Drawing

**drawImage()**
```java
public void drawImage(Image img, int x, int y)
public void drawImage(Image img, int x, int y, Color bgColor)
public void drawImage(Image img, int x, int y, int w, int h)
public void drawImage(Image img, int x, int y, int w, int h, Color bgColor)
public void drawImage(Image img, int x, int y, float scale)
public void drawImage(Image img, int x, int y, float scale, Color bgColor)
```
Draws an image with various options for sizing and background color.

#### Text Drawing

**drawText()**
```java
public void drawText(Object string, int x, int y, Color color)
```
Draws text at the specified position.

**setFont()**
```java
public void setFont(Font f)
```
Sets the font for text rendering.

#### Shaders

**shade()**
```java
public void shade(int x, int y, int w, int h, Shader shader)
public void shade(int x, int y, int w, int h, float scale, Shader shader)
public void shade(int x, int y, int w, int h, float scaleY, float scaleX, Shader shader)
```
Applies a custom shader to a rectangular region.

#### Graphics Settings

**setGraphicsFidelity()**
```java
public void setGraphicsFidelity(GraphicsFidelity gq)
```
Updates graphics quality settings.

**setAlpha()**
```java
public void setAlpha(float alpha)
```
Sets the transparency level (0.0 = fully transparent, 1.0 = fully opaque).

**setLineWidth()**
```java
public void setLineWidth(int w)
```
Sets the line width for drawing operations.

#### Camera Control

**setCameraPos()**
```java
public void setCameraPos(int x, int y)
```
Sets the camera position in world coordinates.

**moveCameraPos()**
```java
public void moveCameraPos(int dx, int dy)
```
Moves the camera by a delta amount.

**getCameraX()**
```java
public int getCameraX()
```
Gets the camera X position.

**getCameraY()**
```java
public int getCameraY()
```
Gets the camera Y position.

**setCameraZoom()**
```java
public void setCameraZoom(double scale)
```
Sets the camera zoom level (1.0 = normal, 2.0 = 2x zoom in, 0.5 = 2x zoom out).

**getCameraZoom()**
```java
public double getCameraZoom()
```
Gets the current camera zoom level.

#### Coordinate Systems

**setUseScreenCoordinates()**
```java
public void setUseScreenCoordinates(boolean useScreenCoordinates)
```
Switches between screen and world coordinates.
- `true` - Screen coordinates (fixed to screen, for UI)
- `false` - World coordinates (affected by camera)

**getUseScreenCoordinates()**
```java
public boolean getUseScreenCoordinates()
```
Returns current coordinate mode.

**setScreenAnchor()**
```java
public void setScreenAnchor(ScreenAnchor anchor)
```
Sets the anchor point for screen coordinate positioning.
- **Parameters:** `anchor` - One of the 9 anchor points (TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, CENTER, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT)
- **Default:** TOP_LEFT
- Only affects rendering when using screen coordinates

**getScreenAnchor()**
```java
public ScreenAnchor getScreenAnchor()
```
Returns the current screen anchor point.

#### Rendering Pipeline

**clear()**
```java
public void clear()
```
Clears the screen. Called automatically each frame.

**show()**
```java
public void show()
```
Displays the rendered frame. Called automatically each frame.

---

### Window

Window management class.

#### Constructor
```java
public Window(String windowTitle, int w, int h, String iconPath, Input input)
```
Creates and displays the game window.

#### Methods

**getCanvas()**
```java
public Canvas getCanvas()
```
Returns the rendering canvas.

---

### Input

Static input handling system.

#### Static Fields
```java
public static final int MOUSE_L_BUTTON = 1
public static final int MOUSE_M_BUTTON = 2
public static final int MOUSE_R_BUTTON = 3
```

#### Static Methods

**isKeyDown()**
```java
public static boolean isKeyDown(int key)
```
Checks if a key is currently pressed.
- **Parameters:** `key` - KeyEvent constant (e.g., KeyEvent.VK_SPACE)
- **Returns:** true if key is pressed

**isMouseDown()**
```java
public static boolean isMouseDown(int button)
```
Checks if a mouse button is currently pressed.
- **Parameters:** `button` - MOUSE_L_BUTTON, MOUSE_M_BUTTON, or MOUSE_R_BUTTON
- **Returns:** true if button is pressed

**getMouseX()**
```java
public static int getMouseX()
```
Gets mouse X position in screen coordinates.

**getMouseY()**
```java
public static int getMouseY()
```
Gets mouse Y position in screen coordinates.

**getMouseXinWorld()**
```java
public static int getMouseXinWorld()
```
Gets mouse X position in world coordinates (accounting for camera).

**getMouseYinWorld()**
```java
public static int getMouseYinWorld()
```
Gets mouse Y position in world coordinates (accounting for camera).

**isMouseOnScreen()**
```java
public static boolean isMouseOnScreen()
```
Checks if mouse is within the window.

**isWindowFocused()**
```java
public static boolean isWindowFocused()
```
Checks if the game window has focus.

---

## Base Classes

### BaseGame

Abstract base class for your main game.

#### Abstract Methods

**play()**
```java
public abstract void play(GameCore gameCore, Renderer r)
```
Called once at game start for initialization.

**update()**
```java
public abstract void update(GameCore gameCore, Renderer r, float delta)
```
Called every frame for game-wide logic.
- **Parameters:** `delta` - Time multiplier for frame-independent behavior

---

### GameObject

Base class for all game entities.

#### Constructor
```java
public GameObject()
```
Creates a game object and registers it with ObjectHandler.

#### Methods

**play()**
```java
public abstract void play(GameCore gameCore)
```
Called once when object is added to the game.

**update()**
```java
public abstract void update(GameCore gameCore, float delta)
```
Called every frame to update object logic.

**delete()**
```java
public void delete()
```
Removes the object from the game.

**getId()**
```java
public long getId()
```
Gets the unique ID of this object.

**setId()**
```java
public void setId(long id)
```
Sets the unique ID (called automatically by ObjectHandler).

---

### Scene

Base class for scene management (currently a placeholder for future implementation).

---

## Interfaces

### ObjectRenderer

Interface for renderable game objects in the world.

#### Methods

**render()**
```java
void render(GameCore gameCore, Renderer r)
```
Called every frame to render the object.

**getX()**
```java
Integer getX()
```
Returns the X position in world coordinates.

**getY()**
```java
Integer getY()
```
Returns the Y position in world coordinates.

**getScale()**
```java
Double getScale()
```
Returns the scale factor (1.0 = normal size).

**getRotation()**
```java
Double getRotation()
```
Returns the rotation in degrees.

**getZDepth()**
```java
int getZDepth()
```
Returns the rendering depth (higher values render in front).

---

### UIObjectRenderer

Interface for UI elements rendered in screen coordinates.

#### Methods

**renderUI()**
```java
void renderUI(GameCore gameCore, Renderer r)
```
Called every frame to render UI elements.

---

### Collidable

Interface for objects with collision detection.

#### Methods

**getColliders()**
```java
List<Collider> getColliders()
```
Returns a list of colliders for this object.

**onEnter()**
```java
void onEnter(GameObject other, String colliderId)
```
Called once when collision starts.
- **Parameters:**
  - `other` - The other object involved in collision
  - `colliderId` - ID of the collider that was hit

**duringCollision()**
```java
void duringCollision(GameObject other, float delta)
```
Called every frame while colliding.

**onExit()**
```java
void onExit(GameObject other)
```
Called once when collision ends.

---

### Transform

Interface providing position, scale, and rotation (required for Collidable).

#### Methods

**getX()**
```java
Integer getX()
```
Returns the X position.

**getY()**
```java
Integer getY()
```
Returns the Y position.

**getScale()**
```java
Double getScale()
```
Returns the scale factor.

**getRotation()**
```java
Double getRotation()
```
Returns the rotation in degrees.

---

### Movable

Interface for objects with velocity (enables swept AABB collision).

#### Methods

**getVx()**
```java
Float getVx()
```
Returns X velocity.

**getVy()**
```java
Float getVy()
```
Returns Y velocity.

---

### KeyboardInput

Interface for keyboard event handling.

#### Methods

**keyPressed()**
```java
void keyPressed(Input input, KeyEvent e, Integer keyCode)
```
Called when a key is pressed.

**keyReleased()**
```java
void keyReleased(Input input, KeyEvent e, Integer keyCode)
```
Called when a key is released.

---

### MouseButtonInput

Interface for mouse button event handling.

#### Methods

**mousePressed()**
```java
void mousePressed(MouseEvent e, Integer x, Integer y, Boolean isOffWindow)
```
Called when a mouse button is pressed.

**mouseReleased()**
```java
void mouseReleased(MouseEvent e, Integer x, Integer y, Boolean isOffWindow)
```
Called when a mouse button is released.

---

### MouseMovement

Interface for mouse movement tracking.

#### Methods

**mouseMoved()**
```java
void mouseMoved(MouseEvent e, Integer x, Integer y, Boolean isOffWindow)
```
Called when the mouse moves.

**mouseDragged()**
```java
void mouseDragged(MouseEvent e, Integer x, Integer y, Boolean isOffWindow)
```
Called when the mouse is dragged (moved while button is pressed).

---

### MouseWheelInput

Interface for scroll wheel event handling.

#### Methods

**mouseWheelMoved()**
```java
void mouseWheelMoved(MouseWheelEvent e, Integer scrollAmount)
```
Called when the mouse wheel is scrolled.

---

### MouseWindowMovement

Interface for mouse entering/exiting window.

#### Methods

**mouseEnteredWindow()**
```java
void mouseEnteredWindow(MouseEvent e, Integer x, Integer y)
```
Called when mouse enters the window.

**mouseExitedWindow()**
```java
void mouseExitedWindow(MouseEvent e, Integer x, Integer y)
```
Called when mouse exits the window.

---

### WindowFocusListener

Interface for window focus events.

#### Methods

**focusGained()**
```java
void focusGained()
```
Called when window gains focus.

**focusLost()**
```java
void focusLost()
```
Called when window loses focus.

---

### SoundEmitter

Interface for declarative, condition-based sound playback in GameObjects.

#### Methods

**defineSounds()**
```java
SoundHelper.SoundRule[] defineSounds()
```
Returns an array of sound rules that define when sounds should play.

Each rule is evaluated every frame, and sounds are automatically played/stopped based on conditions.

- **Returns:** Array of `SoundRule` objects (can be empty or null)

#### SoundRule Constructor

```java
public SoundRule(Resource sound, Supplier<Boolean> condition, 
                 float volume, String tag, boolean onlyOnChange)

public SoundRule(Resource sound, Supplier<Boolean> condition, 
                 float volume, String tag, boolean onlyOnChange, 
                 AudioEffect effect)
```

- **Parameters:**
  - `sound` - The sound resource to play
  - `condition` - Lambda/Supplier returning true when sound should play
  - `volume` - Volume (0.0 to 1.0)
  - `tag` - Internal tag for sound identification
  - `onlyOnChange` - If true, play once on condition change; if false, play while condition is true
  - `effect` - Optional audio effect (can be null)

#### Example

```java
public class Player extends GameObject implements SoundEmitter {
    private boolean isWalking;
    private boolean jumped;
    
    @Override
    public SoundRule[] defineSounds() {
        return new SoundRule[] {
            new SoundRule(
                ResourceManager.getSound("walk.wav"),
                () -> isWalking,
                0.5f,
                "walk",
                false  // Loop while walking
            ),
            new SoundRule(
                ResourceManager.getSound("jump.wav"),
                () -> jumped,
                1.0f,
                "jump",
                true  // Play once when jumping
            )
        };
    }
}
```

See [Audio System - SoundEmitter Interface](DOCUMENTATION.md#soundemitter-interface) for detailed usage guide.

---

## Utility Classes

### Collider

Abstract base class for collision shapes.

#### RectangleCollider

**Constructor**
```java
public RectangleCollider(double x, double y, double width, double height, String id)
```
Creates a rectangular collider.

**Fields**
```java
public final double x, y, width, height
public final String id
```

**Methods**

**intersects()**
```java
public boolean intersects(Collider other)
```
Checks if this collider intersects another.

**globalize()**
```java
public Collider globalize(Transform t)
```
Converts local coordinates to world coordinates.

**getId()**
```java
public String getId()
```
Returns the collider ID.

---

#### CircleCollider

**Constructor**
```java
public CircleCollider(double x, double y, double radius, String id)
```
Creates a circular collider.

**Fields**
```java
public final double x, y, radius
public final String id
```

**Methods**

Same as RectangleCollider.

---

### Resource

Resource wrapper for automatic loading and caching.

#### Constructor
```java
public Resource(String path)
```
Creates and loads a resource from the given path.

#### Methods

**get()**
```java
public Object get()
```
Returns the underlying resource object (Image or Sound).

**getType()**
```java
public ResourceType getType()
```
Returns the resource type (IMAGE or SOUND).

**getPath()**
```java
public String getPath()
```
Returns the file path.

**unload()**
```java
public void unload()
public void unload(boolean fullUnload)
```
Unloads the resource.
- **Parameters:** `fullUnload` - If true, removes from cache entirely

---

### Vector2

2D vector math utility.

#### Constructor
```java
public Vector2(float x, float y)
```

#### Fields
```java
public float x, y
```

#### Methods

**add()**
```java
public void add(Vector2 other)
public void add(float x, float y)
```
Adds another vector.

**subtract()**
```java
public void subtract(Vector2 other)
public void subtract(float x, float y)
```
Subtracts another vector.

**multiply()**
```java
public void multiply(float scalar)
```
Multiplies by a scalar value.

**divide()**
```java
public void divide(float scalar)
```
Divides by a scalar value.

**magnitude()**
```java
public float magnitude()
```
Returns the vector's length.

**normalize()**
```java
public void normalize()
```
Normalizes the vector to unit length.

**distance()**
```java
public float distance(Vector2 other)
```
Returns distance to another vector.

**dot()**
```java
public float dot(Vector2 other)
```
Returns dot product with another vector.

**angle()**
```java
public float angle()
public float angle(Vector2 other)
```
Returns angle in radians.

---

### Shader

Interface for custom pixel shaders.

#### Method

**shade()**
```java
Color shade(int px, int py, float u, float v, int w, int h)
```
Computes color for a pixel.
- **Parameters:**
  - `px, py` - Pixel coordinates
  - `u, v` - Normalized coordinates (0.0 to 1.0)
  - `w, h` - Width and height of shaded region
- **Returns:** Color for this pixel

---

### GraphicsFidelity

Interface for graphics quality settings.

#### Methods

**useGlobalAA()**
```java
boolean useGlobalAA()
```
Enable anti-aliasing for all graphics.

**useQualityRendering()**
```java
boolean useQualityRendering()
```
Enable high-quality rendering (slower).

**useBilinearSampling()**
```java
boolean useBilinearSampling()
```
Enable bilinear interpolation for smooth image scaling.

**useSubPixelFontRendering()**
```java
boolean useSubPixelFontRendering()
```
Enable LCD subpixel text rendering.

**useAAForTextOnly()**
```java
boolean useAAForTextOnly()
```
Enable anti-aliasing only for text, not shapes.

---

### UniqueId

Unique ID generator for game objects.

#### Static Fields
```java
public static final long UNASSIGNED = -1
```

#### Static Methods

**generateGameObjectId()**
```java
public static long generateGameObjectId()
```
Generates a unique ID for a game object.

---

### ResourceType

Enum for resource types.

#### Values
```java
IMAGE
SOUND
```

---

### ScreenAnchor

Enum for screen coordinate anchor points.

#### Values
```java
TOP_LEFT       // Anchor to top-left corner (default)
TOP_CENTER     // Anchor to top-center
TOP_RIGHT      // Anchor to top-right corner
CENTER_LEFT    // Anchor to center-left
CENTER         // Anchor to center of screen
CENTER_RIGHT   // Anchor to center-right
BOTTOM_LEFT    // Anchor to bottom-left corner
BOTTOM_CENTER  // Anchor to bottom-center
BOTTOM_RIGHT   // Anchor to bottom-right corner
```

#### Methods

**getXOffset()**
```java
public int getXOffset(int screenWidth)
```
Calculates the X offset for this anchor point given the screen width.

**getYOffset()**
```java
public int getYOffset(int screenHeight)
```
Calculates the Y offset for this anchor point given the screen height.

**getXFactor()**
```java
public float getXFactor()
```
Gets the horizontal factor (0.0 = left, 0.5 = center, 1.0 = right).

**getYFactor()**
```java
public float getYFactor()
```
Gets the vertical factor (0.0 = top, 0.5 = center, 1.0 = bottom).

---

### AudioEffect

Interface for audio effects.

#### Method

**apply()**
```java
void apply(ResourceManager.Sound sound)
```
Applies an effect to sound PCM data.

---

### Random

Random number utility class.

#### Static Methods

**range()**
```java
public static int range(int min, int max)
public static float range(float min, float max)
```
Returns a random value in the specified range.

**chance()**
```java
public static boolean chance(float probability)
```
Returns true with the given probability (0.0 to 1.0).

---

## Managers

### ObjectHandler

Manages all game objects.

#### Static Methods

**init()**
```java
public static void init(GameCore core)
```
Initializes the ObjectHandler (called automatically).

**add()**
```java
public static long add(GameObject gameObject)
public static long[] add(GameObject... gameObjects)
```
Adds game object(s) to the engine.
- **Returns:** Unique ID(s)

**remove()**
```java
public static void remove(GameObject gameObject)
public static void remove(GameObject... gameObjects)
```
Removes game object(s) from the engine.

**getById()**
```java
public static GameObject getById(long id)
```
Gets a game object by its unique ID.
- **Returns:** GameObject or null if not found

**exists()**
```java
public static boolean exists(long id)
```
Checks if an object with the given ID exists.

**get()**
```java
public static List<GameObject> get(Class<? extends GameObject> targetClass)
```
Gets all objects of a specific type.

**updateInterfaces()**
```java
public static void updateInterfaces(Class<?> interfaceClass, String methodName, Object... args)
```
Calls a method on all objects implementing the specified interface.

**queueInterfaceUpdate()**
```java
public static void queueInterfaceUpdate(Class<?> interfaceClass, String methodName, Object... args)
```
Queues an interface update for the next frame.

---

### CollisionManager

Manages collision detection.

#### Static Methods

**register()**
```java
public static void register(Collidable c)
```
Registers a collidable object (called automatically).

**unregister()**
```java
public static void unregister(Collidable c)
```
Unregisters a collidable object (called automatically).

**clear()**
```java
public static void clear()
```
Clears all collision data.

**checkCollisions()**
```java
public static void checkCollisions(float delta)
```
Checks all collisions (called automatically each frame).

**renderCollisionBoxes()**
```java
public static void renderCollisionBoxes(Renderer r)
```
Debug method to visualize collision boxes.

---

### SoundManager

Manages audio playback.

#### Static Methods

**play()**
```java
public static void play(Resource resource, float volume, AudioEffect effect, String... tags)
```
Plays a sound with optional effects and tags.
- **Parameters:**
  - `resource` - Sound resource to play
  - `volume` - Volume level (0.0 to 1.0)
  - `effect` - Optional audio effect (can be null)
  - `tags` - Optional tags for sound control

**stopByTag()**
```java
public static void stopByTag(String tag)
```
Stops all sounds with the specified tag.

**stopByTags()**
```java
public static void stopByTags(String... tags)
```
Stops all sounds with any of the specified tags.

**stopAll()**
```java
public static void stopAll()
```
Stops all currently playing sounds.

**setMainVolume()**
```java
public static void setMainVolume(float volume)
```
Sets the master volume (0.0 to 1.0).

**getMainVolume()**
```java
public static float getMainVolume()
```
Gets the current master volume.

---

### ResourceManager

Low-level resource loading and caching (typically accessed via Resource class).

#### Static Methods

**load()**
```java
public static ResourceType load(String path, long resourceId)
```
Loads a resource from disk.

**fetch()**
```java
public static Object fetch(long id)
```
Fetches a loaded resource by ID.

**unload()**
```java
public static void unload(long id, boolean fullUnload)
```
Unloads a resource.

**clear()**
```java
public static void clear()
```
Clears all loaded resources.

**img()**
```java
static BufferedImage img(String path)
```
Loads an image file.

---

### ResourceManager.Sound

Inner class representing a loaded sound.

#### Methods

**getClip()**
```java
public Clip getClip()
```
Gets the audio clip.

**getFormat()**
```java
public AudioFormat getFormat()
```
Gets the audio format.

**getRawData()**
```java
public byte[] getRawData()
```
Gets the raw PCM audio data.

**getFormatStream()**
```java
public AudioInputStream getFormatStream()
```
Creates a new audio input stream from the raw data.

---

## Common Use Patterns

### Creating a Game Object

```java
public class MyObject extends GameObject implements ObjectRenderer {
    @Override
    public void play(GameCore gc) { /* init */ }
    
    @Override
    public void update(GameCore gc, float delta) { /* update */ }
    
    @Override
    public void render(GameCore gc, Renderer r) { /* render */ }
    
    // ObjectRenderer methods...
}
```

### Handling Collisions

```java
public class MyObject extends GameObject 
    implements Collidable, Transform, ObjectRenderer {
    
    @Override
    public List<Collider> getColliders() {
        return List.of(new Collider.RectangleCollider(0, 0, 50, 50, "body"));
    }
    
    @Override
    public void onEnter(GameObject other, String colliderId) {
        // Collision started
    }
    
    // Other required methods...
}
```

### Input Handling

```java
// Static checking (in update)
if (Input.isKeyDown(KeyEvent.VK_SPACE)) {
    jump();
}

// Event-based (implement KeyboardInput)
@Override
public void keyPressed(Input input, KeyEvent e, Integer keyCode) {
    if (keyCode == KeyEvent.VK_SPACE) {
        jump();
    }
}
```

---

For more information, see:
- [README.md](README.md) - Project overview
- [DOCUMENTATION.md](DOCUMENTATION.md) - Complete documentation
- [EXAMPLES.md](EXAMPLES.md) - Code examples
- [CONTRIBUTING.md](CONTRIBUTING.md) - Contribution guidelines
