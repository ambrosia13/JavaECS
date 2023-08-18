package io.github.ambrosia.ecs;

public class Entities {
	private int count;

	Entities() {
		count = 0;
	}

	public int create() {
		return this.count++;
	}

	public int count() {
		return this.count;
	}

	@Override
	public String toString() {
		return "Entities{" +
			"count=" + count +
			'}';
	}
}

