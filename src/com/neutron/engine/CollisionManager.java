package com.neutron.engine;

import com.neutron.engine.base.GameObject;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.Movable;
import com.neutron.engine.base.interfaces.Transform;
import com.neutron.engine.func.Collider;
import com.neutron.engine.func.Collider.CircleCollider;
import com.neutron.engine.func.Collider.RectangleCollider;

import java.awt.Color;
import java.util.*;

/**
 * A collision manager that does:
 *  - Discrete intersection for all shapes
 *  - Swept‐AABB for fast RectangleCollider vs. RectangleCollider
 */
public class CollisionManager {
    private static final List<Collidable> collidables = new ArrayList<>();
    private static final Set<CollisionPair> prev = new HashSet<>();
    private static final Set<CollisionPair> curr = new HashSet<>();

    public static void register(Collidable c) {
        if (!collidables.contains(c)) collidables.add(c);
    }

    public static void unregister(Collidable c) {
        collidables.remove(c);
        prev.removeIf(p -> p.has(c));
        curr.removeIf(p -> p.has(c));
    }

    public static void clear() {
        collidables.clear();
        prev.clear();
        curr.clear();
    }

    public static void renderCollisionBoxes(Renderer r) {
        for (Collidable c : collidables) {
            Random rnd = new Random(c.hashCode());
            for (Collider collider : c.getColliders()) {
                Color col = new Color(rnd.nextInt(106) + 150,
                        rnd.nextInt(106) + 150,
                        rnd.nextInt(106) + 150,
                        180);
                Collider g = collider.globalize(c);
                if (g instanceof RectangleCollider R) {
                    r.fillRect((int)R.x, (int)R.y, (int)R.width, (int)R.height, col);
                } else if (g instanceof CircleCollider C) {
                    r.fillCircle((int)C.x, (int)C.y, (int)C.radius, col);
                }
            }
        }
    }

    public static void checkCollisions(float delta) {
        curr.clear();
        Set<CollisionPair> done = new HashSet<>();

        for (int i = 0; i < collidables.size(); i++) {
            for (int j = i + 1; j < collidables.size(); j++) {
                Collidable A = collidables.get(i);
                Collidable B = collidables.get(j);
                CollisionPair key = new CollisionPair(A, B);
                if (done.contains(key)) continue;

                Set<CollisionPair> collidingPairs = new HashSet<>();

                for (Collider ca : A.getColliders()) {
                    for (Collider cb : B.getColliders()) {
                        if (ca == null || cb == null) continue;
                        Collider gA = ca.globalize(A);
                        Collider gB = cb.globalize(B);

                        boolean intersects = false;

                        // Case 1: fast swept‐AABB for two rectangles on Movables
                        if (gA instanceof RectangleCollider ra &&
                                gB instanceof RectangleCollider rb &&
                                A instanceof Movable mA &&
                                B instanceof Movable mB) {

                            float dx = mA.getVx() * delta;
                            float dy = mA.getVy() * delta;

                            // 1. Discrete check
                            if (ra.intersects(rb)) {
                                intersects = true;
                            }

                            // 2. Swept‐AABB only if non‐trivial motion
                            if (!intersects && (Math.abs(dx) > 1e-3f || Math.abs(dy) > 1e-3f)) {
                                float minX = (float) Math.min(ra.x, ra.x + dx);
                                float minY = (float) Math.min(ra.y, ra.y + dy);
                                float  w   = (float) (ra.width  + Math.abs(dx));
                                float  h   = (float) (ra.height + Math.abs(dy));

                                RectangleCollider swept =
                                        new RectangleCollider(minX, minY, w, h, ra.getId());

                                if (swept.intersects(rb) && sweptAABB(ra, dx, dy, rb)) {
                                    intersects = true;
                                }
                            }

                        } else {
                            // Case 2: any other combination → discrete
                            if (gA.intersects(gB)) {
                                intersects = true;
                            }
                        }

                        if (intersects) {
                            CollisionPair p = new CollisionPair(A, B, ca.getId(), cb.getId());
                            collidingPairs.add(p);
                        }
                    }
                }

                if (!collidingPairs.isEmpty()) {
                    done.add(key);
                    for (CollisionPair p : collidingPairs) {
                        curr.add(p);
                        if (!prev.contains(p)) {
                            p.objA().onEnter((GameObject)p.objB(), p.colliderIdB);
                            p.objB().onEnter((GameObject)p.objA(), p.colliderIdA);
                        }
                    }
                    A.duringCollision((GameObject)B, delta);
                    B.duringCollision((GameObject)A, delta);
                }
            }
        }

        // exit callbacks
        for (CollisionPair p : prev) {
            if (!curr.contains(p)) {
                p.objA().onExit((GameObject)p.objB(), p.colliderIdB);
                p.objB().onExit((GameObject)p.objA(), p.colliderIdA);
            }
        }

        prev.clear();
        prev.addAll(curr);
    }

    /** Narrow‐phase swept‐AABB test */
    private static boolean sweptAABB(
            RectangleCollider m, float dx, float dy, RectangleCollider t
    ) {
        float xEntry, yEntry, xExit, yExit;

        // Handle zero velocity cases to avoid division by zero
        if (Math.abs(dx) < 1e-6f) {
            // No X movement - check if already overlapping on X axis
            if (m.x + m.width <= t.x || m.x >= t.x + t.width) {
                return false; // No X overlap, can never collide
            }
            xEntry = Float.NEGATIVE_INFINITY;
            xExit = Float.POSITIVE_INFINITY;
        } else if (dx > 0) {
            xEntry = (float) ((t.x - (m.x + m.width)) / dx);
            xExit  = (float) (((t.x + t.width) - m.x) / dx);
        } else {
            xEntry = (float) (((t.x + t.width) - m.x) / dx);
            xExit  = (float) ((t.x - (m.x + m.width)) / dx);
        }

        if (Math.abs(dy) < 1e-6f) {
            // No Y movement - check if already overlapping on Y axis
            if (m.y + m.height <= t.y || m.y >= t.y + t.height) {
                return false; // No Y overlap, can never collide
            }
            yEntry = Float.NEGATIVE_INFINITY;
            yExit = Float.POSITIVE_INFINITY;
        } else if (dy > 0) {
            yEntry = (float) ((t.y - (m.y + m.height)) / dy);
            yExit  = (float) (((t.y + t.height) - m.y) / dy);
        } else {
            yEntry = (float) (((t.y + t.height) - m.y) / dy);
            yExit  = (float) ((t.y - (m.y + m.height)) / dy);
        }

        float entryTime = Math.max(xEntry, yEntry);
        float exitTime  = Math.min(xExit,  yExit);

        return entryTime <= exitTime && entryTime >= 0f && entryTime <= 1f;
    }

    private record CollisionPair(
            Collidable objA,
            Collidable objB,
            String     colliderIdA,
            String     colliderIdB
    ) {
        /** Convenience ctor for two-object key */
        public CollisionPair(Collidable a, Collidable b) {
            this(a, b, null, null);
        }

        /** Enforce ordering so (A,B) == (B,A) when IDs match */
        private CollisionPair(
                Collidable objA,
                Collidable objB,
                String     colliderIdA,
                String     colliderIdB
        ) {
            if (objA.hashCode() <= objB.hashCode()) {
                this.objA        = objA;
                this.objB        = objB;
                this.colliderIdA = colliderIdA;
                this.colliderIdB = colliderIdB;
            } else {
                this.objA        = objB;
                this.objB        = objA;
                this.colliderIdA = colliderIdB;
                this.colliderIdB = colliderIdA;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CollisionPair that)) return false;
            return Objects.equals(objA, that.objA)
                    && Objects.equals(objB, that.objB)
                    && Objects.equals(colliderIdA, that.colliderIdA)
                    && Objects.equals(colliderIdB, that.colliderIdB);
        }

        @Override
        public int hashCode() {
            return Objects.hash(objA, objB, colliderIdA, colliderIdB);
        }

        /** True if this pair involves the given object */
        public boolean has(Collidable c) {
            return objA.equals(c) || objB.equals(c);
        }
    }
}
