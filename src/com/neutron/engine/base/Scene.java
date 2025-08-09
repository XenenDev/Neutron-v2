package com.neutron.engine.base;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene { //TODO work in progress.
    protected final List<GameObject> gameObjects = new ArrayList<>();

    public void addObject(GameObject obj) {
        gameObjects.add(obj);
    }

    public void removeObject(GameObject obj) {
        gameObjects.remove(obj);
    }

    public List<GameObject> getObjects() {
        return gameObjects;
    }

    // Force subclasses to define these
    public abstract void init();
    public abstract void update(float deltaTime);
    public abstract void render();
}
