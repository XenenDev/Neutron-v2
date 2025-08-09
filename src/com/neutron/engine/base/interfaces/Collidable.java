package com.neutron.engine.base.interfaces;

import com.neutron.engine.base.GameObject;
import com.neutron.engine.func.Collider;

import java.util.List;

public interface Collidable extends Transform, Movable {

    List<Collider> getColliders();

    default void duringCollision(GameObject other, float delta) {}

    default void onEnter(GameObject other, String id) {}

    default void onExit(GameObject other) {}

}
