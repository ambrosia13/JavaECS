package io.github.ambrosia.ecs;

import io.github.ambrosia.ecs.query.Query;

import java.util.ArrayList;
import java.util.Arrays;

public class EntityComponentSystem {
	public record AttachedComponent(Component component, int index) {}

	private final Resources resources;
	private final Systems systems;
	private final Entities entities;

	// ArrayList might not be the best data structure for this
	private final ArrayList<AttachedComponent> components;


	public EntityComponentSystem() {
		this.entities = new Entities();
		this.components = new ArrayList<>();
		this.systems = new Systems(this);
		this.resources = new Resources();
	}

	/**
	 * Returns an object that is able to query many things about the ECS, like its components.
	 */
	public Query query() {
		return new Query(this.components, this.entities);
	}

	public Systems systems() {
		return this.systems;
	}

	public Resources resources() {
		return this.resources;
	}

	/**
	 * Spawns an entity with the specified components.
	 * @param components the components to add
	 */
	// TODO: Because of how entities are added, components are always grouped by entity rather than type (so no locality).
	// This doesn't provide much of an advantage performance-wise over OOP. I'll need to rethink this approach soon.
	public EntityComponentSystem spawn(Component... components) {
		// Create a new entity...
		int entityIndex = this.entities.create();

		// ...and attach the components to that entity.
		Arrays.stream(components)
			.forEach(component -> this.components.add(new AttachedComponent(component, entityIndex)));

		return this;
	}
}
