package io.github.ambrosia.ecs.query;

import io.github.ambrosia.ecs.Component;

public record None<T extends Component>() implements Predicate<T> {
	
}
