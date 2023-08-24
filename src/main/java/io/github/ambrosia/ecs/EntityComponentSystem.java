package io.github.ambrosia.ecs;

import io.github.ambrosia.ecs.query.Query;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The central engine of the ECS. There should only be one instance of this
 * per "game". There is no built-in functionality for e.g. running appls, 
 * so game engines will need to wrap around and work with the ECS.
 */
public class EntityComponentSystem {
	/**
	 * A component with a reference to which entity it belongs to.
	 */
	public record AttachedComponent(Component component, int index) {}

	private final Resources resources;
	private final Systems systems;
	private final Entities entities;
	private final Query query;
	private final Events events;

	// ArrayList might not be the best data structure for this
	// TODO: maybe a HashMap keyed with component type, with values of lists of components?
	private final ArrayList<AttachedComponent> components;
	
	/**
	 * Creates a new Entity-Component-System ready for use.
	 */
	public EntityComponentSystem() {
		this.entities = new Entities();
		this.components = new ArrayList<>();
		this.systems = new Systems(this);
		this.resources = new Resources();
		this.query = new Query(this.components, this.entities);
		this.events = new Events();
	}

	/**
	 * Returns an object that is able to query many things about the components and entities of this ECS.
	 */
	public Query query() {
		return this.query;
	}
	
	/**
	 * Returns an object that handles the systems of this ECS. With this object, you can add and run
	 * systems.
	 */
	public Systems systems() {
		return this.systems;
	}
	
	/**
	 * Returns an object that handles the resources of this ECS. With it, resources can be initialized, added, and fetched.
	 */
	public Resources resources() {
		return this.resources;
	}
	
	/** 
	 * Returns an object that handles event sending and receiving in this ECS, allowing communication between systems.
	 * */
	public Events events() {
		return this.events;
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
