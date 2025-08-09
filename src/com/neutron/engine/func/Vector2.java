package com.neutron.engine.func;

public class Vector2 {

    public static final Vector2 CENTER = new Vector2(0, 0);
    public static final Vector2 UP_VECTOR = new Vector2(0, 1);
    public static final Vector2 DOWN_VECTOR = new Vector2(0, -1);
    public static final Vector2 RIGHT_VECTOR = new Vector2(1, 0);
    public static final Vector2 LEFT_VECTOR = new Vector2(-1, 0);

    public float x,y;
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 setXY(float x, float y) {
        this.x = x;
        this.y = y;

        return this;
    }

    public Vector2 setXY(int x, int y) {
        this.x = x;
        this.y = y;

        return this;
    }

    public Vector2 translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;

        return this;
    }

    public Vector2 translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;

        return this;
    }

    public Vector2 scale(float s) {
        this.x *= s;
        this.y *= s;

        return this;
    }

    public Vector2 multiply(float sx, float sy) {
        this.x *= sx;
        this.y *= sy;

        return this;
    }

    public Vector2 multiply(int sx, int sy) {
        this.x *= sx;
        this.y *= sy;

        return this;
    }

    public Vector2 multiply(Vector2 v) {
        this.x *= v.x;
        this.y *= v.y;

        return this;
    }

    public Vector2 divide(float sx, float sy) {
        this.x /= sx;
        this.y /= sy;

        return this;
    }

    public Vector2 divide(int sx, int sy) {
        this.x /= sx;
        this.y /= sy;

        return this;
    }

    public Vector2 divide(Vector2 v) {
        this.x /= v.x;
        this.y /= v.y;

        return this;
    }

    public Vector2 negate() {
        this.x *= -1;
        this.y *= -1;

        return this;
    }

    public static double distance(Vector2 v1, Vector2 v2) {
        float d1 = v1.x - v2.x;
        float d2 = v1.y - v2.y;
        return Math.sqrt(d1*d1 + d2*d2);
    }

    public static double distance(Vector2 v, float x1, float y1) {
        float d1 = x1 - v.x;
        float d2 = y1 - v.y;
        return Math.sqrt(d1*d1 + d2*d2);
    }

    public static double distance(float x1, float y1, float x2, float y2) {
        float d1 = x1 - x2;
        float d2 = y1 - y2;
        return Math.sqrt(d1*d1 + d2*d2);
    }

    public static double distanceSquared(Vector2 v1, Vector2 v2) {
        float d1 = v1.x - v2.x;
        float d2 = v1.y - v2.y;
        return d1*d1 + d2*d2;
    }

    public static double distanceSquared(Vector2 v, float x1, float y1) {
        float d1 = x1 - v.x;
        float d2 = y1 - v.y;
        return d1*d1 + d2*d2;
    }

    public static double distanceSquared(float x1, float y1, float x2, float y2) {
        float d1 = x1 - x2;
        float d2 = y1 - y2;
        return d1*d1 + d2*d2;
    }

    public double distanceTo(float x, float y) {
        float d1 = this.x - x;
        float d2 = this.y - y;
        return Math.sqrt(d1*d1 + d2*d2);
    }

    public double distanceTo(Vector2 v) {
        float d1 = this.x - v.x;
        float d2 = this.y - v.y;
        return Math.sqrt(d1*d1 + d2*d2);
    }

    public double distanceToSquared(float x, float y) {
        float d1 = this.x - x;
        float d2 = this.y - y;
        return d1*d1 + d2*d2;
    }

    public double distanceToSquared(Vector2 v) {
        float d1 = this.x - v.x;
        float d2 = this.y - v.y;
        return d1*d1 + d2*d2;
    }

    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    public Vector2 normalize() {
        double dist = this.magnitude();
        this.x /= (float) dist;
        this.y /= (float) dist;

        return this;
    }

    public static Vector2 normalize(Vector2 v) {
        double dist = v.magnitude();
        return new Vector2(v.x /= (float) dist, v.y /= (float) dist);
    }

    public static double dot(Vector2 v1, Vector2 v2) {
        Vector2 uv1 = Vector2.normalize(v1);
        Vector2 uv2 = Vector2.normalize(v2);

        return (uv1.x * uv2.x) + (uv1.y * uv2.y);
    }

    public static float lerp(int a, int b, float alpha) {
        return a + (b - a) * alpha;
    }

    public static float lerp(float a, float b, float alpha) {
        return a + (b - a) * alpha;
    }

    public static float sqrtLerp(float a, float b, float alpha) {
        return (float) Math.sqrt( a*a + (b*b - a*a) * alpha);
    }

    public static Vector2 lerp(Vector2 vec1, Vector2 vec2, float alpha) {
        return new Vector2(lerp(vec1.x, vec2.x, alpha), lerp(vec1.y, vec2.y, alpha));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 v = (Vector2) o;
        return x == v.x && y == v.y;
    }

    @Override
    public String toString() {
        return "Vector2(" + x + ", " + y + ')';
    }
}
