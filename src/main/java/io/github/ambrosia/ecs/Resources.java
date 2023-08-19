package io.github.ambrosia.ecs;

import java.util.ArrayList;

public class Resources {
	ArrayList<Resource> resources;

	public Resources() {
		this.resources = new ArrayList<>();
	}

	public <T extends Resource> Resources init(ResourceInitializer<T> initializer) {
		this.add(initializer.init());
		return this;
	}

	public Resources add(Resource resource) {
		this.resources.add(resource);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T extends Resource> T get(Class<T> type) {
		for (var resource : this.resources) {
			if (resource.getClass().equals(type)) return (T) resource;
		}

		throw new IllegalStateException("Tried to access resource that wasn't yet inserted in the ECS");
	}
}
