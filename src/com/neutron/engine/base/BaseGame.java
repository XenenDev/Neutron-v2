package com.neutron.engine.base;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;

public abstract class BaseGame {

    public abstract void play(GameCore gameCore, Renderer r);
    public abstract void update(GameCore gameCore, Renderer r, float delta);

}
