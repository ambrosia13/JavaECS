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

	@AllArgsConstructor
	@ToString
	static class Intelligence implements Component {
		public int value;
	}

	public static void main(String[] args) {
		var ecs = new EntityComponentSystem();

		ecs
			.spawn(new Health(100), new Name("Sophia"), new Defense(40))
			.spawn(new Health(100), new Name("Bimerton"))
			.spawn(new Name("Joe Biden"), new Defense(10))
			.spawn(new Name("Geraldine"))
			.spawn(new Health(600))
			.spawn(new Name("Help"), new Health(160), new Defense(10))
			.spawn(new Name("Smartie Pants"), new Health(30), new Defense(10), new Intelligence(140));

		// TODO: add system schedules
		ecs.addSystem(Main::mySystem);

		ecs.runSystems();
	}

	static void mySystem(EntityComponentSystem ecs) {
		ecs.query().of(Name.class)
			.with(Health.class)
			.build()
			.forEach(name -> System.out.println(name.name + " is very healthy!"));
	}
}