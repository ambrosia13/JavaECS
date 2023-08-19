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
		var ecs = new EntityComponentSystem();

		ecs
			.spawn(new Health(100), new Name("Sophia"), new Defense(40))
			.spawn(new Health(100), new Name("Bimerton"))
			.spawn(new Name("Joe Biden"), new Defense(10))
			.spawn(new Name("Geraldine"));

		ecs.query().start()
			.component(Name.class) // the component we want to operate on
			.with(Health.class) // the selected entity must have this component
			.without(Defense.class) // the selected entity can't have this component
			.requireAllPredicates(true) // defaults to true, so not really needed.
			.build() // gives a stream of components that match the search
			.forEach(System.out::println);
	}
}