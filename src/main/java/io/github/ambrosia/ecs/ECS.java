package io.github.ambrosia.ecs;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ECS {
	record AttachedComponent(Component component, int index) {}

	// TODO: public for debugging purposes. make these private later (or don't? if the ECS object is encapsulated, unnecessary)
	public Entities entities;

	// ArrayList might not be the best data structure for this
	public ArrayList<AttachedComponent> components;

	public ECS() {
		this.entities = new Entities();
		this.components = new ArrayList<>();
	}

	public Commands getCommands() {
		return new Commands(this);
	}

	@SuppressWarnings("unchecked")
	private <T extends Component> Stream<T> getComponentsOfEntity(int entityId) {
		return this.components.stream()
			.filter(attached -> attached.index == entityId)
			.map(attached -> (T) attached.component);
	}

	@SuppressWarnings("unchecked")
	private <T extends Component> Stream<T> getStreamOfType(Stream<AttachedComponent> componentStream, Class<T> type) {
		return componentStream
			.filter(attached -> attached.component.getClass().equals(type))
			.map(attached -> (T) attached.component);
	}

	public <T extends Component> Stream<T> query(Class<T> type) {
		return this.getStreamOfType(this.components.stream(), type);
	}

	private static <T> Stream<T> concat(Stream<T> stream, Stream<T> previous) {
		return previous == null ? stream : Stream.concat(previous, stream);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component, U extends Component> Stream<T> queryWithPredicate(Class<U> predicate, Class<T> type) {
		// todo: very inefficient
		// basically marked the indices of all entities that contain the predicate
		var entities = new ArrayList<Integer>();
		this.components
			.forEach(attachedComponent -> {
				if (attachedComponent.component.getClass().equals(predicate)) {
					if (!entities.contains(attachedComponent.index))
						entities.add(attachedComponent.index);
				}
			});

		Stream<Component> components = null;

		for (int i : entities) {
			components = concat(this.getComponentsOfEntity(i), components);
		}

		if (components == null) components = Stream.empty();

		var filtered = components
			.filter(component -> component.getClass().equals(type));

		return (Stream<T>) filtered;
	}


}
