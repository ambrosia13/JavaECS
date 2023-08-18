package io.github.ambrosia.ecs.query;

import io.github.ambrosia.ecs.Component;

public sealed interface Predicate<T extends Component> permits Filter, None {
	default boolean hasFilter() {
		return this instanceof Filter<T>;
	}
}
