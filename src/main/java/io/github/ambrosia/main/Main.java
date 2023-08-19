package io.github.ambrosia.main;

import io.github.ambrosia.ecs.Component;
import io.github.ambrosia.ecs.EntityComponentSystem;
import io.github.ambrosia.ecs.Resource;
import io.github.ambrosia.ecs.Schedule;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.stream.IntStream;

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

	static class GlobalTime implements Resource {
		public int value;

		// Resources must have a default constructor
		public GlobalTime() {
			value = 0;
		}
	}

	static void incrementGlobalTime(EntityComponentSystem ecs) {
		ecs.resources().get(GlobalTime.class).value++;
	}

	public static void resourcesExample() {
		var ecs = new EntityComponentSystem();

		ecs.resources()
			.init(GlobalTime::new);

		ecs.systems().add(Schedule.UPDATE, Main::incrementGlobalTime);

		for (int i = 0; i < 10; i++) {
			System.out.println(ecs.resources().get(GlobalTime.class).value);
			ecs.systems().runUpdateSystems();
		}
	}

	public static void main(String[] args) {
		resourcesExample();
//		var ecs = new EntityComponentSystem();
//
//		ecs
//			.spawn(new Health(100), new Name("Sophia"), new Defense(40))
//			.spawn(new Health(100), new Name("Bimerton"))
//			.spawn(new Name("Joe Biden"), new Defense(10))
//			.spawn(new Name("Geraldine"))
//			.spawn(new Health(600))
//			.spawn(new Name("Help"), new Health(160), new Defense(10))
//			.spawn(new Name("Smartie Pants"), new Health(30), new Defense(10), new Intelligence(140));
//
//		// TODO: add system schedules
//		ecs.systems().add(Schedule.STARTUP, Main::mySystem);
//
//		ecs.systems().debugRunAllSystems();
	}

	static void mySystem(EntityComponentSystem ecs) {
		ecs.query().of(Name.class)
			.with(Health.class)
			.build()
			.forEach(name -> System.out.println(name.name + " is very healthy!"));
	}
}