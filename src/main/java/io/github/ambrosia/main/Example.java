package io.github.ambrosia.main;

import io.github.ambrosia.ecs.Component;
import io.github.ambrosia.ecs.EntityComponentSystem;
import io.github.ambrosia.ecs.Resource;
import io.github.ambrosia.ecs.Schedule;
import lombok.AllArgsConstructor;

public class Example {
	record Human() implements Component {}
	record Goblin() implements Component {}

	@AllArgsConstructor
	static class Name implements Component {
		public String value;
	}

	@AllArgsConstructor
	static class Health implements Component {
		public int value;
	}

	@AllArgsConstructor
	static class Attack implements Component {
		public int value;
	}

	static class ShouldGoblinsAttack implements Resource {
		public boolean value = true;
	}

	public static void main(String[] args) {
		var ecs = new EntityComponentSystem();

		// Initialize the ShouldGoblinsAttack resource in our ECS.
		ecs.resources()
			.init(ShouldGoblinsAttack::new);

		// Add all the systems we defined below
		ecs.systems()
			.add(Schedule.STARTUP, Example::spawnHumans, Example::spawnGoblins)
			.add(Schedule.UPDATE, Example::handleGoblinAttack, Example::handleDeath);

		// Since we don't yet have an app that handles this for us, run all the systems manually
		ecs.systems().runStartupSystems();

		// Run the update systems a few times so humans die
		for (int i = 0; i < 2; i++) ecs.systems().runUpdateSystems();
	}

	// System that spawns all the humans in our simulation.
	static void spawnHumans(EntityComponentSystem ecs) {
		// Spawn a few humans with Name and Health components
		ecs
			.spawn(new Human(), new Name("Filbert"), new Health(100))
			.spawn(new Human(), new Name("Helocox"), new Health(80))
			.spawn(new Human(), new Name("Sazrubabu"), new Health(160));
	}

	// System that spawns all the goblins in our simulation
	static void spawnGoblins(EntityComponentSystem ecs) {
		// The goblins have an Attack component in addition to Name and Health
		ecs
			.spawn(new Goblin(), new Name("Scarbo"), new Health(40), new Attack(45));
	}

	// System that handles the goblin attack on the humans
	static void handleGoblinAttack(EntityComponentSystem ecs) {
		var shouldGoblinsAttack = ecs.resources().get(ShouldGoblinsAttack.class);

		// If the ShouldGoblinsAttack resource is false, don't handle goblin attacks.
		if (!shouldGoblinsAttack.value) {
			return;
		}

		System.out.println("The goblins begin their attack!");

		// Query the Health component of all Humans
		ecs.query().of(Health.class)
			.with(Human.class)
			.build()
			.forEach(health -> {
				// Query the Attack component of all Goblins, so they can attack the humans.
				ecs.query().of(Attack.class)
					.with(Goblin.class)
					.build()
					.forEach(attack -> health.value -= attack.value);
			});
	}

	// System that checks for any deaths (health component <= 0)
	static void handleDeath(EntityComponentSystem ecs) {
		ecs.query().of(Health.class)
			.with(Name.class)
			.getEntities()
			.forEach(entity -> {
				// The following code is a perfect example on how querying for multiple components
				// needs to be improved.
				Health health = null;
				Name name = null;

				for (var component : entity) {
					var componentClass = component.getClass();

					if (componentClass.equals(Health.class)) {
						health = (Health) component;
					}

					if (componentClass.equals(Name.class)) {
						name = (Name) component;
					}

					// If we found both components, break
					if (health != null && name != null) {
						break;
					}
				}

				assert health != null && name != null;
				if (health.value <= 0) {
					System.out.println(name.value + " has died!");
				}
			});
	}

	/*
	Current limitations:
		- it's not easy to query multiple components at once
		- destroying entities after they are spawned is not currently implemented
		- adding and removing components to existing entities is not currently implemented
	 */
}