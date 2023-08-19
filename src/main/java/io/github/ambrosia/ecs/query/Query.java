package io.github.ambrosia.ecs.query;

import io.github.ambrosia.ecs.Component;
import io.github.ambrosia.ecs.EntityComponentSystem;
import io.github.ambrosia.ecs.Entities;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
public class Query {
	List<EntityComponentSystem.AttachedComponent> components;
	Entities entities;

	public <T extends Component> IntermediateQuery<T> of(Class<T> type) {
		return new IntermediateQuery<>(this, type);
	}

	@SuppressWarnings("unchecked")
	private <T extends Component> Stream<T> getComponentsOfEntity(int entityId) {
		return this.components.stream()
			.filter(attached -> attached.index() == entityId)
			.map(attached -> (T) attached.component());
	}

	@SuppressWarnings("unchecked")
	private <T extends Component> Stream<T> getStreamOfType(Stream<EntityComponentSystem.AttachedComponent> componentStream, Class<T> type) {
		return componentStream
			.filter(attached -> attached.component().getClass().equals(type))
			.map(attached -> (T) attached.component());
	}

	public <T extends Component> Stream<T> components(Class<T> type) {
		return this.getStreamOfType(this.components.stream(), type);
	}

	// NOTE: Doesn't work
	@SuppressWarnings("unchecked")
	private <T extends Component> Stream<T> componentsFiltered(Class<T> type, boolean reverse, Class<? extends Component>... predicates) {
		var predicatesList = List.of(predicates);

		// todo: very inefficient
		// basically marked the indices of all entities that contain the predicate
		var entities = new ArrayList<Integer>();
		this.components
			.forEach(attachedComponent -> {
				if (predicatesList.contains(attachedComponent.component().getClass()) ^ reverse) {
					if (!entities.contains(attachedComponent.index()))
						entities.add(attachedComponent.index());
				}
			});

		Stream<Component> components = Stream.empty();

		for (int i : entities) {
			components = Stream.concat(components, this.getComponentsOfEntity(i));
		}

		var filtered = components
			.filter(component -> type.equals(component.getClass()));

		return (Stream<T>) filtered;
	}

	@SafeVarargs
	public final <T extends Component> Stream<T> componentsWith(Class<T> type, Class<? extends Component>... predicates) {
		return componentsFiltered(type, false, predicates);
	}

	@SafeVarargs
	public final <T extends Component> Stream<T> componentsWithout(Class<T> type, Class<? extends Component>... predicates) {
		return componentsFiltered(type, true, predicates);
	}
}
