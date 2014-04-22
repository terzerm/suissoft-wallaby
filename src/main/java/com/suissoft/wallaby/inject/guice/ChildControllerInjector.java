package com.suissoft.wallaby.inject.guice;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.suissoft.wallaby.controller.Controller;

public class ChildControllerInjector {

	public static final String PARENT_CONTROLLER_NAME = "parentController";

	public static void injectChildControllers(Injector injector, Controller controller) {
		final Module module = new Module() {
			public void configure(Binder binder) {
				binder.bind(Controller.class).annotatedWith(Names.named(PARENT_CONTROLLER_NAME)).toInstance(controller);
			}
		};
		injectChildControllers(controller, injector.createChildInjector(module), new LinkedHashSet<>());
	}

	private static void injectChildControllers(Controller controller, Injector injector, Set<Controller> visited) {
		visited.add(controller);
		Class<?> clazz = controller.getClass();
		do {
			for (final Field field : clazz.getDeclaredFields()) {
				if (Controller.class.isAssignableFrom(field.getType())) {
					field.setAccessible(true);
					final Controller childController;
					try {
						childController = (Controller) field.get(controller);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("could not access child controller field " + controller.getClass().getName() + "#" + field.getName() + ", e=" + e, e);
					}
					if (!visited.contains(childController)) {
						injector.injectMembers(childController);
						//recurse children of child
						injectChildControllers(injector.getParent(), childController);
					}
				}
			}
			//superclass controller fields
			clazz = clazz.getSuperclass();
		} while (clazz != null);
	}
	
	//no instances
	private ChildControllerInjector() {
		super();
	}
}
