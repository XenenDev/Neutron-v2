package com.neutron.engine.base.interfaces;

import com.neutron.engine.ResourceManager;

public interface SoundEmitter {

    ResourceManager.Sound sounds();

    Float volume();

    Boolean playConditions();

}
