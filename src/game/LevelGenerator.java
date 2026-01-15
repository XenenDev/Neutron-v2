package game;

import com.neutron.engine.ObjectHandler;
import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Transform;
import game.primitives.Block;
import game.primitives.Spike;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Handles procedural level generation with plateaus of varying heights
 * and random spike placement.
 * 
 * The generator tracks world-space coordinates that increase as the level
 * scrolls. New terrain is spawned at fixed world positions, while blocks
 * move left at a constant speed.
 */
public class LevelGenerator {

    private final List<Long> obstacleIds = new ArrayList<>();
    private final Random random = new Random();

    // Configuration
    private float speed = 5f;                // Pixels per frame (at 60fps)
    private int screenWidth = 1000;          // Screen width in pixels
    private int spawnBuffer = 200;           // Spawn this far ahead of screen edge
    private int despawnX = -100;             // Despawn blocks past this x position

    // World tracking - worldOffset represents how far we've "scrolled" into the level
    private float worldOffset = 0;           // Accumulates over time based on speed
    private float nextSpawnWorldX = 0;       // World X position where next column spawns

    // Procedural generation state
    private int currentHeight;
    private int currentPlateauRemaining = 0;
    private int consecutiveSpikes = 0;
    private int gapSinceLastSpike = 0;
    private boolean isIntro = true;
    private int introRemaining = 0;

    // Height levels (grid Y coordinates - lower value = higher on screen)
    private static final int[] HEIGHT_LEVELS = {7, 8, 9, 10};
    private static final int MIN_PLATEAU_LENGTH = 3;
    private static final int MAX_PLATEAU_LENGTH = 8;
    private static final int GRID_SIZE = 50;

    // Spike configuration
    private static final double SPIKE_CHANCE = 0.30;
    private static final int MAX_CONSECUTIVE_SPIKES = 3;
    private static final int MIN_GAP_AFTER_MAX_SPIKES = 2;

    public LevelGenerator() {
    }

    public LevelGenerator(float speed, int screenWidth) {
        this.speed = speed;
        this.screenWidth = screenWidth;
    }

    /**
     * Clears all existing obstacles and resets generation state.
     */
    public void clear() {
        for (long id : obstacleIds) {
            ObjectHandler.removeObjectById(id);
        }
        obstacleIds.clear();
        resetState();
    }

    /**
     * Resets the procedural generation state.
     */
    private void resetState() {
        worldOffset = 0;
        nextSpawnWorldX = 0;
        currentHeight = HEIGHT_LEVELS[HEIGHT_LEVELS.length - 1];  // Start at lowest height
        currentPlateauRemaining = 0;
        consecutiveSpikes = 0;
        gapSinceLastSpike = 0;
        isIntro = true;
        introRemaining = 25;  // Safe blocks at start (no spikes for first 20-30 blocks)
    }

    /**
     * Initializes the level - call once at game start/restart.
     */
    public void generate() {
        clear();
        
        // Fill initial screen plus buffer with terrain
        float fillDistance = screenWidth + spawnBuffer;
        while (nextSpawnWorldX < fillDistance) {
            spawnNextColumn();
        }
    }

    /**
     * Updates the procedural generation - call every frame.
     * @param delta Time since last frame in seconds (use 1.0 for fixed timestep)
     */
    public void update(float delta) {
        // Advance world offset based on scroll speed
        worldOffset += speed * delta;

        // Calculate the world X that corresponds to the right edge of spawn zone
        float spawnEdgeWorldX = worldOffset + screenWidth + spawnBuffer;

        // Spawn new columns as needed
        while (nextSpawnWorldX < spawnEdgeWorldX) {
            spawnNextColumn();
        }

        // Clean up off-screen obstacles
        despawnBehind();
    }

    /**
     * Backwards compatible update without delta (assumes delta of ~1/60).
     */
    public void update() {
        update(1f / 60f);
    }

    /**
     * Spawns the next column of terrain at the current world position.
     */
    private void spawnNextColumn() {
        // Start a new plateau if needed
        if (currentPlateauRemaining <= 0) {
            startNewPlateau();
        }

        // Calculate screen X position for this column
        // screenX = worldX - worldOffset (world coords to screen coords)
        float screenX = nextSpawnWorldX - worldOffset;
        int gridX = Math.round(screenX / GRID_SIZE);
        int gridY = currentHeight;

        // Spawn the ground block
        Block block = new Block(gridX, gridY, speed);
        obstacleIds.add(block.getId());

        // Determine if we should spawn a spike
        boolean canSpawnSpike = !isIntro && currentPlateauRemaining > 1;

        if (canSpawnSpike && shouldSpawnSpike()) {
            Spike spike = new Spike(gridX, gridY - 1, speed);
            obstacleIds.add(spike.getId());
            consecutiveSpikes++;
            gapSinceLastSpike = 0;
        } else {
            if (consecutiveSpikes > 0) {
                gapSinceLastSpike++;
            }
            consecutiveSpikes = 0;
        }

        // Advance to next spawn position
        nextSpawnWorldX += GRID_SIZE;
        currentPlateauRemaining--;

        // Handle intro phase
        if (isIntro) {
            introRemaining--;
            if (introRemaining <= 0) {
                isIntro = false;
            }
        }
    }

    /**
     * Starts a new plateau with a random height and length.
     */
    private void startNewPlateau() {
        // Pick new height (with smooth transitions)
        currentHeight = pickNextHeight(currentHeight);

        // Random plateau length
        currentPlateauRemaining = MIN_PLATEAU_LENGTH +
                random.nextInt(MAX_PLATEAU_LENGTH - MIN_PLATEAU_LENGTH + 1);

        // Reset spike tracking
        consecutiveSpikes = 0;
        gapSinceLastSpike = 2;  // Allow spike placement from second block
    }

    /**
     * Removes obstacles that have moved off-screen to the left.
     */
    private void despawnBehind() {
        Iterator<Long> iterator = obstacleIds.iterator();
        while (iterator.hasNext()) {
            long id = iterator.next();
            GameObject obj = ObjectHandler.getById(id);

            if (obj == null) {
                iterator.remove();
                continue;
            }

            if (obj instanceof Transform transform) {
                int x = transform.getX();
                if (x < despawnX) {
                    ObjectHandler.removeObjectById(id);
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Picks the next height level, preferring smooth transitions.
     */
    private int pickNextHeight(int currentHeight) {
        int currentIndex = getHeightIndex(currentHeight);

        // 35% chance to keep same height
        if (random.nextDouble() < 0.35) {
            return currentHeight;
        }

        // Determine transition size
        int maxOffset;
        double roll = random.nextDouble();
        if (roll < 0.7) {
            maxOffset = 1;  // 70% chance: small transition
        } else {
            maxOffset = 2;  // 30% chance: larger jump
        }

        // Random direction
        int direction = random.nextBoolean() ? 1 : -1;
        int offset = direction * (1 + random.nextInt(maxOffset));

        // Clamp to valid range
        int newIndex = Math.max(0, Math.min(HEIGHT_LEVELS.length - 1, currentIndex + offset));

        return HEIGHT_LEVELS[newIndex];
    }

    /**
     * Gets the index of a height in the HEIGHT_LEVELS array.
     */
    private int getHeightIndex(int height) {
        for (int i = 0; i < HEIGHT_LEVELS.length; i++) {
            if (HEIGHT_LEVELS[i] == height) {
                return i;
            }
        }
        return HEIGHT_LEVELS.length - 1;
    }

    /**
     * Determines whether a spike should spawn at the current position.
     */
    private boolean shouldSpawnSpike() {
        // Enforce maximum consecutive spikes with mandatory gap
        if (consecutiveSpikes >= MAX_CONSECUTIVE_SPIKES) {
            return false;
        }

        // If we just had max spikes, enforce minimum gap
        if (gapSinceLastSpike < MIN_GAP_AFTER_MAX_SPIKES && gapSinceLastSpike > 0) {
            return false;
        }

        // Calculate spawn chance with adjustments
        double chance = SPIKE_CHANCE;

        // Increase chance if there's been a gap
        if (gapSinceLastSpike >= 3) {
            chance += 0.15;
        } else if (gapSinceLastSpike >= 2) {
            chance += 0.08;
        }

        // Decrease chance for consecutive spikes (makes long spike runs rarer)
        if (consecutiveSpikes > 0) {
            chance -= 0.08 * consecutiveSpikes;
        }

        return random.nextDouble() < chance;
    }

    /**
     * Gets the list of all spawned obstacle IDs.
     */
    public List<Long> getObstacleIds() {
        return new ArrayList<>(obstacleIds);
    }

    /**
     * Gets the current number of active obstacles.
     */
    public int getObstacleCount() {
        return obstacleIds.size();
    }

    /**
     * Sets the scroll speed for obstacles.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Gets the current scroll speed.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the screen width for spawn calculations.
     */
    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    /**
     * Gets the current world offset (total distance scrolled).
     */
    public float getWorldOffset() {
        return worldOffset;
    }
}
