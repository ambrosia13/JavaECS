package io.github.ambrosia.ecs;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
public class Query {
	List<ECS.AttachedComponent> components;
	Entities entities;

	@SuppressWarnings("unchecked")
	private <T extends Component> Stream<T> getComponentsOfEntity(int entityId) {
		return this.components.stream()
			.filter(attached -> attached.index() == entityId)
			.map(attached -> (T) attached.component());
	}

	@SuppressWarnings("unchecked")
	private <T extends Component> Stream<T> getStreamOfType(Stream<ECS.AttachedComponent> componentStream, Class<T> type) {
		return componentStream
			.filter(attached -> attached.component().getClass().equals(type))
			.map(attached -> (T) attached.component());
	}

	public <T extends Component> Stream<T> components(Class<T> type) {
		return this.getStreamOfType(this.components.stream(), type);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component, U extends Component> Stream<T> componentsWith(Class<U> predicate, Class<T> type) {
		// todo: very inefficient
		// basically marked the indices of all entities that contain the predicate
		var entities = new ArrayList<Integer>();
		this.components
			.forEach(attachedComponent -> {
				if (attachedComponent.component().getClass().equals(predicate)) {
					if (!entities.contains(attachedComponent.index()))
						entities.add(attachedComponent.index());
				}
			});

		Stream<Component> components = Stream.empty();

		for (int i : entities) {
			components = Stream.concat(components, this.getComponentsOfEntity(i));
		}

		var filtered = components
			.filter(component -> component.getClass().equals(type));

		return (Stream<T>) filtered;
	}

}
