package io.github.ambrosia.ecs.query;

import io.github.ambrosia.ecs.Component;
import io.github.ambrosia.ecs.EntityComponentSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class IntermediateQuery<T extends Component> {
	Query query;

	Class<T> target;

	ArrayList<Class<? extends Component>> with;
	ArrayList<Class<? extends Component>> without;
	boolean requireAllPredicates;

	IntermediateQuery(Query query) {
		this.query = query;

		this.with = new ArrayList<>();
		this.without = new ArrayList<>();

		this.requireAllPredicates = true;
	}

	@SuppressWarnings("unchecked")
	public <U extends Component> IntermediateQuery<T> component(Class<U> componentClass) {
		this.target = (Class<T>) componentClass;
		return this;
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

	@SuppressWarnings("unchecked")
	// TODO: doesn't support filtering components yet
	public Stream<T> build() {
		var components = this.query.components;

		return (Stream<T>) components.stream()
			.map(EntityComponentSystem.AttachedComponent::component)
			.filter(component -> component.getClass().equals(target));
	}
}
