package com.neutron.engine.base.interfaces;

import com.neutron.engine.GameCore;
import com.neutron.engine.Renderer;

public interface ObjectRenderer extends Transform {

    void render(GameCore gameCore, Renderer r);

    int getZDepth();

}
