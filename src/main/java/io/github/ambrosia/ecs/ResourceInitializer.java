package io.github.ambrosia.ecs;

@FunctionalInterface
public interface ResourceInitializer<T extends Resource> {
	T init();
}
