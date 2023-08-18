package io.github.ambrosia.ecs.query;

import io.github.ambrosia.ecs.Component;

public sealed interface Filter<T extends Component> extends Predicate<T> {
	record With<T extends Component>(Class<T> type) implements Filter<T> {
		@Override
		public Class<T> getType() {
			return type;
		}
	}
	
	record Without<T extends Component>(Class<T> type) implements Filter<T> {
		@Override
		public Class<T> getType() {
			return type;
		}
	}
	
	Class<T> getType();
}
