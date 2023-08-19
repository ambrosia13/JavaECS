package io.github.ambrosia.ecs;

@FunctionalInterface
// The name of this class is stupid, but System conflicts with java's.
public interface EcsSystem {
	void run(EntityComponentSystem ecs);
}
