package com.neutron.engine.func;

import com.neutron.engine.base.interfaces.Transform;


public abstract class Collider {
    private final String id;

    public String getId() {
        return id;
    }

    public Collider(String id) {
        this.id = id;
    }


    public abstract boolean intersects(Collider other);

    /** Small tolerance for edge-case collisions */
    private static final double EPSILON = 0.1;

    /** AABB vs. AABB test with small tolerance for edge cases. */
    public static boolean checkRectangleIntersection(RectangleCollider r1, RectangleCollider r2) {
        return r1.x < r2.x + r2.width + EPSILON &&
                r1.x + r1.width + EPSILON > r2.x &&
                r1.y < r2.y + r2.height + EPSILON &&
                r1.y + r1.height + EPSILON > r2.y;
    }

    /**
     * Transforms a local-space collider into world-space using the given Transform.
     */
    public Collider globalize(Transform t) {
        double scale = t.getScale();
        int xOff = t.getX();
        int yOff = t.getY();

        if (this instanceof RectangleCollider r) {
            return new RectangleCollider(
                    r.x * scale + xOff,
                    r.y * scale + yOff,
                    r.width * scale,
                    r.height * scale,
                    id
            );
        } else if (this instanceof CircleCollider c) {
            return new CircleCollider(
                    c.x * scale + xOff,
                    c.y * scale + yOff,
                    c.radius * scale,
                    id
            );
        }
        throw new IllegalStateException("Unknown collider type");
    }

    /**
     * Axis-Aligned Rectangle (AABB) collider.
     */
    public static class RectangleCollider extends Collider {
        public final double x, y, width, height;

        public RectangleCollider(double x, double y, double width, double height, String tag) {
            super(tag);
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean intersects(Collider other) {
            if (other instanceof RectangleCollider r) {
                return checkRectangleIntersection(this, r);
            } else if (other instanceof CircleCollider c) {
                // circle-rectangle SAT
                double closestX = clamp(c.x, this.x, this.x + this.width);
                double closestY = clamp(c.y, this.y, this.y + this.height);
                double dx = c.x - closestX;
                double dy = c.y - closestY;
                return dx*dx + dy*dy <= c.radius * c.radius;
            }
            return false;
        }
    }

    /**
     * Circle collider.
     */
    public static class CircleCollider extends Collider {
        public final double x, y, radius;

        public CircleCollider(double x, double y, double radius, String id) {
            super(id);
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        @Override
        public boolean intersects(Collider other) {
            if (other instanceof CircleCollider c) {
                double dx = this.x - c.x;
                double dy = this.y - c.y;
                double rsum = this.radius + c.radius;
                return dx*dx + dy*dy <= rsum * rsum;
            } else if (other instanceof RectangleCollider r) {
                // defer to rect's logic
                return r.intersects(this);
            }
            return false;
        }
    }

    private static double clamp(double v, double min, double max) {
        return v < min ? min : Math.min(v, max);
    }
}
