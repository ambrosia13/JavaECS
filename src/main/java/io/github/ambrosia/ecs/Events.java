package io.github.ambrosia.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Events {
	List<Event> events;
	
	Events() {
		events = new ArrayList<>();
	}
	
	public <T extends Event> Events send(T event) {
		events.add(event);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Event> Stream<T> receive(Class<T> type) {
		var matches = this.events.stream()
			.filter(e -> e.getClass().equals(type))
			.map(e -> (T) e)
			.toList();
		
		this.events.removeIf(e -> e.getClass().equals(type));
		
		return matches.stream();
	}
}
