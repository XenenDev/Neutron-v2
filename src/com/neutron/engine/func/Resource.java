package com.neutron.engine.func;

import com.neutron.engine.ResourceManager;

public class Resource {

    private final long id;
    private final String path;
    private final ResourceType type;

    public Resource(String path) {
        this.id = UniqueId.generateResourceId();
        this.path = path;
        this.type = ResourceManager.load(path, this.id);
    }

    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public ResourceType getType() {
        return type;
    }

    public Object get() {
        return ResourceManager.fetch(this.id);
    }
}
