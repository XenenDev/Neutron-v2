package com.neutron.engine;

import com.neutron.engine.base.*;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.ui.UIGroup;
import com.neutron.engine.base.interfaces.ui.UIObject;
import com.neutron.engine.func.UniqueId;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectHandler {

    private static GameCore gameCore;

    private static final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private static final ArrayList<ObjectRenderer> toRenderList = new ArrayList<>();
    private static final ArrayList<UIGroup> uiObjects = new ArrayList<>();
    private static UIObject focusedUIObject = null;

    private static final List<Runnable> postUpdateTasks = new ArrayList<>();
    private static final HashMap<Long, GameObject> gameObjectsById = new HashMap<>();

    public static void init(GameCore core) {
        if (gameCore != null) {
            throw new IllegalStateException("ObjectHandler already initialized!");
        }
        gameCore = core;
    }

    public static UIObject getFocusedUIObject() {
        return focusedUIObject;
    }

    public static void updateObjects(GameCore gameCore, float delta) {
        ObjectHandler.gameCore = gameCore;
        // Run any deferred object additions/removals after interface calls
        // Clear before running so tasks added during execution are preserved for next update
        List<Runnable> snapshot = new ArrayList<>(postUpdateTasks);
        postUpdateTasks.clear();
        snapshot.forEach(Runnable::run);

        for (GameObject gameObject : gameObjects) {
            gameObject.update(gameCore, delta);
        }
    }

    public static void renderObjects(GameCore gameCore, Renderer r) {
        // Sort objects by z-depth
        toRenderList.sort(Comparator.comparingDouble(ObjectRenderer::getZDepth));

        List<ObjectRenderer> snapshot = new ArrayList<>(toRenderList);
        // Loop through each render object
        for (ObjectRenderer renderObject : snapshot) {
            int x = renderObject.getX(), y = renderObject.getY();
            double scale = renderObject.getScale();
            double rotation = renderObject.getRotation();

            if (scale == 0) scale = 1e-6D;  // Avoid zero scaling

            r.setAlpha(1f);

            double px = renderObject.getPivotX();
            double py = renderObject.getPivotY();
            r.graphics.translate(x, y);
            r.graphics.translate(px, py);
            r.graphics.rotate(Math.toRadians(rotation));
            r.graphics.translate(-px, -py);
            r.graphics.scale(scale, scale);
            renderObject.render(gameCore, r);
            r.graphics.scale(1d/scale, 1d/scale);
            r.graphics.translate(px, py);
            r.graphics.rotate(-Math.toRadians(rotation));
            r.graphics.translate(-px, -py);
            r.graphics.translate(-x, -y);
        }

        //render lightmap above everything
        //r.drawLightmap(); TODO
    }


    public static void renderUIObjects(GameCore gameCore, Renderer r) {
        r.setUseScreenCoordinates(true);

        for (UIGroup uiGroup : uiObjects) {
            for (UIObject uiObject : uiGroup.objects()) {
                uiObject.renderUI(gameCore, r);
            }
        }

        r.setUseScreenCoordinates(false);
    }

    public static void sendUIObjectUpdates(int mouseX, int mouseY) {
        for (UIGroup uiGroup : uiObjects) {
            for (UIObject uiObject : uiGroup.objects()) {
                if (uiObject.isBeingPressed(mouseX, mouseY)) {
                    uiObject.onPress();
                    if (uiObject.canFocus()) focusedUIObject = uiObject;
                }
            }
        }
    }

    public static void queueInterfaceUpdate(Class<?> interfaceClass, String methodName, Object... args) {
        postUpdateTasks.add(() -> updateInterfaces(interfaceClass, methodName, args));
    }

    //Thank you chatGPT.
    public static void updateInterfaces(Class<?> interfaceClass, String methodName, Object... args) {
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < types.length; i++) types[i] = args[i].getClass();

        List<GameObject> snapshot = new ArrayList<>(gameObjects);
        snapshot.stream().filter(interfaceClass::isInstance)
                .forEach(gameObject -> {
                    try {
                        interfaceClass.getMethod(methodName, types).invoke(gameObject, args);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static long[] add(GameObject... gameObjects) {
        long[] ids = new long[gameObjects.length];
        int i = 0;
        for (GameObject gameObject : gameObjects) {
            ids[i++] = ObjectHandler.add(gameObject);
        }
        return ids;
    }

    public static long add(GameObject gameObject) {
        long id = UniqueId.generateGameObjectId();
        gameObject.setId(id); // Assign ID immediately
        gameObjectsById.put(id, gameObject); // Add to lookup map immediately
        postUpdateTasks.add(() -> {
            gameObjects.add(gameObject);

            if (gameObject instanceof ObjectRenderer) toRenderList.add((ObjectRenderer) gameObject);
            if (gameObject instanceof UIGroup) uiObjects.add((UIGroup) gameObject);
            if (gameObject instanceof Collidable) CollisionManager.register((Collidable) gameObject);

            gameObject.play(ObjectHandler.gameCore);
        });
        return id;
    }

    public static void remove(GameObject gameObject) {
        postUpdateTasks.add(() -> {
            gameObjects.remove(gameObject);
            gameObjectsById.remove(gameObject.getId()); // Remove from map

            if (gameObject instanceof ObjectRenderer) toRenderList.remove(gameObject);
            if (gameObject instanceof UIGroup) uiObjects.remove(gameObject);
            if (gameObject instanceof Collidable) CollisionManager.unregister((Collidable) gameObject);
        });
    }

    public static GameObject getById(long id) {
        return gameObjectsById.get(id); // Returns null if not found
    }

    public static void removeObjectById(long id) {
        GameObject obj = getById(id);
        if (obj != null) {
            remove(obj);
        }
    }

    public static boolean exists(long id) {
        return gameObjectsById.containsKey(id);
    }

    public static void remove(GameObject... gameObjects) {
        for (GameObject gameObject : gameObjects) {
            remove(gameObject);
        }
    }

    public static List<GameObject> get(Class<? extends GameObject> targetClass) {
        return gameObjects.stream()
                .filter(targetClass::isInstance)
                .collect(Collectors.toList());
    }

}
