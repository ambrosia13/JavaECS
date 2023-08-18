package io.github.ambrosia.ecs;

import java.util.Arrays;

public class Commands {
	private final ECS ecs;

	public Commands(ECS ecs) {
		this.ecs = ecs;
	}

	/**
	 * Spawns an entity with the specified components.
	 * @param components the components to add
	 */

	// TODO: Because of how entities are added, components are always grouped by entity rather than type (so no locality).
	// This doesn't provide much of an advantage performance-wise over OOP. I'll need to rethink this approach soon.
	public Commands spawn(Component... components) {
		// Create a new entity...
		int entityIndex = this.ecs.entities.create();

		// ...and attach the components to that entity.
		Arrays.stream(components)
			.forEach(component -> {
				this.ecs.components.add(new ECS.AttachedComponent(component, entityIndex));
			});

		return this;
	}
}
