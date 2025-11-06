package com.neutron.engine;

import com.neutron.engine.base.*;
import com.neutron.engine.base.interfaces.Collidable;
import com.neutron.engine.base.interfaces.ObjectRenderer;
import com.neutron.engine.base.interfaces.UIObjectRenderer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectHandler {

    private static GameCore gameCore;

    private static final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private static final ArrayList<ObjectRenderer> toRenderList = new ArrayList<>();
    private static final ArrayList<UIObjectRenderer> toRenderUIList = new ArrayList<>();

    private static final List<Runnable> postUpdateTasks = new ArrayList<>();

    public static void init(GameCore core) {
        if (gameCore != null) {
            throw new IllegalStateException("ObjectHandler already initialized!");
        }
        gameCore = core;
    }

    public static void updateObjects(GameCore gameCore, float delta) {
        ObjectHandler.gameCore = gameCore;
        // Run any deferred object additions/removals after interface calls
        List<Runnable> snapshot = new ArrayList<>(postUpdateTasks);
        snapshot.forEach(Runnable::run); //FIXME: Can cause null pointer exception when a GO is deleted, and a later function tries to modify or reference the now non-existent game object
        postUpdateTasks.clear();

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

            r.graphics.translate(x, y);
            r.graphics.scale(scale, scale);
            r.graphics.rotate(Math.toRadians(rotation));
            renderObject.render(gameCore, r);
            r.graphics.rotate(-Math.toRadians(rotation));
            r.graphics.scale(1d/scale, 1d/scale);
            r.graphics.translate(-x, -y);
        }

        //render lightmap above everything
        //r.drawLightmap(); TODO
    }


    public static void renderUIObjects(GameCore gameCore, Renderer r) {
        r.setUseScreenCoordinates(true);

        for (UIObjectRenderer uiObject : toRenderUIList) {
            uiObject.renderUI(gameCore, r);
        }

        r.setUseScreenCoordinates(false);
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

    public static void add(GameObject... gameObjects) {
        for (GameObject gameObject : gameObjects) {
            add(gameObject);
        }
    }

    public static void add(GameObject gameObject) {
        postUpdateTasks.add(() -> {
            gameObjects.add(gameObject);

            if (gameObject instanceof ObjectRenderer) toRenderList.add((ObjectRenderer) gameObject);
            if (gameObject instanceof UIObjectRenderer) toRenderUIList.add((UIObjectRenderer) gameObject);
            if (gameObject instanceof Collidable) CollisionManager.register((Collidable) gameObject);

           gameObject.play(ObjectHandler.gameCore);
        });
    }


    public static void remove(GameObject gameObject) {
        postUpdateTasks.add(() -> {
            gameObjects.remove(gameObject);

            if (gameObject instanceof ObjectRenderer) toRenderList.remove(gameObject);
            if (gameObject instanceof UIObjectRenderer) toRenderUIList.remove(gameObject);
            if (gameObject instanceof Collidable) CollisionManager.unregister((Collidable) gameObject);
        });
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
