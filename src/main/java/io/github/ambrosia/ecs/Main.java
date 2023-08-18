package io.github.ambrosia.ecs;

import io.github.ambrosia.util.PerformanceProfiler;
import lombok.AllArgsConstructor;
import lombok.ToString;

public class Main {
	@AllArgsConstructor
	@ToString
	static class Health implements Component {
		public int health;
	}

	@AllArgsConstructor
	@ToString
	static class Name implements Component {
		public String name;
	}

	public static void main(String[] args) {
		var ecs = new ECS();
		var commands = ecs.getCommands();

		commands
			.spawn(new Health(100), new Name("Sophia"))
			.spawn(new Health(100), new Name("Bimerton"))
			.spawn(new Name("Joe Biden"))
			.spawn(new Name("Geraldine"));

		PerformanceProfiler.run(() -> {
			// query every entity with a name
			ecs.query(Name.class)
				.forEach(name -> System.out.println(name.name + " says hello!"));
		});

		PerformanceProfiler.run(() -> {
			// now, query every entity with a name, but only those who also have health
			ecs.queryWithPredicate(Health.class, Name.class)
				.forEach(name -> System.out.println(name.name + " has health!"));
		});

	}
}