package io.github.ambrosia.ecs;

public enum Schedule {
	STARTUP(0),
	UPDATE(1),
	CLEANUP(2);

	public final int index;

	Schedule(int i) {
		this.index = i;
	}
}
