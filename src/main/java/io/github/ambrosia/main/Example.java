package io.github.ambrosia.main;

import io.github.ambrosia.ecs.*;
import lombok.AllArgsConstructor;

public class Example {
	public static void main(String[] args) {
		var ecs = new EntityComponentSystem();

		ecs.systems()
			.add(Schedule.STARTUP, Example::sendEvent)
			.add(Schedule.UPDATE, Example::receiveEvent);
		
		ecs.systems().runStartupSystems();

		for (int i = 0; i < 4; i++) ecs.systems().runUpdateSystems();
	}
	
	record MyEvent(String message) implements Event {}
	
	static void sendEvent(EntityComponentSystem ecs) {
		ecs.events()
			.send(new MyEvent("You smell"));
	}
	
	static void receiveEvent(EntityComponentSystem ecs) {
		ecs.events()
			.receive(MyEvent.class)
			.forEach(e -> System.out.println(e.message));
	}
	

	/*
	Current limitations:
		- it's not easy to query multiple components at once
		- destroying entities after they are spawned is not currently implemented
		- adding and removing components to existing entities is not currently implemented
	 */
}