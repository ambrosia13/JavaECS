package io.github.ambrosia.ecs;

import java.util.ArrayList;
import java.util.List;

public class Systems {
	EntityComponentSystem ecs;
	List<ArrayList<EcsSystem>> systems;

	private boolean startupHasRun = false;
	private boolean cleanupHasRun = false;

	public Systems(EntityComponentSystem ecs) {
		this.ecs = ecs;
		systems = List.of(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
	}

	public Systems add(Schedule schedule, EcsSystem... systems) {
		this.systems.get(schedule.index).addAll(List.of(systems));
		return this;
	}

	public void runStartupSystems() {
		if (startupHasRun) throw new IllegalStateException("Tried to run startup system after it has already run");
		startupHasRun = true;

		systems.get(Schedule.STARTUP.index).forEach(system -> system.run(this.ecs));
	}
	public void runUpdateSystems() {
		systems.get(Schedule.UPDATE.index).forEach(system -> system.run(this.ecs));
	}
	public void runCleanupSystems() {
		if (cleanupHasRun) throw new IllegalStateException("Tried to run cleanup system after it has already run");
		cleanupHasRun = true;

		systems.get(Schedule.CLEANUP.index).forEach(system -> system.run(this.ecs));
	}

	public void runAllSystems() {
		systems.forEach(schedule -> schedule.forEach(system -> system.run(this.ecs)));
	}
}
