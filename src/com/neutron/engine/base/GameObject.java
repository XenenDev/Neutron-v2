package com.neutron.engine.base;

import com.neutron.engine.GameCore;
import com.neutron.engine.ObjectHandler;
import com.neutron.engine.SoundHelper;
import com.neutron.engine.func.UniqueId;

public abstract class GameObject {
    private long id = UniqueId.UNASSIGNED; // Unassigned

    public GameObject() {
        ObjectHandler.add(this);
    }

    public long getId() {
        return id;
    }

    // Package-private setter for ObjectHandler
    public void setId(long id) {
        if (this.id != -1) {
            throw new IllegalStateException("ID already assigned!");
        }
        this.id = id;
    }

    public abstract void play(GameCore gameCore);
    public abstract void update(GameCore gameCore, float delta);

    public void delete() {
        SoundHelper.cleanup(this.id); // Cleanup sound rules
        ObjectHandler.remove(this);
    }
}