package io.github.ambrosia.ecs.query;

import io.github.ambrosia.ecs.Component;
import io.github.ambrosia.ecs.EntityComponentSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntermediateQuery<T extends Component> {
	Query query;

	Class<T> target;

	ArrayList<Class<? extends Component>> with;
	ArrayList<Class<? extends Component>> without;
	boolean requireAllPredicates;

	IntermediateQuery(Query query, Class<T> type) {
		this.target = type;

		this.query = query;

		this.with = new ArrayList<>();
		this.without = new ArrayList<>();

		this.requireAllPredicates = true;
	}

	@SafeVarargs
	public final IntermediateQuery<T> with(Class<? extends Component>... componentClasses) {
		this.with.addAll(List.of(componentClasses));
		return this;
	}

	@SafeVarargs
	public final IntermediateQuery<T> without(Class<? extends Component>... componentClasses) {
		this.without.addAll(List.of(componentClasses));
		return this;
	}

	public IntermediateQuery<T> requireAllPredicates(boolean b) {
		this.requireAllPredicates = b;
		return this;
	}

	// This method maybe isn't a good idea but it makes things easier
	private ArrayList<ArrayList<Component>> objectifyComponents() {
		var components = new ArrayList<ArrayList<Component>>();

		IntStream.range(0, this.query.entities.count())
			.forEach(i -> components.add(new ArrayList<>()));

		this.query.components
			.forEach(
				attached -> components.get(attached.index()).add(attached.component())
			);

		return components;
	}

	private boolean resolveQueries(ArrayList<Component> entity) {
		var targetMatches = new ArrayList<Boolean>();
		var withMatches = new ArrayList<Boolean>();
		var withoutMatches = new ArrayList<Boolean>();

		entity.forEach(component -> {
			var componentClass = component.getClass();

			if (componentClass.equals(this.target)) {
				targetMatches.add(true);

				// If it's the component that we're querying in the first place,
				// don't check if it matches the filters.
				return;
			}

			if (this.with.isEmpty()) return;
			withMatches.add(this.with.contains(componentClass));

			if (this.without.isEmpty()) return;
			withoutMatches.add(this.without.contains(componentClass));
		});

		boolean containsTarget = targetMatches.stream().anyMatch(b -> b);
		boolean containsWith = withMatches.stream().mapToInt(b -> b ? 1 : 0).sum() >= this.with.size() || this.with.isEmpty();
		boolean containsWithout = withoutMatches.stream().anyMatch(b -> b) && !this.without.isEmpty();

		return  containsTarget && containsWith && !containsWithout;
	}

	@SuppressWarnings("unchecked")
	private T getTargetComponentFromEntity(ArrayList<Component> entity) {
		T target = null;

		for (var component : entity) {
			if (component.getClass().equals(this.target)) {
				target = (T) component;
			}
		}

		if (target == null) {
			throw new IllegalStateException("Filtered entity didn't have the target component");
		}

		return target;
	}

	public Stream<T> build() {
		var entityComponents = objectifyComponents();

		return (Stream<T>) entityComponents.stream()
			.filter(this::resolveQueries)
			.map(this::getTargetComponentFromEntity);

//		return (Stream<T>) components.stream()
//			.map(EntityComponentSystem.AttachedComponent::component)
//			.filter(component -> component.getClass().equals(target));
	}
}
