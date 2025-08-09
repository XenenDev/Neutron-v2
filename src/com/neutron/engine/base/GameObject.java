package com.neutron.engine.base;

import com.neutron.engine.GameCore;
import com.neutron.engine.ObjectHandler;

public abstract class GameObject {

    public GameObject() {
        ObjectHandler.add(this);
    }

    public abstract void play(GameCore gameCore);

    public abstract void update(GameCore gameCore, float delta);

    public void delete() {
        ObjectHandler.remove(this);
    }

}
