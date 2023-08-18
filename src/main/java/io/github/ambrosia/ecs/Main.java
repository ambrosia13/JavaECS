package io.github.ambrosia.ecs;

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
	static class Defense implements Component {
		public int defense;
	}

	@AllArgsConstructor
	@ToString
	static class Name implements Component {
		public String name;
	}

	public static void main(String[] args) {
		var ecs = new ECS();

		ecs
			.spawn(new Health(100), new Name("Sophia"), new Defense(40))
			.spawn(new Health(100), new Name("Bimerton"))
			.spawn(new Name("Joe Biden"), new Defense(10))
			.spawn(new Name("Geraldine"));

		// query every entity with a name
		ecs.query().components(Name.class)
			.forEach(name -> System.out.println(name.name + " says hello!"));

		// now, query every entity with a name, but only those who also have health
		ecs.query().componentsWith(Health.class, Name.class)
			.forEach(name -> System.out.println(name.name + " has health!"));

		ecs.query().components(Defense.class)
			.forEach(defense -> System.out.println("An entity has " + defense.defense + " defense"));
	}
}